package com.fastcode.example.application.core.t1.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateT1Input {

  	@NotNull(message = "id Should not be null")
  	private Long id;
  	
  	private Long versiono;
  
}

