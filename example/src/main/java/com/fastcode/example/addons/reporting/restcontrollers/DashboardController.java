package com.fastcode.example.addons.reporting.restcontrollers;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.commons.search.OffsetBasedPageRequest;
import com.fastcode.example.addons.reporting.application.dashboard.IDashboardAppService;
import com.fastcode.example.addons.reporting.application.dashboard.dto.*;
import com.fastcode.example.addons.reporting.application.dashboardversionreport.IDashboardversionreportAppService;
import com.fastcode.example.addons.reporting.application.dashboardversionreport.dto.FindDashboardversionreportByIdOutput;
import com.fastcode.example.addons.reporting.application.report.IReportAppService;
import com.fastcode.example.addons.reporting.application.report.dto.FindReportByIdOutput;
import com.fastcode.example.application.extended.authorization.users.IUsersAppServiceExtended;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@SuppressWarnings({"unchecked", "rawtypes"})
@RequestMapping("/reporting/dashboard")
public class DashboardController {

	@Autowired
	@Qualifier("dashboardAppService")
	private IDashboardAppService _dashboardAppService;
	
	@Autowired
	@Qualifier("dashboardversionreportAppService")
	private IDashboardversionreportAppService  _reportdashboardAppService;
	
	@Autowired
	@Qualifier("reportAppService")
	private IReportAppService _reportAppService;

	@Autowired
	@Qualifier("usersAppServiceExtended")
	private IUsersAppServiceExtended _usersAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

