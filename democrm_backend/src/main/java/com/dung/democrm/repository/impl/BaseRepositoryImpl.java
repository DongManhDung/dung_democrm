package com.dung.democrm.repository.impl;

import com.dung.democrm.entity.BaseEntity;
import com.dung.democrm.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BaseRepositoryImpl<T extends BaseEntity, ID>
        extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(
            JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager
    ){
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findActiveById(ID id) {
        return findById(id).filter(BaseEntity::isActive);
    }

    @Override
    public List<T> findAllActive() {
        return findAll().stream().filter(BaseEntity::isActive).toList();
    }

    @Override
    public void softDelete(T entity) {
        entity.setActive(false);
        entity.setDeletedAt(LocalDateTime.now());
        save(entity);
    }

    @Override
    public void restore(T entity) {
        entity.setActive(true);
        entity.setDeletedAt(null);
        save(entity);
    }
}
