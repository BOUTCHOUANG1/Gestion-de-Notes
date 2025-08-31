package com.university.ManageNotes.mapper;


public interface BaseMapper<E, Req, Res> {
    E toEntity(Req request);
    Res toResponse(E entity);
    E updateEntityFromRequest(Req request, E entity);
}
