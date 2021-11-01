package com.fastcode.example.restcontrollers.extended;

import org.springframework.web.bind.annotation.*;
import com.fastcode.example.restcontrollers.core.UsersController;
import com.fastcode.example.application.extended.authorization.users.IUsersAppServiceExtended;
import com.fastcode.example.application.extended.authorization.userspermission.IUserspermissionAppServiceExtended;
import com.fastcode.example.application.extended.authorization.usersrole.IUsersroleAppServiceExtended;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fastcode.example.security.JWTAppService;
import org.springframework.core.env.Environment;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/users/extended")
public class UsersControllerExtended extends UsersController {

		public UsersControllerExtended(IUsersAppServiceExtended usersAppServiceExtended, IUserspermissionAppServiceExtended userspermissionAppServiceExtended, IUsersroleAppServiceExtended usersroleAppServiceExtended,
	    PasswordEncoder pEncoder,JWTAppService jwtAppService, LoggingHelper helper, Environment env) {
		super(
		usersAppServiceExtended,
    	userspermissionAppServiceExtended,
    	usersroleAppServiceExtended,
	    pEncoder,
	    jwtAppService,
		helper, env);
	}

	//Add your custom code here

}

