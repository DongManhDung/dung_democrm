package com.dung.democrm.repository;

import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends BaseRepository<Activity, Long> {
}
