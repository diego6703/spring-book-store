package p.projects.springbookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.UserRegistrationRequestDto;
import p.projects.springbookstore.dto.UserResponseDto;
import p.projects.springbookstore.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

    @Mapping(target = "email", expression = "java(requestDto.email().toLowerCase())")
    User toEntity(UserRegistrationRequestDto requestDto);
}
