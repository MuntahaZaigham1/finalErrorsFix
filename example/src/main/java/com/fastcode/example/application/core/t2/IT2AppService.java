package com.fastcode.example.application.core.t2;

import com.fastcode.example.domain.core.t2.T2Id;
import org.springframework.data.domain.Pageable;
import com.fastcode.example.application.core.t2.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;

import java.util.*;

public interface IT2AppService {
	
	//CRUD Operations
	CreateT2Output create(CreateT2Input t2);

    void delete(T2Id t2Id);

    UpdateT2Output update(T2Id t2Id, UpdateT2Input input);

    FindT2ByIdOutput findById(T2Id t2Id);


    List<FindT2ByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    
    //Join Column Parsers
    
	T2Id parseT2Key(String keysString);
}

