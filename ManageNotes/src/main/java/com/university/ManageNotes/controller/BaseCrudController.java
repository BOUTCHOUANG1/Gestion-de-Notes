package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.service.BaseCrudService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseCrudController<ID, Req, Res> {

    protected abstract BaseCrudService<?, ID, Req, Res> service();

    @GetMapping
    public List<Res> all() {
        return service().getAll();
    }

    @GetMapping("/{id}")
    public Res one(@PathVariable ID id) {
        return service().getById(id);
    }

    @PostMapping
    public MessageResponse create(@Valid @RequestBody Req request) {
        return service().create(request);
    }

    @PutMapping("/{id}")
    public MessageResponse update(@PathVariable ID id,
                                  @Valid @RequestBody Req request) {
        return service().update(id, request);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable ID id) {
        return service().delete(id);
    }
}
