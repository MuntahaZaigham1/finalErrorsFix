package com.fastcode.example.domain.core.authorization.tokenverification;

import java.time.*;
import javax.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
public class TokenverificationId implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String tokenType;
    private Long usersId;
    
    public TokenverificationId(String tokenType,Long usersId) {
 	this.tokenType = tokenType;
 	this.usersId = usersId;
    }
    
}
