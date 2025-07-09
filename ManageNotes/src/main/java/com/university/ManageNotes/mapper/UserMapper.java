package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends com.university.ManageNotes.mapper.BaseMapper<Users, SignupRequest, UserResponse> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    UserResponse toResponse(Users user);

    @Override
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "gradesEntered", ignore = true)
    Users toEntity(SignupRequest signupRequest);

    @Override
    void updateEntityFromRequest(SignupRequest request, @org.mapstruct.MappingTarget Users entity);
}
