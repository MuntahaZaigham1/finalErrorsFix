package com.fastcode.example.restcontrollers.core;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
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
import javax.persistence.EntityExistsException;

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
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.application.core.authorization.users.UsersAppService;
import com.fastcode.example.application.core.authorization.users.dto.*;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.Users;

import com.fastcode.example.application.core.authorization.userspermission.UserspermissionAppService;    
import com.fastcode.example.application.core.authorization.usersrole.UsersroleAppService;    
import com.fastcode.example.security.JWTAppService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fastcode.example.application.core.authorization.usersrole.UsersroleAppService;
import com.fastcode.example.application.core.authorization.userspermission.UserspermissionAppService;
import com.fastcode.example.domain.core.authorization.userspreference.Userspreference;
import com.fastcode.example.domain.core.authorization.userspreference.IUserspreferenceManager;
import com.fastcode.example.DatabaseContainerConfig;
import com.fastcode.example.domain.core.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class UsersControllerTest extends DatabaseContainerConfig{
	
	@Autowired
	protected SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired
	@Qualifier("usersRepository") 
	protected IUsersRepository users_repository;
	

	@SpyBean
	@Qualifier("usersAppService")
	protected UsersAppService usersAppService;
	
    @SpyBean
    @Qualifier("userspermissionAppService")
	protected UserspermissionAppService  userspermissionAppService;
	
    @SpyBean
    @Qualifier("usersroleAppService")
	protected UsersroleAppService  usersroleAppService;
	
    @SpyBean
	protected IUserspreferenceManager userspreferenceManager;
	
	@SpyBean
	protected JWTAppService jwtAppService;
	
	@SpyBean
    protected PasswordEncoder pEncoder;
    
	@SpyBean
	protected LoggingHelper logHelper;

	@SpyBean
	protected Environment env;

	@Mock
	protected Logger loggerMock;

	protected Users users;

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
		em.createNativeQuery("truncate table s1.users CASCADE").executeUpdate();
		em.createNativeQuery("truncate table s1.userspreference CASCADE").executeUpdate();
		em.getTransaction().commit();
	}
	

	public Users createEntity() {
	
		Users usersEntity = new Users();
        usersEntity.setEmailaddress("bbc@d.c");
		usersEntity.setFirstname("1");
		usersEntity.setId(1L);
		usersEntity.setIsactive(false);
		usersEntity.setIsemailconfirmed(false);
    	usersEntity.setJoinDate(SearchUtils.stringToLocalDate("1996-09-01"));
		usersEntity.setLastname("1");
		usersEntity.setPassword("1");
		usersEntity.setTriggerGroup("1");
		usersEntity.setTriggerName("1");
		usersEntity.setUsername("1");
		usersEntity.setVersiono(0L);
		
		return usersEntity;
	}
    public CreateUsersInput createUsersInput() {
	
	    CreateUsersInput usersInput = new CreateUsersInput();
        usersInput.setEmailaddress("pmk@d.c");
  		usersInput.setFirstname("5");
		usersInput.setId(5L);
		usersInput.setIsactive(false);
		usersInput.setIsemailconfirmed(false);
    	usersInput.setJoinDate(SearchUtils.stringToLocalDate("1996-08-10"));
  		usersInput.setLastname("5");
  		usersInput.setPassword("5");
  		usersInput.setTriggerGroup("5");
  		usersInput.setTriggerName("5");
  		usersInput.setUsername("5");
		
		return usersInput;
	}

	public Users createNewEntity() {
		Users users = new Users();
        users.setEmailaddress("bmc@d.c");
		users.setFirstname("3");
		users.setId(3L);
		users.setIsactive(false);
		users.setIsemailconfirmed(false);
    	users.setJoinDate(SearchUtils.stringToLocalDate("1996-08-11"));
		users.setLastname("3");
		users.setPassword("3");
		users.setTriggerGroup("3");
		users.setTriggerName("3");
		users.setUsername("3");
		
		return users;
	}
	
	public Users createUpdateEntity() {
		Users users = new Users();
        users.setEmailaddress("pmlp@d.c");
		users.setFirstname("4");
		users.setId(4L);
		users.setIsactive(false);
		users.setIsemailconfirmed(false);
    	users.setJoinDate(SearchUtils.stringToLocalDate("1996-09-09"));
		users.setLastname("4");
		users.setPassword("4");
		users.setTriggerGroup("4");
		users.setTriggerName("4");
		users.setUsername("4");
		
		return users;
	}
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
    
		final UsersController usersController = new UsersController(usersAppService, userspermissionAppService, usersroleAppService,
	pEncoder,jwtAppService,logHelper,env);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(usersController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		users= createEntity();
		List<Users> list= users_repository.findAll();
		if(!list.contains(users)) {
			users=users_repository.save(users);
		}

	}
	
	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/users/" + users.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() {

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/users/999")
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}
	@Test
	public void CreateUsers_UsersDoesNotExist_ReturnStatusOk() throws Exception {
        Mockito.doReturn(null).when(usersAppService).findByFirstname(anyString());
	  
		CreateUsersInput users = createUsersInput();
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(users);
		
		
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isOk());
		 
		 users_repository.delete(createNewEntity());
		 
	}  
    
	@Test
	public void CreateUsers_UsersAlreadyExists_ThrowEntityExistsException() throws Exception {
	    FindUsersByFirstnameOutput output= new FindUsersByFirstnameOutput();
        output.setEmailaddress("bpc@g.c");
  		output.setFirstname("1");
		output.setId(1L);
		output.setIsactive(false);
		output.setIsemailconfirmed(false);
  		output.setLastname("1");
  		output.setUsername("1");

        Mockito.doReturn(output).when(usersAppService).findByFirstname(anyString());
	    CreateUsersInput users = createUsersInput();
	    ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
	
		String json = ow.writeValueAsString(users);
       
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(post("/users")
        		.contentType(MediaType.APPLICATION_JSON).content(json))
         .andExpect(status().isOk())).hasCause(new EntityExistsException("There already exists a users with Firstname =" + users.getFirstname()));
	} 
	
	
	
	
	
	

	@Test
	public void DeleteUsers_IdIsNotValid_ThrowEntityNotFoundException() {

        doReturn(null).when(usersAppService).findById(999L);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/users/999")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a users with a id=999"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 	Users entity =  createNewEntity();
	 	entity.setVersiono(0L);
		entity = users_repository.save(entity);
		
		Userspreference userspreference = new Userspreference();
		userspreference.setId(entity.getId());
		userspreference.setUsers(entity);
		userspreference.setTheme("Abc");
		userspreference.setLanguage("abc");
	 	userspreference=userspreferenceManager.create(userspreference);

		FindUsersByIdOutput output= new FindUsersByIdOutput();
		output.setEmailaddress(entity.getEmailaddress());
		output.setFirstname(entity.getFirstname());
		output.setId(entity.getId());
		output.setIsactive(entity.getIsactive());
		output.setIsemailconfirmed(entity.getIsemailconfirmed());
		output.setLastname(entity.getLastname());
		output.setUsername(entity.getUsername());
		
         Mockito.doReturn(output).when(usersAppService).findById(any(Long.class));
       
      //   Mockito.when(usersAppService.findById(any(Long.class))).thenReturn(output);
        
		mvc.perform(delete("/users/" + entity.getId()+"/")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateUsers_UsersDoesNotExist_ReturnStatusNotFound() throws Exception {
   
        doReturn(null).when(usersAppService).findById(999L);
        
        UpdateUsersInput users = new UpdateUsersInput();
        users.setEmailaddress("bmc@g.c");
  		users.setFirstname("999");
		users.setId(999L);
		users.setIsactive(false);
		users.setIsemailconfirmed(false);
		users.setJoinDate(SearchUtils.stringToLocalDate("1996-09-28"));
  		users.setLastname("999");
  		users.setTriggerGroup("999");
  		users.setTriggerName("999");
  		users.setUsername("999");

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(users);

		 org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(
		 	put("/users/999")
		 	.contentType(MediaType.APPLICATION_JSON)
		 	.content(json))
			.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. Users with id=999 not found."));
	}    

	@Test
	public void UpdateUsers_UsersExists_ReturnStatusOk() throws Exception {
		Users entity =  createUpdateEntity();
		entity.setVersiono(0L);
		
		entity = users_repository.save(entity);
		FindUsersWithAllFieldsByIdOutput output= new FindUsersWithAllFieldsByIdOutput();
		output.setEmailaddress(entity.getEmailaddress());
		output.setFirstname(entity.getFirstname());
		output.setId(entity.getId());
		output.setIsactive(entity.getIsactive());
		output.setIsemailconfirmed(entity.getIsemailconfirmed());
		output.setJoinDate(entity.getJoinDate());
		output.setLastname(entity.getLastname());
		output.setPassword(entity.getPassword());
		output.setTriggerGroup(entity.getTriggerGroup());
		output.setTriggerName(entity.getTriggerName());
		output.setUsername(entity.getUsername());
		output.setVersiono(entity.getVersiono());
		
		Mockito.when(usersAppService.findWithAllFieldsById(entity.getId())).thenReturn(output);
        
		
		UpdateUsersInput usersInput = new UpdateUsersInput();
		usersInput.setEmailaddress(entity.getEmailaddress());
		usersInput.setFirstname(entity.getFirstname());
		usersInput.setId(entity.getId());
		usersInput.setIsactive(entity.getIsactive());
		usersInput.setIsemailconfirmed(entity.getIsemailconfirmed());
		usersInput.setLastname(entity.getLastname());
		usersInput.setPassword(entity.getPassword());
		usersInput.setUsername(entity.getUsername());
		

		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(usersInput);
	
		mvc.perform(put("/users/" + entity.getId()+"/").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		Users de = createUpdateEntity();
		de.setId(entity.getId());
		users_repository.delete(de);
		

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/users?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/users?search=usersid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property usersid not found!"));

	} 
		
	
    @Test
	public void GetUserspermissions_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("id", "1");

		Mockito.when(usersAppService.parseUserspermissionsJoinColumn("usersid")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/users/1/userspermissions?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetUserspermissions_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("id", "1");
		
        Mockito.when(usersAppService.parseUserspermissionsJoinColumn("usersId")).thenReturn(joinCol);
		mvc.perform(get("/users/1/userspermissions?search=usersId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetUserspermissions_searchIsNotEmpty() {
	
		Mockito.when(usersAppService.parseUserspermissionsJoinColumn(anyString())).thenReturn(null);
	 		  		    		  
	    org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/users/1/userspermissions?search=usersId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid join column"));
	}    
	
    @Test
	public void GetUsersroles_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("id", "1");

		Mockito.when(usersAppService.parseUsersrolesJoinColumn("usersid")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/users/1/usersroles?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetUsersroles_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("id", "1");
		
        Mockito.when(usersAppService.parseUsersrolesJoinColumn("usersId")).thenReturn(joinCol);
		mvc.perform(get("/users/1/usersroles?search=usersId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetUsersroles_searchIsNotEmpty() {
	
		Mockito.when(usersAppService.parseUsersrolesJoinColumn(anyString())).thenReturn(null);
	 		  		    		  
	    org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(get("/users/1/usersroles?search=usersId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid join column"));
	}    
    
}

