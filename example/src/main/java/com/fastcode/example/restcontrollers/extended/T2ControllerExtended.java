package com.fastcode.example.restcontrollers.extended;

import org.springframework.web.bind.annotation.*;
import com.fastcode.example.restcontrollers.core.T2Controller;
import com.fastcode.example.application.extended.t2.IT2AppServiceExtended;
import org.springframework.core.env.Environment;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/t2/extended")
public class T2ControllerExtended extends T2Controller {

		public T2ControllerExtended(IT2AppServiceExtended t2AppServiceExtended,
	     LoggingHelper helper, Environment env) {
		super(
		t2AppServiceExtended,
		helper, env);
	}

	//Add your custom code here

}

