package com.fastcode.example.application.core.t2;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
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

import com.fastcode.example.domain.core.t2.*;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.application.core.t2.dto.*;
import com.fastcode.example.domain.core.t2.QT2;
import com.fastcode.example.domain.core.t2.T2;
import com.fastcode.example.domain.core.t2.T2Id;

import com.fastcode.example.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.time.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class T2AppServiceTest {

	@InjectMocks
	@Spy
	protected T2AppService _appService;
	@Mock
	protected IT2Repository _t2Repository;
	
	
	@Mock
	protected IT2Mapper _mapper;

	@Mock
	protected Logger loggerMock;

	@Mock
	protected LoggingHelper logHelper;
	
    @Mock
    protected T2Id t2Id;
    
    private static final Long ID = 15L;
	 
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}
	
	@Test
	public void findT2ById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
		Optional<T2> nullOptional = Optional.ofNullable(null);
		Mockito.when(_t2Repository.findById(any(T2Id.class))).thenReturn(nullOptional);
		Assertions.assertThat(_appService.findById(t2Id)).isEqualTo(null);
	}
	
	@Test
	public void findT2ById_IdIsNotNullAndIdExists_ReturnT2() {

		T2 t2 = mock(T2.class);
		Optional<T2> t2Optional = Optional.of((T2) t2);
		Mockito.when(_t2Repository.findById(any(T2Id.class))).thenReturn(t2Optional);
		
	    Assertions.assertThat(_appService.findById(t2Id)).isEqualTo(_mapper.t2ToFindT2ByIdOutput(t2));
	}
	
	
	@Test 
    public void createT2_T2IsNotNullAndT2DoesNotExist_StoreT2() { 
 
        T2 t2Entity = mock(T2.class); 
    	CreateT2Input t2Input = new CreateT2Input();
		
        Mockito.when(_mapper.createT2InputToT2(any(CreateT2Input.class))).thenReturn(t2Entity); 
        Mockito.when(_t2Repository.save(any(T2.class))).thenReturn(t2Entity);

	   	Assertions.assertThat(_appService.create(t2Input)).isEqualTo(_mapper.t2ToCreateT2Output(t2Entity));

    } 
	@Test
	public void updateT2_T2IdIsNotNullAndIdExists_ReturnUpdatedT2() {

		T2 t2Entity = mock(T2.class);
		UpdateT2Input t2= mock(UpdateT2Input.class);
		
		Optional<T2> t2Optional = Optional.of((T2) t2Entity);
		Mockito.when(_t2Repository.findById(any(T2Id.class))).thenReturn(t2Optional);
	 		
		Mockito.when(_mapper.updateT2InputToT2(any(UpdateT2Input.class))).thenReturn(t2Entity);
		Mockito.when(_t2Repository.save(any(T2.class))).thenReturn(t2Entity);
		Assertions.assertThat(_appService.update(t2Id,t2)).isEqualTo(_mapper.t2ToUpdateT2Output(t2Entity));
	}
    
	@Test
	public void deleteT2_T2IsNotNullAndT2Exists_T2Removed() {

		T2 t2 = mock(T2.class);
		Optional<T2> t2Optional = Optional.of((T2) t2);
		Mockito.when(_t2Repository.findById(any(T2Id.class))).thenReturn(t2Optional);
 		
		_appService.delete(t2Id); 
		verify(_t2Repository).delete(t2);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<T2> list = new ArrayList<>();
		Page<T2> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindT2ByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_t2Repository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<T2> list = new ArrayList<>();
		T2 t2 = mock(T2.class);
		list.add(t2);
    	Page<T2> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindT2ByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();

		output.add(_mapper.t2ToFindT2ByIdOutput(t2));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_t2Repository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QT2 t2 = QT2.t2Entity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
		Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchKeyValuePair(t2,map,searchMap)).isEqualTo(builder);
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
		QT2 t2 = QT2.t2Entity;
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
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QT2.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}

  
	@Test
	public void ParseT2Key_KeysStringIsNotEmptyAndKeyValuePairExists_ReturnT2Id()
	{
		String keyString= "id=15,pic=15";
	
		T2Id t2Id = new T2Id();
        t2Id.setId(15L);
		t2Id.setPic(String.valueOf(ID));

		Assertions.assertThat(_appService.parseT2Key(keyString)).isEqualToComparingFieldByField(t2Id);
	}
	
	@Test
	public void ParseT2Key_KeysStringIsEmpty_ReturnNull()
	{
		String keyString= "";
		Assertions.assertThat(_appService.parseT2Key(keyString)).isEqualTo(null);
	}
	
	@Test
	public void ParseT2Key_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
		String keyString= "id";

		Assertions.assertThat(_appService.parseT2Key(keyString)).isEqualTo(null);
	}
}
