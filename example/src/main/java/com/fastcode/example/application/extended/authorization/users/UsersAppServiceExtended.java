package com.fastcode.example.application.extended.authorization.users;

import org.springframework.stereotype.Service;
import com.fastcode.example.application.core.authorization.users.UsersAppService;

import com.fastcode.example.domain.extended.authorization.users.IUsersRepositoryExtended;
import com.fastcode.example.domain.core.authorization.userspreference.IUserspreferenceRepository;
import com.fastcode.example.addons.reporting.domain.dashboardversion.*;
import com.fastcode.example.addons.reporting.domain.dashboardversionreport.*;
import com.fastcode.example.addons.reporting.domain.reportversion.*;
import com.fastcode.example.commons.logging.LoggingHelper;

@Service("usersAppServiceExtended")
public class UsersAppServiceExtended extends UsersAppService implements IUsersAppServiceExtended {

	public UsersAppServiceExtended(IDashboardversionRepository dashboardversionRepository,IReportversionRepository reportversionRepository,IDashboardversionreportRepository reportDashboardRepository,IUsersRepositoryExtended usersRepositoryExtended,
				IUserspreferenceRepository userspreferenceRepository,IUsersMapperExtended mapper,LoggingHelper logHelper) {

		super(dashboardversionRepository,reportversionRepository,reportDashboardRepository,usersRepositoryExtended,
		userspreferenceRepository,mapper,logHelper);

	}

 	//Add your custom code here
 
}

