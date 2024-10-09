package com.spring.microservices.analytics.service.dataaccess.repository;

import com.spring.microservices.analytics.service.dataaccess.entity.TwitterAnalyticsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TwitterAnalyticsRepository extends
        JpaRepository<TwitterAnalyticsEntity, UUID>,
        AnalyticsCustomRepository<TwitterAnalyticsEntity, UUID> {

    @Query(value = "select e from TwitterAnalyticsEntity e where e.word=:word order by e.recordDate desc")
    List<TwitterAnalyticsEntity> getAnalyticsEntitiesByWord(@Param("word") String word, Pageable pageable);
}
