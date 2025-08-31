package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.mapper.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseCrudService<E, ID, Req, Res> {

    protected final JpaRepository<E, ID> repository;
    protected final BaseMapper<E, Req, Res> mapper;

    @Transactional(readOnly = true)
    public List<Res> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Res getById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        return mapper.toResponse(entity);
    }

    @Transactional
    public MessageResponse create(Req request) {
        E entity = mapper.toEntity(request);
        repository.save(entity);
        return MessageResponse.success("Created successfully");
    }

    @Transactional
    public MessageResponse update(ID id, Req request) {
        E existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        mapper.updateEntityFromRequest(request, existing);
        repository.save(existing);
        return MessageResponse.success("Updated successfully");
    }

    @Transactional
    public MessageResponse delete(ID id) {
        repository.deleteById(id);
        return MessageResponse.success("Deleted successfully");
    }
}
