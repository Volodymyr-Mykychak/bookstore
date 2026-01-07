package com.store.book.mapper;

import com.store.book.config.MapperConfig;
import com.store.book.dto.user.UserRegistrationRequestDto;
import com.store.book.dto.user.UserResponseDto;
import com.store.book.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserRegistrationRequestDto dto);
}
