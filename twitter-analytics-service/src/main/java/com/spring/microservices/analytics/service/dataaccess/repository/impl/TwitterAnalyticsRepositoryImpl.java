package com.spring.microservices.analytics.service.dataaccess.repository.impl;

import com.spring.microservices.analytics.service.dataaccess.entity.BaseEntity;
import com.spring.microservices.analytics.service.dataaccess.repository.AnalyticsCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TwitterAnalyticsRepositoryImpl<T extends BaseEntity<PK>, PK> implements AnalyticsCustomRepository<T, PK> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterAnalyticsRepositoryImpl.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size:50}")
    protected int batchSize;

    @Override
    @Transactional
    public <S extends T> PK persist(S entity) {
        this.entityManager.persist(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public <S extends T> void batchPersist(Collection<S> entities) {
        if (entities.isEmpty()) {
            LOG.info("No entity found to insert!");
            return;
        }
        int batchCount = 0;
        for (S entity : entities) {
            LOG.trace("Persisting entity with id {}", entity.getId());
            this.entityManager.persist(entity);
            batchCount++;
            if (batchCount % batchSize == 0) {
                this.entityManager.flush();
                this.entityManager.clear();
            }
        }
        if (batchCount % batchSize != 0) {
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }

    @Override
    @Transactional
    public <S extends T> S merge(S entity) {
        return this.entityManager.merge(entity);
    }

    @Override
    @Transactional
    public <S extends T> void batchMerge(Collection<S> entities) {
        if (entities.isEmpty()) {
            LOG.info("No entity found to insert!");
            return;
        }
        int batchCount = 0;
        for (S entity : entities) {
            LOG.trace("Merging entity with id {}", entity.getId());
            this.entityManager.merge(entity);
            batchCount++;
            if (batchCount % batchSize == 0) {
                this.entityManager.flush();
                this.entityManager.clear();
            }
        }
        if (batchCount % batchSize != 0) {
            this.entityManager.flush();
            this.entityManager.clear();
        }
    }

    @Override
    @Transactional
    public void clear() {
        this.entityManager.clear();
    }
}
