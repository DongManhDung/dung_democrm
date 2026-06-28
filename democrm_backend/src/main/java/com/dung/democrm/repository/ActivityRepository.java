package com.dung.democrm.repository;

import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.BaseRepository;
import com.dung.democrm.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Long> {
    List<Activity> findByLeadOrderByActivityDateDesc(Lead lead);
}
