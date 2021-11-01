package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.restcontrollers.core.T2ControllerTest;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class T2ControllerTestExtended extends T2ControllerTest {
	
	//Add your custom code here	
}
