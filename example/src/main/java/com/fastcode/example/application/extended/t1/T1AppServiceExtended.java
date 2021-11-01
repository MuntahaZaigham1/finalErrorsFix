package com.fastcode.example.application.extended.t1;

import org.springframework.stereotype.Service;
import com.fastcode.example.application.core.t1.T1AppService;

import com.fastcode.example.domain.extended.t1.IT1RepositoryExtended;
import com.fastcode.example.commons.logging.LoggingHelper;

@Service("t1AppServiceExtended")
public class T1AppServiceExtended extends T1AppService implements IT1AppServiceExtended {

	public T1AppServiceExtended(IT1RepositoryExtended t1RepositoryExtended,
				IT1MapperExtended mapper,LoggingHelper logHelper) {

		super(t1RepositoryExtended,
		mapper,logHelper);

	}

 	//Add your custom code here
 
}

