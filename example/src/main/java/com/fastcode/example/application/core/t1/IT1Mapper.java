package com.fastcode.example.application.core.t1;

import org.mapstruct.Mapper;
import com.fastcode.example.application.core.t1.dto.*;
import com.fastcode.example.domain.core.t1.T1;
import java.time.*;

@Mapper(componentModel = "spring")
public interface IT1Mapper {
   T1 createT1InputToT1(CreateT1Input t1Dto);
   CreateT1Output t1ToCreateT1Output(T1 entity);
   
    T1 updateT1InputToT1(UpdateT1Input t1Dto);
    
   	UpdateT1Output t1ToUpdateT1Output(T1 entity);
   	FindT1ByIdOutput t1ToFindT1ByIdOutput(T1 entity);

}

