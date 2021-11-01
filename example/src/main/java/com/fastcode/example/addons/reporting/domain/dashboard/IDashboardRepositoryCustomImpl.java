package com.fastcode.example.addons.reporting.domain.dashboard;

import java.util.ArrayList;
import java.util.List;

import java.time.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fastcode.example.addons.reporting.application.dashboard.dto.DashboardDetailsOutput;

@Repository("dashboardRepositoryCustomImpl")
@SuppressWarnings({"unchecked"})
public class IDashboardRepositoryCustomImpl implements IDashboardRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired 
	private Environment env;

	@Override
	public Page<DashboardDetailsOutput> getAllDashboardsByUsersId(Long userId, String search, Pageable pageable) {
		String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString = String.format(""
				+ "SELECT d.id, rep.dashboard_version, rep.description, rep.title, d.is_published, rep.is_refreshed, "
   				+ " us.firstname,"
				+ "rep.user_id "
				+ " FROM ((%s.dashboard d "
				+ " RIGHT OUTER JOIN "
				+ "  (SELECT dv.* "
				+ "   FROM "
				+ "        %s.dashboardversion dv "
				+ "   WHERE "
				+ "   dv.user_id = :userId"
				+ "     AND (cast(:search as varchar) is null OR dv.title like cast(:search as varchar)) "
				+ "     AND dv.dashboard_version = 'running' ) AS rep ON rep.dashboard_id = d.id "
				+ " AND d.owner_id = rep.user_id"
				+ " ) JOIN  ( "
   				+ " SELECT id  ,firstname FROM %s.users where id = :userId ) AS us ON us.id = rep.user_id )"
		         ,schema,schema,schema);
		Query query = 
				entityManager.createNativeQuery(qlString)
				.setParameter("userId",userId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
		List<Object[]> results = query.getResultList();
		List<DashboardDetailsOutput> finalResults = new ArrayList<>();
		
		for(Object[] obj : results){
			DashboardDetailsOutput dashboardDetails = new DashboardDetailsOutput();

			// Here you manually obtain value from object and map to your pojo setters
			dashboardDetails.setId(obj[0] !=null ? Long.parseLong(obj[0].toString()) : null);
			dashboardDetails.setDashboardVersion(obj[1] !=null ? (obj[1].toString()) : null);
			dashboardDetails.setDescription(obj[2] !=null ? (obj[2].toString()) : null);
			dashboardDetails.setTitle(obj[3]!=null ? (obj[3].toString()) : null);
	
			dashboardDetails.setIsPublished(obj[4] != null && obj[4].toString().equals("true"));
            dashboardDetails.setIsRefreshed(obj[5] != null && obj[5].toString().equals("true"));
            dashboardDetails.setOwnerDescriptiveField(obj[6] != null ? (obj[6].toString()) : null);
            dashboardDetails.setUserId(obj[7] !=null ? Long.parseLong(obj[7].toString()) : null);
			finalResults.add(dashboardDetails);

		}
		int totalRows = results.size();

		Page<DashboardDetailsOutput> result = new PageImpl<DashboardDetailsOutput>(finalResults, pageable, totalRows);

		return result;
	}


	@Override
	public Page<DashboardDetailsOutput> getAvailableDashboardsByUsersId(Long userId,Long reportId, String search, Pageable pageable) {
			String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
		String qlString= ""
		       + "SELECT d.id, dv.dashboard_version, dv.description, dv.title , d.is_published, "
		       + "				dv.user_id "
				+ " FROM "+ schema +".dashboard d, "+ schema +".dashboardversion dv "
				+ "	WHERE dv.dashboard_id = d.id "
				+ " AND dv.user_id =:userId "
				+ "	 AND dv.dashboard_id NOT IN "
				+ "	(SELECT dashboard_id AS id "
				+ "	FROM "+ schema +".dashboardversionreport "
				+ "	WHERE report_id = :reportId "
				+ "	GROUP BY (report_id, dashboard_id)) "
				+ "	AND dv.dashboard_version = 'running' "
				+ "	   AND (cast(:search as varchar) is null OR dv.title like cast(:search as varchar))";
		Query query = 
				entityManager.createNativeQuery(qlString)
				.setParameter("userId",userId)
				.setParameter("reportId",reportId)
				.setParameter("search","%" + search + "%")
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
		List<Object[]> results = query.getResultList();
		List<DashboardDetailsOutput> finalResults = new ArrayList<>();
		
		for(Object[] obj : results) {
			DashboardDetailsOutput dashboardDetails = new DashboardDetailsOutput();

			// Here you manually obtain value from object and map to your pojo setters
			dashboardDetails.setId(obj[0]!=null ? Long.parseLong(obj[0].toString()) : null);
			dashboardDetails.setDashboardVersion(obj[1]!=null ? (obj[1].toString()) : null);
			dashboardDetails.setDescription(obj[2]!=null ? (obj[2].toString()) : null);
			dashboardDetails.setTitle(obj[3]!=null ? (obj[3].toString()) : null);
	
			dashboardDetails.setIsPublished(obj[4].toString().equals("true"));
            dashboardDetails.setUserId(obj[5]!=null ? Long.parseLong(obj[5].toString()) : null);
			finalResults.add(dashboardDetails);
		}
		
		int totalRows = results.size();
		Page<DashboardDetailsOutput> result = new PageImpl<DashboardDetailsOutput>(finalResults, pageable, totalRows);

		return result;
	}

}

