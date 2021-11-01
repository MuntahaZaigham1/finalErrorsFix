package com.fastcode.example.domain.core.authorization.users;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.time.*;
import com.fastcode.example.domain.core.authorization.tokenverification.Tokenverification;
import com.fastcode.example.domain.core.authorization.userspermission.Userspermission;
import com.fastcode.example.domain.core.authorization.userspreference.Userspreference;
import com.fastcode.example.domain.core.authorization.usersrole.Usersrole;
import com.fastcode.example.addons.reporting.domain.report.Report;
import com.fastcode.example.addons.reporting.domain.reportversion.Reportversion;
import com.fastcode.example.addons.reporting.domain.dashboard.Dashboard;
import com.fastcode.example.addons.reporting.domain.dashboardversion.Dashboardversion;
import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.querydsl.core.annotations.Config;
import org.hibernate.annotations.TypeDefs;


@Entity
@Config(defaultVariableName = "usersEntity")
@Table(name = "users")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({
}) 
public class Users extends AbstractEntity {

    @Basic
    @Column(name = "emailaddress", nullable = false,length =255)
    private String emailaddress;

    @Basic
    @Column(name = "firstname", nullable = false,length =255)
    private String firstname;

    @Id
    @EqualsAndHashCode.Include() 
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Basic
    @Column(name = "isactive", nullable = false)
    private Boolean isactive;
    
    @Basic
    @Column(name = "isemailconfirmed", nullable = false)
    private Boolean isemailconfirmed;
    
    @Basic
    @Column(name = "join_date", nullable = true)
    private LocalDate joinDate;

    @Basic
    @Column(name = "lastname", nullable = false,length =255)
    private String lastname;

    @Basic
    @Column(name = "password", nullable = false,length =255)
    private String password;

    @Basic
    @Column(name = "trigger_group", nullable = true,length =200)
    private String triggerGroup;

    @Basic
    @Column(name = "trigger_name", nullable = true,length =200)
    private String triggerName;

    @Basic
    @Column(name = "username", nullable = false,length =255)
    private String username;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Dashboard> dashboardsSet = new HashSet<Dashboard>();
    
    public void addDashboards(Dashboard dashboards) {        
    	dashboardsSet.add(dashboards);

        dashboards.setUsers(this);
    }
    public void removeDashboards(Dashboard dashboards) {
        dashboardsSet.remove(dashboards);
        
        dashboards.setUsers(null);

    }
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Dashboardversion> dashboardversionsSet = new HashSet<Dashboardversion>();
    
    public void addDashboardversions(Dashboardversion dashboardversions) {        
    	dashboardversionsSet.add(dashboardversions);

        dashboardversions.setUsers(this);
    }
    public void removeDashboardversions(Dashboardversion dashboardversions) {
        dashboardversionsSet.remove(dashboardversions);
        
        dashboardversions.setUsers(null);

    }
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Report> reportsSet = new HashSet<Report>();
    
    public void addReports(Report reports) {        
    	reportsSet.add(reports);

        reports.setUsers(this);
    }
    public void removeReports(Report reports) {
        reportsSet.remove(reports);
        
        reports.setUsers(null);

    }
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reportversion> reportversionsSet = new HashSet<Reportversion>();
    
    public void addReportversions(Reportversion reportversions) {        
    	reportversionsSet.add(reportversions);

        reportversions.setUsers(this);
    }
    public void removeReportversions(Reportversion reportversions) {
        reportversionsSet.remove(reportversions);
        
        reportversions.setUsers(null);

    }
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tokenverification> tokenverificationsSet = new HashSet<Tokenverification>();
    
    public void addTokenverifications(Tokenverification tokenverifications) {        
    	tokenverificationsSet.add(tokenverifications);

        tokenverifications.setUsers(this);
    }
    public void removeTokenverifications(Tokenverification tokenverifications) {
        tokenverificationsSet.remove(tokenverifications);
        
        tokenverifications.setUsers(null);

    }
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Userspermission> userspermissionsSet = new HashSet<Userspermission>();
    
    public void addUserspermissions(Userspermission userspermissions) {        
    	userspermissionsSet.add(userspermissions);

        userspermissions.setUsers(this);
    }
    public void removeUserspermissions(Userspermission userspermissions) {
        userspermissionsSet.remove(userspermissions);
        
        userspermissions.setUsers(null);

    }
    

    @OneToOne(mappedBy = "users", cascade=CascadeType.MERGE)
    private Userspreference userspreference;

    public void setUserspreference(Userspreference userspreference) {
    	if (userspreference == null) {
            if (this.userspreference != null) {
                this.userspreference.setUsers(null);
            }
        }
        else {
            userspreference.setUsers(this);
        }
        this.userspreference = userspreference;
    }
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Usersrole> usersrolesSet = new HashSet<Usersrole>();
    
    public void addUsersroles(Usersrole usersroles) {        
    	usersrolesSet.add(usersroles);

        usersroles.setUsers(this);
    }
    public void removeUsersroles(Usersrole usersroles) {
        usersrolesSet.remove(usersroles);
        
        usersroles.setUsers(null);

    }
    

}



