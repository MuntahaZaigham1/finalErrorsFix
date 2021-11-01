package com.fastcode.example.addons.reporting.application.dashboardversion.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Getter @Setter
public class CreateDashboardversionInput {

	private String description;
	private String title;
  	private Long userId; 
	private Long dashboardId;

}

