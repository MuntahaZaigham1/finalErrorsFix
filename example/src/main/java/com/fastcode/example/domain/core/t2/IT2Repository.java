package com.fastcode.example.domain.core.t2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.time.*;

@Repository("t2Repository")
public interface IT2Repository extends JpaRepository<T2, T2Id>,QuerydslPredicateExecutor<T2> {

    
	
}

