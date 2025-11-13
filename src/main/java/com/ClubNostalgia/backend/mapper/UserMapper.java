package com.ClubNostalgia.backend.mapper;

    import com.ClubNostalgia.backend.dto.request.UserRequest;
    import com.ClubNostalgia.backend.dto.response.UserResponse;
    import com.ClubNostalgia.backend.entity.User;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse userToUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    User userRequestToUser(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "name", source = "request.name")
    User userRequestToUser(UserRequest request, String hashedPassword, String role);

}
