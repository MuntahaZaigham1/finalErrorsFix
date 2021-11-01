package com.fastcode.example.application.extended.authorization.permission;

import org.springframework.stereotype.Service;
import com.fastcode.example.application.core.authorization.permission.PermissionAppService;

import com.fastcode.example.domain.extended.authorization.permission.IPermissionRepositoryExtended;
import com.fastcode.example.commons.logging.LoggingHelper;

@Service("permissionAppServiceExtended")
public class PermissionAppServiceExtended extends PermissionAppService implements IPermissionAppServiceExtended {

	public PermissionAppServiceExtended(IPermissionRepositoryExtended permissionRepositoryExtended,
				IPermissionMapperExtended mapper,LoggingHelper logHelper) {

		super(permissionRepositoryExtended,
		mapper,logHelper);

	}

 	//Add your custom code here
 
}

