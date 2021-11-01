package com.fastcode.example.addons.reporting.domain.report;

import com.fastcode.example.addons.reporting.domain.reportversion.Reportversion;
import com.fastcode.example.addons.reporting.domain.dashboardversionreport.Dashboardversionreport;
import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.fastcode.example.domain.core.authorization.users.Users;
import javax.persistence.*;
import java.time.*;

import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "report")
public class Report extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@EqualsAndHashCode.Include()
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
	private Long id;
  	
  	@Basic
	@Column(name = "is_published", nullable = false)
  	private Boolean isPublished;
  	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Users users;
  	
  	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL) 
  	private Set<Reportversion> reportversionSet = new HashSet<Reportversion>(); 
  	
  	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL) 
  	private Set<Dashboardversionreport> reportdashboardSet = new HashSet<Dashboardversionreport>(); 
  	
  	
  	public void addReportversion(Reportversion reportVersion) {
  		reportversionSet.add(reportVersion);
  		reportVersion.setReport(this);
	}
  	
  	public void removeReportversion(Reportversion reportVersion) {
        reportversionSet.remove(reportVersion);
        reportVersion.setReport(null);
    }

  	public void addDashboardversionreport(Dashboardversionreport dashboardversionreport) {
  		reportdashboardSet.add(dashboardversionreport);
  		dashboardversionreport.setReport(this);
	}

	public void removeDashboardversionreport(Dashboardversionreport dashboardversionreport) {
		reportdashboardSet.remove(dashboardversionreport);
		dashboardversionreport.setReport(null);
	}

}

  
      



