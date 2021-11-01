package com.fastcode.example.domain.core.t2;

import java.time.*;
import javax.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
public class T2Id implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;
    private String pic;
    
    public T2Id(Long id,String pic) {
 	this.id = id;
 	this.pic = pic;
    }
    
}
