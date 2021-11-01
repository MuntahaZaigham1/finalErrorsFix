package com.fastcode.example.restcontrollers.extended;

import org.springframework.web.bind.annotation.*;
import com.fastcode.example.restcontrollers.core.T1Controller;
import com.fastcode.example.application.extended.t1.IT1AppServiceExtended;
import org.springframework.core.env.Environment;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/t1/extended")
public class T1ControllerExtended extends T1Controller {

		public T1ControllerExtended(IT1AppServiceExtended t1AppServiceExtended,
	     LoggingHelper helper, Environment env) {
		super(
		t1AppServiceExtended,
		helper, env);
	}

	//Add your custom code here

}

