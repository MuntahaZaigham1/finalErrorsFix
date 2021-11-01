package com.fastcode.example.application.extended.t2;

import org.springframework.stereotype.Service;
import com.fastcode.example.application.core.t2.T2AppService;

import com.fastcode.example.domain.extended.t2.IT2RepositoryExtended;
import com.fastcode.example.commons.logging.LoggingHelper;

@Service("t2AppServiceExtended")
public class T2AppServiceExtended extends T2AppService implements IT2AppServiceExtended {

	public T2AppServiceExtended(IT2RepositoryExtended t2RepositoryExtended,
				IT2MapperExtended mapper,LoggingHelper logHelper) {

		super(t2RepositoryExtended,
		mapper,logHelper);

	}

 	//Add your custom code here
 
}

