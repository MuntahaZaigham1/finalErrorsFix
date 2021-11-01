package com.fastcode.example.domain.core.t1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.*;

@Repository("t1Repository")
public interface IT1Repository extends JpaRepository<T1, Long>,QuerydslPredicateExecutor<T1> {

    
	
}

