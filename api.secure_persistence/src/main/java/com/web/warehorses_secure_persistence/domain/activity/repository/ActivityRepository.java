package com.web.warehorses_secure_persistence.domain.activity.repository;

import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByType(Activity.TxnType type, Pageable pageable);

    Page<Activity> findByClientClientId(int clientId, Pageable pageable);

    @Query("SELECT a FROM Activity a WHERE a.createdAt BETWEEN :start AND :end")
    List<Activity> findByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}