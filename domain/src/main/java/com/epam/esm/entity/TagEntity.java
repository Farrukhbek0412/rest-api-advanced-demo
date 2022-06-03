package com.epam.esm.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Getter
@Setter

@Entity
@Table(name = "tag")
public class TagEntity extends BaseEntity {

    @NotBlank(message = "tag name can not be null or empty")
    private String name;

    public TagEntity() {
    }

    public TagEntity(String name) {
        this.name = name;
    }

    public TagEntity(Long id, LocalDateTime createDate, LocalDateTime lastUpdateDate, String name) {
        super(id, createDate, lastUpdateDate);
        this.name = name;
    }
}
