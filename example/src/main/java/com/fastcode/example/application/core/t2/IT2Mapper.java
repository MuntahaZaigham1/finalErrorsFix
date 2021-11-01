package com.fastcode.example.application.core.t2;

import org.mapstruct.Mapper;
import com.fastcode.example.application.core.t2.dto.*;
import com.fastcode.example.domain.core.t2.T2;
import java.time.*;

@Mapper(componentModel = "spring")
public interface IT2Mapper {
   T2 createT2InputToT2(CreateT2Input t2Dto);
   CreateT2Output t2ToCreateT2Output(T2 entity);
   
    T2 updateT2InputToT2(UpdateT2Input t2Dto);
    
   	UpdateT2Output t2ToUpdateT2Output(T2 entity);
   	FindT2ByIdOutput t2ToFindT2ByIdOutput(T2 entity);

}

