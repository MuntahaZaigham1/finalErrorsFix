package com.fastcode.example.addons.reporting.domain.dashboard;

import com.fastcode.example.addons.reporting.domain.dashboardversion.Dashboardversion;
import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.fastcode.example.domain.core.authorization.users.Users;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import java.time.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "dashboard")
public class Dashboard extends AbstractEntity {

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

	@OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL) 
	private Set<Dashboardversion> dashboardversionSet = new HashSet<Dashboardversion>(); 

	public void addDashboardversion(Dashboardversion dashboardversion) {
		dashboardversionSet.add(dashboardversion);
		dashboardversion.setDashboard(this);
	}
	
	public void removeDashboardversion(Dashboardversion dashboardversion) {
		dashboardversionSet.remove(dashboardversion);
		dashboardversion.setDashboard(null);
	}

}






