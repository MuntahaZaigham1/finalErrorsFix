package com.fastcode.example.addons.reporting.application.dashboardversion.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Getter @Setter
public class CreateDashboardversionOutput {

    private String description;
    private Long id;
    private String title;
    private Boolean isRefreshed;
  	private Long userId; 
	private String userDescriptiveField;
	
}

