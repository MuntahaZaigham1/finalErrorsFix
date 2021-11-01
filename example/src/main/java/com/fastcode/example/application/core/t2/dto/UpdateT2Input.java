package com.fastcode.example.application.core.t2.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateT2Input {

  	@NotNull(message = "id Should not be null")
  	private Long id;
  	
  	@NotNull(message = "pic Should not be null")
  	private String pic;
  	
  	private Long versiono;
  
}

