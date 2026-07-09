package com.dung.democrm.repository;

import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Long> {
    Optional<Activity> findByIdAndActiveTrue(Long id);
    List<Activity> findAllByActiveTrue();

}

