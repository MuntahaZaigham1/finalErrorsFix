package com.fastcode.example.application.core.authorization.permission;

import org.mapstruct.Mapper;
import com.fastcode.example.application.core.authorization.permission.dto.*;
import com.fastcode.example.domain.core.authorization.permission.Permission;
import java.time.*;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {
   Permission createPermissionInputToPermission(CreatePermissionInput permissionDto);
   CreatePermissionOutput permissionToCreatePermissionOutput(Permission entity);
   
    Permission updatePermissionInputToPermission(UpdatePermissionInput permissionDto);
    
   	UpdatePermissionOutput permissionToUpdatePermissionOutput(Permission entity);
   	FindPermissionByIdOutput permissionToFindPermissionByIdOutput(Permission entity);

 	FindPermissionByNameOutput permissionToFindPermissionByNameOutput(Permission entity);
 	
}

