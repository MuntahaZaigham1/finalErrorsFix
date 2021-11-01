package com.fastcode.example.addons.reporting.application.dashboard.dto;

import java.time.*;
import java.util.List;
import com.fastcode.example.addons.reporting.application.report.dto.FindReportByIdOutput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindDashboardByIdOutput {

	private String description;
	private String title;
  	private Long userId; 
	private Long id;
	private Boolean isPublished;
	private Boolean isRefreshed;
  	private Long ownerId; 
	private String ownerDescriptiveField;
	private List<FindReportByIdOutput> reportDetails;
	private Long versiono;
}

