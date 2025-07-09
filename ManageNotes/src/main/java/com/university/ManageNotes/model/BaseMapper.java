package com.university.ManageNotes.mapper;

public interface BaseMapper<E, Req, Res> {
    E toEntity(Req request);
    Res toResponse(E entity);
    void updateEntityFromRequest(Req request, @org.mapstruct.MappingTarget E entity);
}
