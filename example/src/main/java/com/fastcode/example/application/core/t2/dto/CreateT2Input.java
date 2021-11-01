package com.fastcode.example.application.core.t2.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateT2Input {

  	@NotNull(message = "pic Should not be null")
  	private String pic;
  

}

