package com.epam.esm.audit;

import com.epam.esm.entity.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditingListener {

    @PrePersist
    void prePersist(BaseEntity entity) {
        entity.setCreateDate(LocalDateTime.now());
        entity.setLastUpdateDate(LocalDateTime.now());
    }

    @PreUpdate
    void preUpdate(BaseEntity entity) {
        entity.setLastUpdateDate(LocalDateTime.now());
    }
}
