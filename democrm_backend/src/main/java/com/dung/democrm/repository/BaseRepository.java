package com.dung.democrm.repository;

import com.dung.democrm.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
    Optional<T> findActiveById(ID id);
    List<T> findAllActive();
    void softDelete(T entity);
    void restore(T entity);
}
