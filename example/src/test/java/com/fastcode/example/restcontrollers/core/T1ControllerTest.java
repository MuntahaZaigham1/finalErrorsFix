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
import com.fastcode.example.application.core.t1.T1AppService;
import com.fastcode.example.application.core.t1.dto.*;
import com.fastcode.example.domain.core.t1.IT1Repository;
import com.fastcode.example.domain.core.t1.T1;

import com.fastcode.example.DatabaseContainerConfig;
import com.fastcode.example.domain.core.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class T1ControllerTest extends DatabaseContainerConfig{
	
	@Autowired
	protected SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired
	@Qualifier("t1Repository") 
	protected IT1Repository t1_repository;
	

	@SpyBean
	@Qualifier("t1AppService")
	protected T1AppService t1AppService;
	
	@SpyBean
	protected LoggingHelper logHelper;

	@SpyBean
	protected Environment env;

	@Mock
	protected Logger loggerMock;

	protected T1 t1;

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
		em.createNativeQuery("truncate table s1.t1 CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
	

	public T1 createEntity() {
	
		T1 t1Entity = new T1();
		t1Entity.setId(1L);
		t1Entity.setVersiono(0L);
		
		return t1Entity;
	}
    public CreateT1Input createT1Input() {
	
	    CreateT1Input t1Input = new CreateT1Input();
		
		return t1Input;
	}

	public T1 createNewEntity() {
		T1 t1 = new T1();
		t1.setId(3L);
		
		return t1;
	}
	
	public T1 createUpdateEntity() {
		T1 t1 = new T1();
		t1.setId(4L);
		
		return t1;
	}
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
    
		final T1Controller t1Controller = new T1Controller(t1AppService,
	logHelper,env);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(t1Controller)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		t1= createEntity();
		List<T1> list= t1_repository.findAll();
		if(!list.contains(t1)) {
			t1=t1_repository.save(t1);
		}

	}
	
	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/t1/" + t1.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() {

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/t1/999")
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}
	@Test
	public void CreateT1_T1DoesNotExist_ReturnStatusOk() throws Exception {
		CreateT1Input t1Input = createT1Input();	
		
		
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
	
		String json = ow.writeValueAsString(t1Input);

		mvc.perform(post("/t1").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());
	}     

	@Test
	public void DeleteT1_IdIsNotValid_ThrowEntityNotFoundException() {

        doReturn(null).when(t1AppService).findById(999L);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/t1/999")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a t1 with a id=999"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 	T1 entity =  createNewEntity();
	 	entity.setVersiono(0L);
		entity = t1_repository.save(entity);
		

		FindT1ByIdOutput output= new FindT1ByIdOutput();
		output.setId(entity.getId());
		
         Mockito.doReturn(output).when(t1AppService).findById(entity.getId());
       
    //    Mockito.when(t1AppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/t1/" + entity.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateT1_T1DoesNotExist_ReturnStatusNotFound() throws Exception {
   
        doReturn(null).when(t1AppService).findById(999L);
        
        UpdateT1Input t1 = new UpdateT1Input();
		t1.setId(999L);

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(t1);

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(
		 	put("/t1/999")
		 	.contentType(MediaType.APPLICATION_JSON)
		 	.content(json))
			.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. T1 with id=999 not found."));
	}    

	@Test
	public void UpdateT1_T1Exists_ReturnStatusOk() throws Exception {
		T1 entity =  createUpdateEntity();
		entity.setVersiono(0L);
		
		entity = t1_repository.save(entity);
		FindT1ByIdOutput output= new FindT1ByIdOutput();
		output.setId(entity.getId());
		output.setVersiono(entity.getVersiono());
		
        Mockito.when(t1AppService.findById(entity.getId())).thenReturn(output);
        
        
		
		UpdateT1Input t1Input = new UpdateT1Input();
		t1Input.setId(entity.getId());
		

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(t1Input);
	
		mvc.perform(put("/t1/" + entity.getId()+"/").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		T1 de = createUpdateEntity();
		de.setId(entity.getId());
		t1_repository.delete(de);
		

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/t1?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/t1?search=t1id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property t1id not found!"));

	} 
		
    
}

