package com.example.chatserver.user;

import lombok.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDto);

    UserDTO toDto(User user);
}
