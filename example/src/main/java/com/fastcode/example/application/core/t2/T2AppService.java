package com.fastcode.example.application.core.t2;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.fastcode.example.application.core.t2.dto.*;
import com.fastcode.example.domain.core.t2.IT2Repository;
import com.fastcode.example.domain.core.t2.QT2;
import com.fastcode.example.domain.core.t2.T2;
import com.fastcode.example.domain.core.t2.T2Id;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import java.time.*;
import java.util.*;

@Service("t2AppService")
@RequiredArgsConstructor
public class T2AppService implements IT2AppService {
	@Qualifier("t2Repository")
	@NonNull protected final IT2Repository _t2Repository;

	
	@Qualifier("IT2MapperImpl")
	@NonNull protected final IT2Mapper mapper;

	@NonNull protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateT2Output create(CreateT2Input input) {

		T2 t2 = mapper.createT2InputToT2(input);

		T2 createdT2 = _t2Repository.save(t2);
		return mapper.t2ToCreateT2Output(createdT2);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateT2Output update(T2Id t2Id, UpdateT2Input input) {

		T2 existing = _t2Repository.findById(t2Id).get();

		T2 t2 = mapper.updateT2InputToT2(input);
		
		T2 updatedT2 = _t2Repository.save(t2);
		return mapper.t2ToUpdateT2Output(updatedT2);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(T2Id t2Id) {

		T2 existing = _t2Repository.findById(t2Id).orElse(null); 
	 	
	 	_t2Repository.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindT2ByIdOutput findById(T2Id t2Id) {

		T2 foundT2 = _t2Repository.findById(t2Id).orElse(null);
		if (foundT2 == null)  
			return null; 
 	   
 	    return mapper.t2ToFindT2ByIdOutput(foundT2);
	}

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindT2ByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<T2> foundT2 = _t2Repository.findAll(search(search), pageable);
		List<T2> t2List = foundT2.getContent();
		Iterator<T2> t2Iterator = t2List.iterator(); 
		List<FindT2ByIdOutput> output = new ArrayList<>();

		while (t2Iterator.hasNext()) {
		T2 t2 = t2Iterator.next();
 	    output.add(mapper.t2ToFindT2ByIdOutput(t2));
		}
		return output;
	}
	
	protected BooleanBuilder search(SearchCriteria search) throws Exception {

		QT2 t2= QT2.t2Entity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(t2, map,search.getJoinColumns());
		}
		return null;
	}
	
	protected void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("pic")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	protected BooleanBuilder searchKeyValuePair(QT2 t2, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			 if(details.getKey().replace("%20","").trim().equals("id")) {
			 	if(details.getValue().getOperator().equals("contains")) {
					builder.and(t2.id.like(details.getValue().getSearchValue() + "%"));
				} else if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(t2.id.eq(Long.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue())) {
					builder.and(t2.id.ne(Long.valueOf(details.getValue().getSearchValue())));
				} else if(details.getValue().getOperator().equals("range")) {
				  	if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	   builder.and(t2.id.between(Long.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   	} else if(StringUtils.isNumeric(details.getValue().getStartingValue())) {
                	  	builder.and(t2.id.goe(Long.valueOf(details.getValue().getStartingValue())));
                   	} else if(StringUtils.isNumeric(details.getValue().getEndingValue())) {
                	  	builder.and(t2.id.loe(Long.valueOf(details.getValue().getEndingValue())));
					}
				}
			}
	    
		}
		
		return builder;
	}
	
	public T2Id parseT2Key(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		T2Id t2Id = new T2Id();
		
		Map<String,String> keyMap = new HashMap<String,String>();
		if(keyEntries.length > 1) {
			for(String keyEntry: keyEntries)
			{
				String[] keyEntryArr = keyEntry.split("=");
				if(keyEntryArr.length > 1) {
					keyMap.put(keyEntryArr[0], keyEntryArr[1]);					
				}
				else {
					return null;
				}
			}
		}
		else {
			String[] keyEntryArr = keysString.split("=");
			if(keyEntryArr.length > 1) {
				keyMap.put(keyEntryArr[0], keyEntryArr[1]);					
			}
			else 
			return null;
		}
		
		t2Id.setId(Long.valueOf(keyMap.get("id")));
		t2Id.setPic(keyMap.get("pic"));
		return t2Id;
		
	}	
}



