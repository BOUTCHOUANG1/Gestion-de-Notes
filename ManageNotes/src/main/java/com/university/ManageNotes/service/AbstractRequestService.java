package com.university.ManageNotes.service;

import com.university.ManageNotes.model.BaseRequest;
import com.university.ManageNotes.model.RequestStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractRequestService<T extends BaseRequest, D, C>
        implements RequestService<D, C> {

    protected final JpaRepository<T, Long> repository;
    protected final Function<T, D> toDtoFunction;

    protected AbstractRequestService(JpaRepository<T, Long> repository,
                                     Function<T, D> toDtoFunction) {
        this.repository = repository;
        this.toDtoFunction = toDtoFunction;
    }

    @Override
    @Transactional
    public D approve(Long id, String comment) {
        T request = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Request already processed");
        }

        request.setStatus(RequestStatus.APPROVED);
        request.setResolvedAt(java.time.LocalDateTime.now());
        onApprove(request);

        return toDto(repository.save(request));
    }

    @Override
    @Transactional
    public D reject(Long id, String reason) {
        T request = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setResolvedAt(java.time.LocalDateTime.now());

        return toDto(repository.save(request));
    }

    @Override
    public List<D> getPending() {
        return repository.findAll().stream()
                .filter(Objects::nonNull)
                .map(r -> (BaseRequest) r)
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .map(entity -> this.toDto((T) entity))
                .collect(Collectors.toList());
    }

    @Override
    public D getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
    }

    protected D toDto(T entity) {
        return toDtoFunction.apply(entity);
    }

    protected abstract void onApprove(T request);

}
