package com.fastcode.example.addons.reporting.application.dashboard;

import com.fastcode.example.addons.reporting.application.dashboard.dto.*;
import com.fastcode.example.addons.reporting.application.dashboardversion.IDashboardversionAppService;
import com.fastcode.example.addons.reporting.application.dashboardversion.IDashboardversionMapper;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.CreateDashboardversionInput;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.CreateDashboardversionOutput;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.UpdateDashboardversionInput;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.UpdateDashboardversionOutput;
import com.fastcode.example.addons.reporting.application.dashboardversionreport.IDashboardversionreportAppService;
import com.fastcode.example.addons.reporting.application.report.IReportAppService;
import com.fastcode.example.addons.reporting.application.report.IReportMapper;
import com.fastcode.example.addons.reporting.application.report.dto.CreateReportInput;
import com.fastcode.example.addons.reporting.application.report.dto.CreateReportOutput;
import com.fastcode.example.addons.reporting.application.report.dto.FindReportByIdOutput;
import com.fastcode.example.addons.reporting.application.report.dto.UpdateReportInput;
import com.fastcode.example.addons.reporting.domain.dashboard.IDashboardRepository;
import com.fastcode.example.addons.reporting.domain.dashboardversion.IDashboardversionRepository;
import com.fastcode.example.addons.reporting.domain.dashboardversionreport.IDashboardversionreportRepository;
import com.fastcode.example.addons.reporting.domain.dashboard.QDashboard;
import com.fastcode.example.addons.reporting.domain.report.Report;
import com.fastcode.example.addons.reporting.domain.dashboard.Dashboard;
import com.fastcode.example.addons.reporting.domain.reportversion.Reportversion;
import com.fastcode.example.addons.reporting.domain.reportversion.ReportversionId;
import com.fastcode.example.addons.reporting.domain.dashboardversion.Dashboardversion;
import com.fastcode.example.addons.reporting.domain.dashboardversion.DashboardversionId;
import com.fastcode.example.addons.reporting.domain.dashboardversionreport.Dashboardversionreport;
import com.fastcode.example.addons.reporting.domain.dashboardversionreport.DashboardversionreportId;
import com.fastcode.example.domain.extended.authorization.users.IUsersRepositoryExtended;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.addons.reporting.domain.report.IReportRepository;
import com.fastcode.example.addons.reporting.domain.reportversion.IReportversionRepository;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("dashboardAppService")
public class DashboardAppService implements IDashboardAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	@Qualifier("dashboardRepository")
	protected IDashboardRepository _dashboardRepository;

	@Autowired
	@Qualifier("dashboardversionreportAppService")
	protected IDashboardversionreportAppService _reportDashboardAppService;

	@Autowired
	@Qualifier("reportAppService")
	protected IReportAppService _reportAppService;

	@Autowired
	@Qualifier("dashboardversionAppService")
	protected IDashboardversionAppService _dashboardversionAppservice;

	@Autowired
	@Qualifier("IDashboardversionMapperImpl")
	protected IDashboardversionMapper dashboardversionMapper;

	@Autowired
	@Qualifier("dashboardversionRepository")
	protected IDashboardversionRepository _dashboardversionRepository;

	@Autowired
	@Qualifier("reportversionRepository")
	protected IReportversionRepository _reportversionRepository;

	@Autowired
	@Qualifier("dashboardversionreportRepository")
	protected IDashboardversionreportRepository _reportDashboardRepository;

	@Autowired
	@Qualifier("usersRepositoryExtended")
	protected IUsersRepositoryExtended _usersRepository;

	@Autowired
	@Qualifier("IDashboardMapperImpl")
	protected IDashboardMapper mapper;

	@Autowired
	@Qualifier("IReportMapperImpl")
	protected IReportMapper reportMapper;

	@Autowired
	@Qualifier("reportRepository")
	protected IReportRepository _reportRepository;

	@Autowired
	protected LoggingHelper logHelper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateDashboardOutput create(CreateDashboardInput input) {

		Dashboard dashboard = mapper.createDashboardInputToDashboard(input); 
		if(input.getOwnerId()!=null)
	  	{
			Users foundUsers = _usersRepository.findById(input.getOwnerId()).orElse(null);
	        
			if(foundUsers!=null) {
				foundUsers.addDashboards(dashboard);
			}
		}

		Dashboard createdDashboard = _dashboardRepository.save(dashboard);
		CreateDashboardversionInput dashboardversion = mapper.creatDashboardInputToCreateDashboardversionInput(input);
		dashboardversion.setDashboardId(createdDashboard.getId());

		CreateDashboardversionOutput dashboardversionOutput = _dashboardversionAppservice.create(dashboardversion);

		return mapper.dashboardAndCreateDashboardversionOutputToCreateDashboardOutput(createdDashboard,dashboardversionOutput);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateDashboardOutput update(Long dashboardId, UpdateDashboardInput input) {

		DashboardversionId dashboardversionId = new DashboardversionId(input.getUserId(), dashboardId, "running");

		Dashboardversion rv = _dashboardversionRepository.findById(dashboardversionId).orElse(null);
		UpdateDashboardversionInput dashboardversion = mapper.updateDashboardInputToUpdateDashboardversionInput(input);
		dashboardversion.setDashboardId(rv.getDashboard().getId());
		dashboardversion.setDashboardVersion(rv.getDashboardVersion());
		dashboardversion.setVersiono(rv.getVersiono());
		dashboardversion.setIsRefreshed(false);

		UpdateDashboardversionOutput dashboardversionOutput =  _dashboardversionAppservice.update(dashboardversionId, dashboardversion);
		Dashboard foundDashboard = _dashboardRepository.findById(dashboardId).orElse(null);
		if(foundDashboard.getUsers() !=null && foundDashboard.getUsers().getId() == input.getUserId() )
		{
			foundDashboard.setIsPublished(false);
			foundDashboard = _dashboardRepository.save(foundDashboard);
		}

		Long count =0L;
		for(UpdateReportInput reportInput : input.getReportDetails()) {
			Dashboardversionreport dashboardreport = _reportDashboardRepository.findById(new DashboardversionreportId(dashboardId,  input.getUserId(),"running", reportInput.getId())).orElse(null);
			if(reportInput.getReportWidth() !=null) {
				dashboardreport.setReportWidth(reportInput.getReportWidth());
			}
			else {
				dashboardreport.setReportWidth("mediumchart");
			}
			dashboardreport.setOrderId(count);
			count ++;

			Dashboardversionreport dashboardVersionReport = _reportDashboardRepository.save(dashboardreport);
		}

		return mapper.dashboardAndUpdateDashboardversionOutputToUpdateDashboardOutput(foundDashboard,dashboardversionOutput);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long dashboardId,  Long userId) {

		Dashboard existing = _dashboardRepository.findById(dashboardId).orElse(null); 
		 List<Dashboardversionreport> dvrList = _reportDashboardRepository.findByDashboardId(dashboardId);
	     
			for(Dashboardversionreport dvr : dvrList) {
				dvr.getDashboardversionEntity().removeDashboardversionreport(dvr);
				dvr.getReport().removeDashboardversionreport(dvr);
				_reportDashboardRepository.delete(dvr);
			}

		existing.removeDashboardversion(_dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "running")).get());
		existing.removeDashboardversion(_dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "published")).get());
		
		_dashboardversionAppservice.delete(new DashboardversionId(userId, dashboardId, "running"));
		_dashboardversionAppservice.delete(new DashboardversionId(userId, dashboardId, "published"));

		_dashboardRepository.delete(existing);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteReportFromDashboard(Long dashboardId, Long reportId,  Long userId) {

		Dashboardversionreport existing = _reportDashboardRepository.findById(new DashboardversionreportId(dashboardId, userId, "running", reportId)).get();
		
		_dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "running")).get().removeDashboardversionreport(existing);
		_reportRepository.findByReportIdAndUsersId(reportId, userId).removeDashboardversionreport(existing);
		
	    _reportDashboardAppService.delete(new DashboardversionreportId(dashboardId, userId, "running", reportId));
	
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindDashboardByIdOutput findById(Long dashboardId) {

		Dashboard foundDashboard = _dashboardRepository.findById(dashboardId).orElse(null);
		if (foundDashboard == null)  
			return null ; 

		FindDashboardByIdOutput output = mapper.dashboardToFindDashboardByIdOutput(foundDashboard); 
		return output;
	}

	public List<FindReportByIdOutput> setReportsList(Long dashboardId,  Long userId, String version)
	{
		List<Dashboardversionreport> reportDashboardList = _reportDashboardRepository.findByDashboardIdAndVersionAndUsersId(dashboardId, version, userId);

		List<FindReportByIdOutput> reportDetails = new ArrayList<>();
		for(Dashboardversionreport rd : reportDashboardList)
		{
			Reportversion reportversion = _reportversionRepository.findById(new ReportversionId(rd.getUserId(), rd.getReportId(), version)).orElse(null);
			FindReportByIdOutput output= reportMapper.reportEntitiesToFindReportByIdOutput(rd.getReport(), reportversion); 
			output.setOrderId(rd.getOrderId());
			output.setReportWidth(rd.getReportWidth());
			reportDetails.add(output);
		}

		List<FindReportByIdOutput>  sortedReports = reportDetails.stream()
				.sorted(Comparator.comparing(FindReportByIdOutput::getOrderId))
				.collect(Collectors.toList());

		return sortedReports;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindDashboardByIdOutput findByDashboardIdAndUsersId(Long dashboardId,  Long userId, String version) {

		Dashboard foundDashboard = _dashboardRepository.findByDashboardIdAndUsersId(dashboardId, userId);
		if (foundDashboard == null)  
			return null ; 

		Dashboardversion dashboardVersion, publishedversion;
		publishedversion = _dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "published")).orElse(null);
		if(StringUtils.isNotBlank(version) && version.equalsIgnoreCase("published")) {
			dashboardVersion = publishedversion;
		} else {
			dashboardVersion = _dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "running")).orElse(null);	
		}
		FindDashboardByIdOutput output = mapper.dashboardEntitiesToFindDashboardByIdOutput(foundDashboard,dashboardVersion); 

		return output;

	}

	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<DashboardDetailsOutput> getDashboards( Long userId,String search, Pageable pageable) throws Exception
	{ 
		Page<DashboardDetailsOutput> foundDashboard = _dashboardRepository.getAllDashboardsByUsersId(userId, search, pageable) ;
		List<DashboardDetailsOutput> dashboardList = foundDashboard.getContent();

		return dashboardList ;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<DashboardDetailsOutput> getAvailableDashboards( Long userId, Long reportId, String search, Pageable pageable) throws Exception
	{ 
		Page<DashboardDetailsOutput> foundDashboard = _dashboardRepository.getAvailableDashboardsByUsersId(userId, reportId, search, pageable) ;
		List<DashboardDetailsOutput> dashboardList = foundDashboard.getContent();

		return dashboardList ;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindDashboardByIdOutput publishDashboard( Long userId, Long dashboardId) {

		Dashboard foundDashboard = _dashboardRepository.findById(dashboardId).orElse(null);
		foundDashboard.setIsPublished(true);
		foundDashboard = _dashboardRepository.save(foundDashboard);

		Dashboardversion foundDashboardversion = _dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "running")).orElse(null);
		Dashboardversion foundPublishedversion = _dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "published")).orElse(null);
		Dashboardversion publishedVersion = dashboardversionMapper.dashboardversionToDashboardversion(foundDashboardversion, userId, "published");
		
		if(foundPublishedversion !=null)
		{
			publishedVersion.setVersiono(foundPublishedversion.getVersiono());
		}
		else
			publishedVersion.setVersiono(0L);
		
		_dashboardversionRepository.save(publishedVersion);
		foundDashboardversion.setIsRefreshed(true);
		_dashboardversionRepository.save(foundDashboardversion);
		
		//check if report is added in running version
		List<Dashboardversionreport> dashboardreportList  = _reportDashboardRepository.findByDashboardIdAndVersionAndUsersId(dashboardId, "running", userId);

		for(Dashboardversionreport dashboardreport : dashboardreportList)
		{
			Dashboardversionreport publishedDashboardreport = _reportDashboardRepository.findById(new DashboardversionreportId(dashboardId,userId,"published", dashboardreport.getReportId())).orElse(null);
			if(publishedDashboardreport !=null) {
				publishedDashboardreport.setOrderId(dashboardreport.getOrderId());
				publishedDashboardreport.setReportWidth(dashboardreport.getReportWidth());
				_reportDashboardRepository.save(publishedDashboardreport);

			}
			else
			{
				publishedDashboardreport = dashboardversionMapper.dashboardversionreportToDashboardversionreport(dashboardreport, userId, "published");
				_reportDashboardRepository.save(publishedDashboardreport);
			}

			_reportAppService.publishReport(dashboardreport.getUserId(), dashboardreport.getReportId());
		}

		//check if report is removed from running version
		List<Dashboardversionreport> dashboardPublishedreportList  = _reportDashboardRepository.findByDashboardIdAndVersionAndUsersId(dashboardId, "published", userId);

		for(Dashboardversionreport dashboardeport : dashboardPublishedreportList)
		{
			Dashboardversionreport runningDashboardreport = _reportDashboardRepository.findById(new DashboardversionreportId(dashboardId,userId,"running", dashboardeport.getReportId())).orElse(null);
			if(runningDashboardreport == null) {

				runningDashboardreport = dashboardversionMapper.dashboardversionreportToDashboardversionreport(dashboardeport,userId, "published");
				runningDashboardreport.getReport().removeDashboardversionreport(runningDashboardreport);
				runningDashboardreport.getDashboardversionEntity().removeDashboardversionreport(runningDashboardreport);
				
				_reportDashboardRepository.delete(runningDashboardreport);
			}
		}

		return mapper.dashboardEntitiesToFindDashboardByIdOutput(foundDashboard,foundDashboardversion);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindDashboardByIdOutput refreshDashboard( Long userId, Long dashboardId) {
		Dashboard foundDashboard = _dashboardRepository.findById(dashboardId).orElse(null);

		if(foundDashboard !=null && foundDashboard.getUsers() !=null && foundDashboard.getUsers().getId() == userId) {
			
			Dashboardversion ownerPublishedversion = _dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "published")).orElse(null);
			Dashboardversion ownerRunningversion = _dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "running")).orElse(null);
			
		    Users foundUsers = _usersRepository.findById(userId ).orElse(null);
			
			Dashboardversion updatedVersion = dashboardversionMapper.dashboardversionToDashboardversion(ownerPublishedversion,userId, "running"); 
			updatedVersion.setUsers(foundUsers);
			updatedVersion.setVersiono(ownerRunningversion.getVersiono());
			updatedVersion.setIsRefreshed(true);
			_dashboardversionRepository.save(updatedVersion);
			
			List<Dashboardversionreport> dvrList = _reportDashboardRepository.findByDashboardIdAndVersionAndUsersId(dashboardId, "published",userId);
			for(Dashboardversionreport dvr : dvrList)
			{
				Dashboardversionreport updateDashboardreport = dashboardversionMapper.dashboardversionreportToDashboardversionreport(dvr, userId, "running");
				_reportDashboardRepository.save(updateDashboardreport);
				_reportAppService.refreshReport(userId,dvr.getReportId());

			}
			
			Dashboardversion runningversion = _dashboardversionRepository.findById(new DashboardversionId(userId, dashboardId, "running")).orElse(null);

			FindDashboardByIdOutput output = mapper.dashboardEntitiesToFindDashboardByIdOutput(foundDashboard,runningversion);
			return output;
			
		}

		return null;
	}


	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindDashboardByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<Dashboard> foundDashboard = _dashboardRepository.findAll(search(search), pageable);
		List<Dashboard> dashboardList = foundDashboard.getContent();
		Iterator<Dashboard> dashboardIterator = dashboardList.iterator(); 
		List<FindDashboardByIdOutput> output = new ArrayList<>();

		while (dashboardIterator.hasNext()) {
			Dashboard dashboard = dashboardIterator.next();
			Dashboardversion dashboardVersion =_dashboardversionRepository.findById(new DashboardversionId(dashboard.getUsers().getId(),dashboard.getId(), "running")).orElse(null);
			FindDashboardByIdOutput dashboardOutput  = mapper.dashboardEntitiesToFindDashboardByIdOutput(dashboard, dashboardVersion); 
			Dashboardversion publishedversion = _dashboardversionRepository.findById(new DashboardversionId(dashboard.getUsers().getId(), dashboard.getId(), "published")).orElse(null);
			output.add(dashboardOutput); 

		}
		return output;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindDashboardByIdOutput addNewReportsToNewDashboard(AddNewReportToNewDashboardInput input)
	{
		CreateDashboardInput dashboardInput = mapper.addNewReportToNewDashboardInputTocreatDashboardInput(input);
		CreateDashboardOutput createdDashboard = create(dashboardInput);

		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();

		for(CreateReportInput report : input.getReportDetails())
		{
			report.setIsPublished(true);
			report.setOwnerId(createdDashboard.getOwnerId());
			CreateReportOutput createdReport = _reportAppService.create(report);
			if(report.getReportWidth() !=null) {
				createdReport.setReportWidth(report.getReportWidth());
			}
			else
				createdReport.setReportWidth("mediumchart");
			reportList.add(createdReport);
			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(createdReport);
			output.setReportWidth(report.getReportWidth());
			reportsOutput.add(output);
		}

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);
		_reportDashboardAppService.addReportsToDashboardPublishedversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);
		return dashboardOuputDto;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindDashboardByIdOutput addNewReportsToExistingDashboard(AddNewReportToExistingDashboardInput input)
	{
		Dashboardversion dashboardversion = _dashboardversionRepository.findById(new DashboardversionId(input.getOwnerId(), input.getId(),"running")).orElse(null);
		Dashboard dashboard = _dashboardRepository.findById(input.getId()).orElse(null);

		CreateDashboardOutput createdDashboard = mapper.dashboardAndDashboardversionToCreateDashboardOutput(dashboard, dashboardversion);

		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();

		for(CreateReportInput report : input.getReportDetails())
		{
			report.setIsPublished(true);
			report.setOwnerId(createdDashboard.getOwnerId());
			CreateReportOutput createdReport = _reportAppService.create(report);
			if(report.getReportWidth() !=null) {
				createdReport.setReportWidth(report.getReportWidth());
			}
			else
				createdReport.setReportWidth("mediumchart");

			reportList.add(createdReport);
			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(createdReport);
			output.setReportWidth(report.getReportWidth());
			reportsOutput.add(output);
		}

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);
		//	_reportDashboardAppService.addReportsToDashboardPublishedversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);
		
		if((dashboard.getUsers() !=null && dashboard.getUsers().getId() == input.getOwnerId())) {
			dashboard.setIsPublished(false);
			_dashboardRepository.save(dashboard);
		}
		
		return dashboardOuputDto;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindDashboardByIdOutput addExistingReportsToNewDashboard(AddExistingReportToNewDashboardInput input)
	{
		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();
		
		for(ExistingReportInput report : input.getReportDetails())
		{
			Report reportEntity = _reportRepository.findById(report.getId()).orElse(null);
			Reportversion reportversion = _reportversionRepository.findById(new ReportversionId(input.getOwnerId(),report.getId(),"published")).orElse(null);
			if(reportversion == null)
			{
				reportversion = _reportversionRepository.findById(new ReportversionId(input.getOwnerId(),report.getId(),"running")).orElse(null);
			}
			
			CreateReportOutput reportOutput = reportMapper.reportAndReportversionToCreateReportOutput(reportEntity, reportversion);
			if(report.getReportWidth() !=null) {
				reportOutput.setReportWidth(report.getReportWidth());
			}
			else
				reportOutput.setReportWidth("mediumchart");
			reportList.add(reportOutput);

			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(reportOutput);
			output.setReportWidth(reportOutput.getReportWidth());
			reportsOutput.add(output);
		}

		CreateDashboardInput dashboardInput = mapper.addExistingReportToNewDashboardInputTocreatDashboardInput(input);
		CreateDashboardOutput createdDashboard = create(dashboardInput);

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);
		_reportDashboardAppService.addReportsToDashboardPublishedversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);
		
		return dashboardOuputDto;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindDashboardByIdOutput addExistingReportsToExistingDashboard(AddExistingReportToExistingDashboardInput input)
	{		
		Dashboard dashboard = _dashboardRepository.findById(input.getId()).orElse(null);
		Dashboardversion dashboardversion = _dashboardversionRepository.findById(new DashboardversionId(input.getOwnerId(), dashboard.getId(),"running")).orElse(null);

		CreateDashboardOutput createdDashboard = mapper.dashboardAndDashboardversionToCreateDashboardOutput(dashboard, dashboardversion);
	
		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();
		for(ExistingReportInput report : input.getReportDetails())
		{
			Report reportEntity = _reportRepository.findById(report.getId()).orElse(null);
			Reportversion reportversion = _reportversionRepository.findById(new ReportversionId(input.getOwnerId(),report.getId(),"published")).orElse(null);
			if(reportversion == null)
			{
				reportversion = _reportversionRepository.findById(new ReportversionId(input.getOwnerId(),report.getId(),"running")).orElse(null);
			}
            
			CreateReportOutput reportOutput = reportMapper.reportAndReportversionToCreateReportOutput(reportEntity, reportversion);
			if(report.getReportWidth() !=null) {
				reportOutput.setReportWidth(report.getReportWidth());
			}
			else
				reportOutput.setReportWidth("mediumchart");
			reportList.add(reportOutput);

			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(reportOutput);
			output.setReportWidth(reportOutput.getReportWidth());
			reportsOutput.add(output);
		}

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);

		if((dashboard.getUsers() !=null && dashboard.getUsers().getId() == input.getOwnerId())) {
		
			dashboard.setIsPublished(false);
			_dashboardRepository.save(dashboard);
		}
		
		return dashboardOuputDto;
	}


	protected BooleanBuilder search(SearchCriteria search) throws Exception {

		QDashboard dashboard= QDashboard.dashboard;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(dashboard, map,search.getJoinColumns());
		}
		return null;
	}

	protected void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
					list.get(i).replace("%20","").trim().equals("userId") ||
					list.get(i).replace("%20","").trim().equals("description") ||
					list.get(i).replace("%20","").trim().equals("id") ||
					list.get(i).replace("%20","").trim().equals("reportdashboard") ||
					list.get(i).replace("%20","").trim().equals("title") ||
					list.get(i).replace("%20","").trim().equals("user")
					)) 
			{
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	protected BooleanBuilder searchKeyValuePair(QDashboard dashboard, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("isPublished")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(dashboard.isPublished.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(dashboard.isPublished.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
		}
		
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
			if(joinCol != null && joinCol.getKey().equals("ownerId")) {
				builder.and(dashboard.users.id.eq(Long.parseLong(joinCol.getValue())));
			}
		}
		return builder;
	}


	public Map<String,String> parseReportdashboardJoinColumn(String keysString) {

		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("dashboardId", keysString);
		return joinColumnMap;
	}


}



