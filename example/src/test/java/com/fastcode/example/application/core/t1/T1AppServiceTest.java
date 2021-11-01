package com.fastcode.example.application.core.t1;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.example.domain.core.t1.*;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.application.core.t1.dto.*;
import com.fastcode.example.domain.core.t1.QT1;
import com.fastcode.example.domain.core.t1.T1;

import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.time.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class T1AppServiceTest {

	@InjectMocks
	@Spy
	protected T1AppService _appService;
	@Mock
	protected IT1Repository _t1Repository;
	
	
	@Mock
	protected IT1Mapper _mapper;

	@Mock
	protected Logger loggerMock;

	@Mock
	protected LoggingHelper logHelper;
	
    protected static Long ID=15L;
	 
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}
	
	@Test
	public void findT1ById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
		Optional<T1> nullOptional = Optional.ofNullable(null);
		Mockito.when(_t1Repository.findById(anyLong())).thenReturn(nullOptional);
		Assertions.assertThat(_appService.findById(ID )).isEqualTo(null);
	}
	
	@Test
	public void findT1ById_IdIsNotNullAndIdExists_ReturnT1() {

		T1 t1 = mock(T1.class);
		Optional<T1> t1Optional = Optional.of((T1) t1);
		Mockito.when(_t1Repository.findById(anyLong())).thenReturn(t1Optional);
		
	    Assertions.assertThat(_appService.findById(ID )).isEqualTo(_mapper.t1ToFindT1ByIdOutput(t1));
	}
	
	
	@Test 
    public void createT1_T1IsNotNullAndT1DoesNotExist_StoreT1() { 
 
        T1 t1Entity = mock(T1.class); 
    	CreateT1Input t1Input = new CreateT1Input();
		
        Mockito.when(_mapper.createT1InputToT1(any(CreateT1Input.class))).thenReturn(t1Entity); 
        Mockito.when(_t1Repository.save(any(T1.class))).thenReturn(t1Entity);

	   	Assertions.assertThat(_appService.create(t1Input)).isEqualTo(_mapper.t1ToCreateT1Output(t1Entity));

    } 
	@Test
	public void updateT1_T1IdIsNotNullAndIdExists_ReturnUpdatedT1() {

		T1 t1Entity = mock(T1.class);
		UpdateT1Input t1= mock(UpdateT1Input.class);
		
		Optional<T1> t1Optional = Optional.of((T1) t1Entity);
		Mockito.when(_t1Repository.findById(anyLong())).thenReturn(t1Optional);
	 		
		Mockito.when(_mapper.updateT1InputToT1(any(UpdateT1Input.class))).thenReturn(t1Entity);
		Mockito.when(_t1Repository.save(any(T1.class))).thenReturn(t1Entity);
		Assertions.assertThat(_appService.update(ID,t1)).isEqualTo(_mapper.t1ToUpdateT1Output(t1Entity));
	}
    
	@Test
	public void deleteT1_T1IsNotNullAndT1Exists_T1Removed() {

		T1 t1 = mock(T1.class);
		Optional<T1> t1Optional = Optional.of((T1) t1);
		Mockito.when(_t1Repository.findById(anyLong())).thenReturn(t1Optional);
 		
		_appService.delete(ID); 
		verify(_t1Repository).delete(t1);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<T1> list = new ArrayList<>();
		Page<T1> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindT1ByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_t1Repository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<T1> list = new ArrayList<>();
		T1 t1 = mock(T1.class);
		list.add(t1);
    	Page<T1> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindT1ByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();

		output.add(_mapper.t1ToFindT1ByIdOutput(t1));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_t1Repository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QT1 t1 = QT1.t1Entity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
		Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchKeyValuePair(t1,map,searchMap)).isEqualTo(builder);
	}
	
	@Test (expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("xyz");
		_appService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
		List<String> list = new ArrayList<>();
		_appService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QT1 t1 = QT1.t1Entity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QT1.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}

}
