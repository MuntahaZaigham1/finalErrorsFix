package com.fastcode.example.application.core.t1;

import org.springframework.data.domain.Pageable;
import com.fastcode.example.application.core.t1.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;

import java.util.*;

public interface IT1AppService {
	
	//CRUD Operations
	CreateT1Output create(CreateT1Input t1);

    void delete(Long id);

    UpdateT1Output update(Long id, UpdateT1Input input);

    FindT1ByIdOutput findById(Long id);


    List<FindT1ByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    
    //Join Column Parsers
}

