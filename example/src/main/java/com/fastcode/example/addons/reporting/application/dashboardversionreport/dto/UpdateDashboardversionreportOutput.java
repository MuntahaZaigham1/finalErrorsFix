package com.fastcode.example.addons.reporting.application.dashboardversionreport.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Getter @Setter
public class UpdateDashboardversionreportOutput {

	private Long dashboardId;
	private Long reportId;
  	private Long userId; 
	private String dashboardVersion;
	private String reportWidth;
	private Long orderId;

}