	public DashboardController(IDashboardAppService dashboardAppService, IDashboardversionreportAppService reportdashboardAppService, IUsersAppServiceExtended usersAppService,
			LoggingHelper helper) {
		super();
		this._dashboardAppService = dashboardAppService;
		this._reportdashboardAppService = reportdashboardAppService;
		this._usersAppService = usersAppService;
		this.logHelper = helper;
	}

	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<CreateDashboardOutput> create(@RequestBody @Valid CreateDashboardInput dashboard) {
		Users users = _usersAppService.getUsers();
		
		dashboard.setOwnerId(users.getId()); 
		dashboard.setIsPublished(true);
		CreateDashboardOutput output=_dashboardAppService.create(dashboard);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete dashboard ------------
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = {"application/json"})
	public void delete(@PathVariable String id) {
		
		Users users = _usersAppService.getUsers();
		FindDashboardByIdOutput output = _dashboardAppService.findById(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a dashboard with a id=%s", id)));
 		
		if(output.getOwnerId() != users.getId()) {
			logHelper.getLogger().error("You do not have access to update a dashboard with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to update a dashboard with a id=%s", id));
		}

        _dashboardAppService.delete(Long.valueOf(id), users.getId());
	}
	
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}/report/{reportId}", method = RequestMethod.DELETE, consumes = {"application/json"})
	public void deleteReportFromDashboard(@PathVariable String id,@PathVariable String reportId) {
		
		Users users = _usersAppService.getUsers();

		FindDashboardByIdOutput currentDashboard = _dashboardAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentDashboard).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a dashboard with a id=%s", id)));
		
		if(currentDashboard.getOwnerId() != users.getId()) {
			logHelper.getLogger().error("You do not have access to update a dashboard with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to update a dashboard with a id=%s", id));
		}
		
		_dashboardAppService.deleteReportFromDashboard(Long.valueOf(id), Long.valueOf(reportId), users.getId());
	}

	// ------------ Update dashboard ------------
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<UpdateDashboardOutput> update(@PathVariable String id,@RequestBody @Valid UpdateDashboardInput dashboard) {
		
		Users users = _usersAppService.getUsers();
		
		FindDashboardByIdOutput currentDashboard = _dashboardAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentDashboard).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Dashboard with id=%s not found.", id)));
		
        
		if(currentDashboard.getOwnerId() != users.getId()) {
			logHelper.getLogger().error("You do not have access to update a dashboard with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to update a dashboard with a id=%s", id));
		}
	
		dashboard.setUserId(users.getId());
		//dashboard.setVersiono(currentDashboard.getVersiono());
		
		UpdateDashboardOutput output =_dashboardAppService.update(Long.valueOf(id),dashboard);
		output.setReportDetails(_dashboardAppService.setReportsList(Long.valueOf(id), users.getId(), "running"));
		return new ResponseEntity(output, HttpStatus.OK);
		
	}

	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> findById(@PathVariable String id) {
		
		Users users = _usersAppService.getUsers();
		FindDashboardByIdOutput output = _dashboardAppService.findByDashboardIdAndUsersId(Long.valueOf(id), users.getId(), "running");
		
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Dashboard with id=%s not found.", id)));
		output.setReportDetails(_dashboardAppService.setReportsList(Long.valueOf(id),users.getId(), "running"));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_READ')")
	@RequestMapping(value = "/{id}/getPublishedVersion", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> getPublishedVersion(@PathVariable String id) {
		
		Users users = _usersAppService.getUsers();
		FindDashboardByIdOutput output = _dashboardAppService.findByDashboardIdAndUsersId(Long.valueOf(id), users.getId(), "published");
		
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Dashboard with id=%s not found.", id)));
		output.setReportDetails(_dashboardAppService.setReportsList(Long.valueOf(id),users.getId(), "published"));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_CREATE')")
	@RequestMapping(value = "/addNewReportToNewDashboard", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> addNewReportsToNewDashboard(@RequestBody AddNewReportToNewDashboardInput input) {
		
		Users users = _usersAppService.getUsers();

		input.setOwnerId(users.getId());
		input.setIsPublished(true);
		
		FindDashboardByIdOutput output  = _dashboardAppService.addNewReportsToNewDashboard(input);
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_UPDATE')")
	@RequestMapping(value = "/addNewReportToExistingDashboard", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> addNewReportsToExistingDashboard(@RequestBody AddNewReportToExistingDashboardInput input) {
		
		FindDashboardByIdOutput dashboard = _dashboardAppService.findById(input.getId());
    	Optional.ofNullable(dashboard).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a dashboard with a id=%s", input.getId())));
		
		Users users = _usersAppService.getUsers();
		
		if(dashboard.getOwnerId() != users.getId()) {
			logHelper.getLogger().error("You do not have access to add report to dashboard with id=%s", input.getId());
			throw new EntityNotFoundException(
					String.format("You do not have access to add report to dashboard dashboard with id=%s", input.getId()));
		}
		
		input.setOwnerId(users.getId());
    	FindDashboardByIdOutput output = _dashboardAppService.addNewReportsToExistingDashboard(input);
    
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_CREATE')")
	@RequestMapping(value = "/addExistingReportToNewDashboard", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> addExistingReportsToNewDashboard(@RequestBody AddExistingReportToNewDashboardInput input) {
		
		for(ExistingReportInput reportInput : input.getReportDetails())
		{
			FindReportByIdOutput report = _reportAppService.findById(reportInput.getId());
			Optional.ofNullable(report).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a report with a id=%s", reportInput.getId())));
		}
		
		Users users = _usersAppService.getUsers();
		
		input.setOwnerId(users.getId());
		input.setIsPublished(true);
		FindDashboardByIdOutput output  = _dashboardAppService.addExistingReportsToNewDashboard(input);
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_UPDATE')")
	@RequestMapping(value = "/addExistingReportToExistingDashboard", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> addExistingReportsToExistingDashboard(@RequestBody AddExistingReportToExistingDashboardInput input) {
		
		FindDashboardByIdOutput dashboard = _dashboardAppService.findById(input.getId());
    	Optional.ofNullable(dashboard).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a dashboard with a id=%s", input.getId())));
		
		Users users = _usersAppService.getUsers();
		
		if(dashboard.getOwnerId() != users.getId()) {
			logHelper.getLogger().error("You do not have access to add report to dashboard with id=%s", input.getId());
			throw new EntityNotFoundException(
					String.format("You do not have access to add report to dashboard dashboard with id=%s", input.getId()));
		}
		
		dashboard.setReportDetails(_dashboardAppService.setReportsList(input.getId(), users.getId(), "running"));
		
        for(FindReportByIdOutput reportInput :dashboard.getReportDetails())
        {
        	 if(input.getReportDetails().stream().filter(o -> o.getId().equals(reportInput.getId())).findFirst().isPresent()) {
            	logHelper.getLogger().error("Report already exist in dashboard with a id=%s", input.getId());
    			throw new EntityExistsException(
    					String.format("Report already exist in dashboard with a id=%s", input.getId()));
            }
        
        }
        
        for(ExistingReportInput reportInput : input.getReportDetails())
		{
			FindReportByIdOutput report = _reportAppService.findById(reportInput.getId());
			if(report == null) {
				logHelper.getLogger().error("There does not exist a report with a id=%s", reportInput.getId());
				throw new EntityNotFoundException(
						String.format("There does not exist a report with a id=%s", reportInput.getId()));
			}
		}
		
		input.setOwnerId(users.getId());
		FindDashboardByIdOutput output  = _dashboardAppService.addExistingReportsToExistingDashboard(input);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_READ')")
	@RequestMapping(value = "/{id}/reportdashboard", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity getReportdashboard(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		Map<String,String> joinColDetails=_dashboardAppService.parseReportdashboardJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
		searchCriteria.setJoinColumns(joinColDetails);

		List<FindDashboardversionreportByIdOutput> output = _reportdashboardAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   
	
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<List<DashboardDetailsOutput>> getDashboard(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		Users users = _usersAppService.getUsers();
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		List<DashboardDetailsOutput> output = _dashboardAppService.getDashboards(users.getId(), search,pageable);

		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_READ')")
	@RequestMapping(value = "/available/{reportId}" ,method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<List<DashboardDetailsOutput>> getAvailableDashboard(@PathVariable String reportId, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		
		Users users = _usersAppService.getUsers();
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		List<DashboardDetailsOutput> output = _dashboardAppService.getAvailableDashboards(users.getId(),Long.valueOf(reportId), search,pageable);

		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/publish", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> publishDashboard(@PathVariable String id) {
		
		Users users = _usersAppService.getUsers();
		
		FindDashboardByIdOutput currentDashboard = _dashboardAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentDashboard).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to publish. Dashboard with id=%s not found.", id)));
		
		if(!users.getId().equals(currentDashboard.getOwnerId())) {
			logHelper.getLogger().error("You do not have access to publish a dashboard with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to publish a dashboard with a id=%s", id));
		}

		if(currentDashboard.getIsPublished())
		{
			logHelper.getLogger().error("Dashboard is already published with a id=%s", id);
			throw new EntityExistsException(
					String.format("Dashboard is already published with a id=%s", id));

		}

		FindDashboardByIdOutput output = _dashboardAppService.publishDashboard(users.getId(), Long.valueOf(id));
		output.setReportDetails(_dashboardAppService.setReportsList(output.getId(),users.getId(), "running"));

		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DASHBOARDENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/refresh", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindDashboardByIdOutput> refreshDashboard(@PathVariable String id) {
		Users users = _usersAppService.getUsers();
	
		FindDashboardByIdOutput dashboard = _dashboardAppService.findById(Long.valueOf(id));
		Optional.ofNullable(dashboard).orElseThrow(() -> new EntityNotFoundException(String.format("Dashboard not found with id=%s", id)));
		
		if(!users.getId().equals(dashboard.getOwnerId())) {
			logHelper.getLogger().error("You do not have access to refresh dashboard with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to refresh dashboard with a id=%s", id));
		}
		
		FindDashboardByIdOutput output = _dashboardAppService.refreshDashboard(users.getId(), Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No updates available. Dashboard can not be refreshed")));
		
		output.setReportDetails(_dashboardAppService.setReportsList(output.getId(),users.getId(), "running"));
		return new ResponseEntity(output, HttpStatus.OK);
	}

}


