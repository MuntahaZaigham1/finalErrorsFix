package com.fastcode.example.domain.core.authorization.tokenverification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.time.*;


@Repository
public interface ITokenverificationRepository extends JpaRepository<Tokenverification, TokenverificationId>,QuerydslPredicateExecutor<Tokenverification> {

	Tokenverification findByTokenAndTokenType(String token, String tokenType);
	 
	Tokenverification findByUsersIdAndTokenType(Long id ,String tokenType);
}

