package com.epam.esm.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "tag")
public class TagEntity extends BaseEntity {

    @NotBlank(message = "tag name can not be null or empty")
    private String name;

}
