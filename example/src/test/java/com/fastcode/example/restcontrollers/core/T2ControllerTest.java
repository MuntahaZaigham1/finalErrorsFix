package com.fastcode.example.restcontrollers.core;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;
import java.time.*;
import java.math.BigDecimal;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.springframework.core.env.Environment;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.application.core.t2.T2AppService;
import com.fastcode.example.application.core.t2.dto.*;
import com.fastcode.example.domain.core.t2.IT2Repository;
import com.fastcode.example.domain.core.t2.T2;

import com.fastcode.example.domain.core.t2.T2Id;
import com.fastcode.example.DatabaseContainerConfig;
import com.fastcode.example.domain.core.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class T2ControllerTest extends DatabaseContainerConfig{
	
	@Autowired
	protected SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired
	@Qualifier("t2Repository") 
	protected IT2Repository t2_repository;
	

	@SpyBean
	@Qualifier("t2AppService")
	protected T2AppService t2AppService;
	
	@SpyBean
	protected LoggingHelper logHelper;

	@SpyBean
	protected Environment env;

	@Mock
	protected Logger loggerMock;

	protected T2 t2;

	protected MockMvc mvc;
	
	@Autowired
	EntityManagerFactory emf;
	
    static EntityManagerFactory emfs;
    
    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
	private BigDecimal bigdec = new BigDecimal(1.2);
    
	@PostConstruct
	public void init() {
	emfs = emf;
	}

	@AfterClass
	public static void cleanup() {
		EntityManager em = emfs.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("truncate table s1.t2 CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
	

	public T2 createEntity() {
	
		T2 t2Entity = new T2();
		t2Entity.setId(1L);
		t2Entity.setPic("1");
		t2Entity.setVersiono(0L);
		
		return t2Entity;
	}
    public CreateT2Input createT2Input() {
	
	    CreateT2Input t2Input = new CreateT2Input();
  		t2Input.setPic("5");
		
		return t2Input;
	}

	public T2 createNewEntity() {
		T2 t2 = new T2();
		t2.setId(3L);
		t2.setPic("3");
		
		return t2;
	}
	
	public T2 createUpdateEntity() {
		T2 t2 = new T2();
		t2.setId(4L);
		t2.setPic("4");
		
		return t2;
	}
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
    
		final T2Controller t2Controller = new T2Controller(t2AppService,
	logHelper,env);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(t2Controller)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		t2= createEntity();
		List<T2> list= t2_repository.findAll();
		if(!list.contains(t2)) {
			t2=t2_repository.save(t2);
		}

	}
	
	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/t2/id=" + t2.getId()+ ",pic=" + t2.getPic()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() {

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/t2/id=999,pic=999")
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}
	@Test
	public void CreateT2_T2DoesNotExist_ReturnStatusOk() throws Exception {
		CreateT2Input t2Input = createT2Input();	
		
		
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
	
		String json = ow.writeValueAsString(t2Input);

		mvc.perform(post("/t2").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());
	}     

	@Test
	public void DeleteT2_IdIsNotValid_ThrowEntityNotFoundException() {

        doReturn(null).when(t2AppService).findById(new T2Id(999L, "999"));
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/t2/id=999,pic=999")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a t2 with a id=id=999,pic=999"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 	T2 entity =  createNewEntity();
	 	entity.setVersiono(0L);
		entity = t2_repository.save(entity);
		

		FindT2ByIdOutput output= new FindT2ByIdOutput();
		output.setId(entity.getId());
		output.setPic(entity.getPic());
		
	//    Mockito.when(t2AppService.findById(new T2Id(entity.getId(), entity.getPic()))).thenReturn(output);
        Mockito.doReturn(output).when(t2AppService).findById(new T2Id(entity.getId(), entity.getPic()));
        
		mvc.perform(delete("/t2/id="+ entity.getId()+ ",pic="+ entity.getPic()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateT2_T2DoesNotExist_ReturnStatusNotFound() throws Exception {
   
        doReturn(null).when(t2AppService).findById(new T2Id(999L, "999"));
        
        UpdateT2Input t2 = new UpdateT2Input();
		t2.setId(999L);
  		t2.setPic("999");

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(t2);

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(
		 	put("/t2/id=999,pic=999")
		 	.contentType(MediaType.APPLICATION_JSON)
		 	.content(json))
			.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. T2 with id=id=999,pic=999 not found."));
	}    

	@Test
	public void UpdateT2_T2Exists_ReturnStatusOk() throws Exception {
		T2 entity =  createUpdateEntity();
		entity.setVersiono(0L);
		
		entity = t2_repository.save(entity);
		FindT2ByIdOutput output= new FindT2ByIdOutput();
		output.setId(entity.getId());
		output.setPic(entity.getPic());
		output.setVersiono(entity.getVersiono());
		
	    Mockito.when(t2AppService.findById(new T2Id(entity.getId(), entity.getPic()))).thenReturn(output);
        
        
		
		UpdateT2Input t2Input = new UpdateT2Input();
		t2Input.setId(entity.getId());
		t2Input.setPic(entity.getPic());
		

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(t2Input);
	
		mvc.perform(put("/t2/id=" + entity.getId()+ ",pic=" + entity.getPic()+"/").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		T2 de = createUpdateEntity();
		de.setId(entity.getId());
		de.setPic(entity.getPic());
		t2_repository.delete(de);
		

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/t2?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/t2?search=t2id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property t2id not found!"));

	} 
		
    
}

