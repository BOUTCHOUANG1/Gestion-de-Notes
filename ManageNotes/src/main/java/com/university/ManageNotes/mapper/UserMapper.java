package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @org.mapstruct.Builder(disableBuilder = true))
public interface UserMapper extends com.university.ManageNotes.mapper.BaseMapper<Users, SignupRequest, UserResponse> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    UserResponse toResponse(Users user);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "gradesEntered", ignore = true)
    Users toEntity(SignupRequest signupRequest);

    @Override
    Users updateEntityFromRequest(SignupRequest request, @org.mapstruct.MappingTarget Users entity);
}
