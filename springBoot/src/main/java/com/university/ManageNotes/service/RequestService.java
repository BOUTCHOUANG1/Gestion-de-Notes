// service/RequestService.java
package com.university.ManageNotes.service;

import java.util.List;

public interface RequestService<D, C> {
    D create(C createDto);
    D approve(Long id, String comment);
    D reject(Long id, String reason);
    List<D> getPending();
    D getById(Long id);
}