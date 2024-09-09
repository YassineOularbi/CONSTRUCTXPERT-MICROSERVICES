package com.user_service.mapper;

import com.user_service.dto.*;
import com.user_service.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface UserMapper {

    @SubclassMapping(source = AdminDto.class, target = Admin.class)
    @SubclassMapping(source = ClientDto.class, target = Client.class)
    @SubclassMapping(source = SupervisorDto.class, target = Supervisor.class)
    User toEntity(UserDto userDto);
    @SubclassMapping(source = Admin.class, target = AdminDto.class)
    @SubclassMapping(source = Client.class, target = ClientDto.class)
    @SubclassMapping(source = Supervisor.class, target = SupervisorDto.class)
    UserDto toDto(User user);

    User partialUpdate(UserDto userDto, @MappingTarget User user);

}
