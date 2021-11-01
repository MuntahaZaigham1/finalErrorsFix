package com.fastcode.example.addons.reporting.application.report;

import java.time.*;
import com.fastcode.example.addons.reporting.application.reportversion.IReportversionAppService;
import com.fastcode.example.addons.reporting.application.reportversion.IReportversionMapper;
import com.fastcode.example.addons.reporting.application.reportversion.dto.CreateReportversionInput;
import com.fastcode.example.addons.reporting.application.reportversion.dto.CreateReportversionOutput;
import com.fastcode.example.addons.reporting.application.reportversion.dto.UpdateReportversionInput;
import com.fastcode.example.addons.reporting.application.reportversion.dto.UpdateReportversionOutput;
import com.fastcode.example.addons.reporting.application.report.dto.*;
import com.fastcode.example.addons.reporting.domain.report.IReportRepository;
import com.fastcode.example.addons.reporting.domain.reportversion.IReportversionRepository;
import com.fastcode.example.addons.reporting.domain.dashboardversionreport.Dashboardversionreport;
import com.fastcode.example.addons.reporting.domain.report.QReport;
import com.fastcode.example.addons.reporting.domain.report.Report;
import com.fastcode.example.addons.reporting.domain.reportversion.Reportversion;
import com.fastcode.example.addons.reporting.domain.reportversion.ReportversionId;
import com.fastcode.example.domain.extended.authorization.users.IUsersRepositoryExtended;
import com.fastcode.example.addons.reporting.domain.dashboardversionreport.IDashboardversionreportRepository;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("reportAppService")
public class ReportAppService implements IReportAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	@Qualifier("reportRepository")
	protected IReportRepository _reportRepository;

	@Autowired
	@Qualifier("reportversionRepository")
	protected IReportversionRepository _reportversionRepository;

	@Autowired
	@Qualifier("usersRepositoryExtended")
	protected IUsersRepositoryExtended _usersRepository;

	@Autowired
	@Qualifier("dashboardversionreportRepository")
	protected IDashboardversionreportRepository _reportDashboardRepository;

	@Autowired
	@Qualifier("reportversionAppService")
	protected IReportversionAppService _reportversionAppservice;

	@Autowired
	@Qualifier("IReportMapperImpl")
	protected IReportMapper mapper;

	@Autowired
	@Qualifier("IReportversionMapperImpl")
	protected IReportversionMapper reportversionMapper;

	@Autowired
	protected LoggingHelper logHelper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateReportOutput create(CreateReportInput input) {

		Report report = mapper.createReportInputToReport(input);
		if(input.getOwnerId()!=null)
	  	{
			Users foundUsers = _usersRepository.findById(input.getOwnerId()).orElse(null);
	        
			if(foundUsers!=null) {
				foundUsers.addReports(report);
			}
		}

		Report createdReport = _reportRepository.save(report);
		CreateReportversionInput reportversion = mapper.createReportInputToCreateReportversionInput(input);
		reportversion.setReportId(createdReport.getId());

		CreateReportversionOutput reportversionOutput = _reportversionAppservice.create(reportversion);
		return mapper.reportAndCreateReportversionOutputToCreateReportOutput(createdReport, reportversionOutput);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateReportOutput update(Long reportId, UpdateReportInput input) {

		ReportversionId reportversionId = new ReportversionId(input.getUserId(), reportId, "running");

		Reportversion rv = _reportversionRepository.findById(reportversionId).orElse(null);
		UpdateReportversionInput reportversion = mapper.updateReportInputToUpdateReportversionInput(input);
		reportversion.setReportId(rv.getReport().getId());
		reportversion.setReportVersion(rv.getReportVersion());
        reportversion.setVersiono(rv.getVersiono());
        reportversion.setIsRefreshed(false);
        
		UpdateReportversionOutput reportversionOutput =  _reportversionAppservice.update(reportversionId, reportversion);
		
		Report foundReport = _reportRepository.findById(reportId).orElse(null);
		if(foundReport.getUsers() !=null && foundReport.getUsers().getId() == input.getUserId() )
		{
			foundReport.setIsPublished(false);
			foundReport = _reportRepository.save(foundReport);
		}

		return mapper.reportAndUpdateReportversionOutputToUpdateReportOutput(foundReport,reportversionOutput);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long reportId,  Long userId) {

		Report existing = _reportRepository.findById(reportId).orElse(null); 
		
		List<Dashboardversionreport> dvrList = _reportDashboardRepository.findByReportId(reportId);
	     
		for(Dashboardversionreport dvr : dvrList) {
			dvr.getDashboardversionEntity().removeDashboardversionreport(dvr);
			dvr.getReport().removeDashboardversionreport(dvr);
			_reportDashboardRepository.delete(dvr);
		}
		
		existing.removeReportversion(_reportversionRepository.findById(new ReportversionId(userId, reportId, "running")).get());
		existing.removeReportversion(_reportversionRepository.findById(new ReportversionId(userId, reportId, "published")).get());
		
		_reportversionAppservice.delete(new ReportversionId(userId, reportId, "running"));
		_reportversionAppservice.delete(new ReportversionId(userId, reportId, "published"));
        
		_reportRepository.delete(existing);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindReportByIdOutput findById(Long reportId) {

		Report foundReport = _reportRepository.findById(reportId).orElse(null);
		if (foundReport == null)  
			return null ; 

		Reportversion reportversion = _reportversionRepository.findById(new ReportversionId(foundReport.getUsers().getId(), foundReport.getId(), "running")).orElse(null);

		FindReportByIdOutput output=mapper.reportToFindReportByIdOutput(foundReport,reportversion); 
		return output;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindReportByIdOutput findByReportIdAndUsersId(Long reportId, Long userId, String version) {

		Report foundReport = _reportRepository.findByReportIdAndUsersId(reportId, userId);
		if (foundReport == null)  {
			return null;
		}

		Reportversion reportVersion, publishedversion;
		publishedversion = _reportversionRepository.findById(new ReportversionId(userId, reportId, "published")).orElse(null);
    
		if(StringUtils.isNotBlank(version) && version.equalsIgnoreCase("published")) {
			reportVersion = publishedversion;
		} else {
			reportVersion =_reportversionRepository.findById(new ReportversionId(userId, reportId, "running")).orElse(null);
		}
		FindReportByIdOutput output  = mapper.reportEntitiesToFindReportByIdOutput(foundReport, reportVersion); 
		
		return output;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ReportDetailsOutput publishReport( Long userId, Long reportId) {

		Report foundReport = _reportRepository.findById(reportId).orElse(null);
		
		foundReport.setIsPublished(true);
		foundReport = _reportRepository.save(foundReport);
		Reportversion foundReportversion = _reportversionRepository.findById(new ReportversionId(userId, reportId, "running")).orElse(null);
		Reportversion foundPublishedversion = _reportversionRepository.findById(new ReportversionId(userId, reportId, "published")).orElse(null);
		Reportversion publishedVersion = reportversionMapper.reportversionToReportversion(foundReportversion,userId, "published");
		
		if(foundPublishedversion !=null)
		{
			publishedVersion.setVersiono(foundPublishedversion.getVersiono());
		}
		else
			publishedVersion.setVersiono(0L);
		
		_reportversionRepository.save(publishedVersion);
        foundReportversion.setIsRefreshed(true);
		_reportversionRepository.save(foundReportversion);
		
		return mapper.reportEntitiesToReportDetailsOutput(foundReport,foundReportversion);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ReportDetailsOutput refreshReport( Long userId, Long reportId) {
		Report foundReport = _reportRepository.findById(reportId).orElse(null);
		
		if(foundReport !=null && foundReport.getUsers() !=null && foundReport.getUsers().getId() == userId) {
			
			Reportversion ownerPublishedversion = _reportversionRepository.findById(new ReportversionId(userId, reportId, "published")).orElse(null);
			Reportversion ownerRunningversion = _reportversionRepository.findById(new ReportversionId(userId, reportId, "running")).orElse(null);
		
		    Users foundUsers = _usersRepository.findById(userId ).orElse(null);
			Reportversion updatedVersion = reportversionMapper.reportversionToReportversion(ownerPublishedversion, userId, "running"); 
			updatedVersion.setUsers(foundUsers);
			updatedVersion.setVersiono(ownerRunningversion.getVersiono());
			updatedVersion.setIsRefreshed(true);
			_reportversionRepository.save(updatedVersion);
			
			Reportversion runningversion = _reportversionRepository.findById(new ReportversionId(userId, reportId, "running")).orElse(null);
			ReportDetailsOutput output = mapper.reportEntitiesToReportDetailsOutput(foundReport,runningversion);
			return output;
			
		}
		
		return null;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<ReportDetailsOutput> getReports( Long userId,String search, Pageable pageable) throws Exception
	{ 
		Page<ReportDetailsOutput> foundReport = _reportRepository.getAllReportsByUsersId(userId, search, pageable) ;
		List<ReportDetailsOutput> reportList = foundReport.getContent();

		return reportList ;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindReportByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<Report> foundReport = _reportRepository.findAll(search(search), pageable);
		List<Report> reportList = foundReport.getContent();
		Iterator<Report> reportIterator = reportList.iterator(); 
		List<FindReportByIdOutput> output = new ArrayList<>();

		while (reportIterator.hasNext()) {
			Report report = reportIterator.next();
			Reportversion reportVersion =_reportversionRepository.findById(new ReportversionId(report.getUsers().getId(),report.getId(), "running")).orElse(null);
			FindReportByIdOutput reportOutput  = mapper.reportEntitiesToFindReportByIdOutput(report, reportVersion); 
			output.add(reportOutput); 

		}
		return output;
	}

	protected BooleanBuilder search(SearchCriteria search) throws Exception {

		QReport report= QReport.report;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(report, map,search.getJoinColumns());
		}
		return null;
	}

	protected void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
					list.get(i).replace("%20","").trim().equals("userId") ||
					list.get(i).replace("%20","").trim().equals("ctype") ||
					list.get(i).replace("%20","").trim().equals("description") ||
					list.get(i).replace("%20","").trim().equals("id") ||
					list.get(i).replace("%20","").trim().equals("query") ||
					list.get(i).replace("%20","").trim().equals("reportType") ||
					list.get(i).replace("%20","").trim().equals("reportdashboard") ||
					list.get(i).replace("%20","").trim().equals("title") ||
					list.get(i).replace("%20","").trim().equals("user")
					)) 
			{
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	protected BooleanBuilder searchKeyValuePair(QReport report, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("isPublished")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(report.isPublished.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(report.isPublished.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
			if(joinCol != null && joinCol.getKey().equals("ownerId")) {
				builder.and(report.users.id.eq(Long.parseLong(joinCol.getValue())));
			}
		}
		return builder;
	}

	public Map<String,String> parseReportdashboardJoinColumn(String keysString) {

		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("reportId", keysString);
		return joinColumnMap;
	}

}



