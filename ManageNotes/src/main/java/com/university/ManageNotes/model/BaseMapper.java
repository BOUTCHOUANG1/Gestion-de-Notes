package com.university.ManageNotes.model;

public interface BaseMapper<E, Req, Res> {
    E toEntity(Req request);
    Res toResponse(E entity);
    void updateEntityFromRequest(Req request, E entity);
}
