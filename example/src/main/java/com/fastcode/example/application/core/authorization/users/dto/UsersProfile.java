package com.fastcode.example.application.core.authorization.users.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsersProfile {

	@NotNull(message = "firstname Should not be null")
	@Length(max = 255, message = "firstname must be less than 255 characters")
	private String firstname;
	
	@NotNull(message = "emailaddress Should not be null")
	@Length(max = 255, message = "emailaddress must be less than 255 characters")
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Emailaddress should be valid")
	private String emailaddress;
	
    @NotNull(message = "isemailconfirmed Should not be null")
  	private Boolean isemailconfirmed;
  	
  	private LocalDate joinDate;
  	
    @NotNull(message = "lastname Should not be null")
  	private String lastname;
  	
  	private String triggerGroup;
  	
  	private String triggerName;
  	
    @NotNull(message = "username Should not be null")
  	private String username;
  	
	
	private String theme;
    private String language;
	
}

