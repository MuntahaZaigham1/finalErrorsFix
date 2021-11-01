package com.fastcode.example.domain.core.authorization.userspermission;

import java.time.*;
import javax.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
public class UserspermissionId implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long permissionId;
    private Long usersId;
    
    public UserspermissionId(Long permissionId,Long usersId) {
 	this.permissionId = permissionId;
 	this.usersId = usersId;
    }
    
}
