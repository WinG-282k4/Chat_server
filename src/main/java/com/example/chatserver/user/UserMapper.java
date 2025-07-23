package com.example.chatserver.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userId", target = "userId")
    UserDTO toDto(User user);

    @Mapping(source = "userId", target = "userId")
    User toEntity(UserDTO userDto);

}
