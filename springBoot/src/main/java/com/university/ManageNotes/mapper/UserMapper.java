package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @org.mapstruct.Builder(disableBuilder = true))
public interface UserMapper extends com.university.ManageNotes.mapper.BaseMapper<Users, SignupRequest, UserResponse> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    @Mapping(source = "levels", target = "levels")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "phone", target = "phone")
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "matricule", ignore = true)
    @Mapping(target = "speciality", ignore = true)
    @Mapping(target = "cycle", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "placeOfBirth", ignore = true)
    UserResponse toResponse(Users user);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "gradesEntered", ignore = true)
    @Mapping(target = "mustChangePassword", ignore = true)
    @Mapping(target = "levels", ignore = true)  // Handle manually in service
    @Mapping(target = "department", ignore = true)  // Handle manually in service
    @Mapping(target = "phone", ignore = true)  // Handle manually in service
    Users toEntity(SignupRequest signupRequest);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "gradesEntered", ignore = true)
    @Mapping(target = "mustChangePassword", ignore = true)
    @Mapping(target = "levels", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Users updateEntityFromRequest(SignupRequest request, @MappingTarget Users entity);
}
