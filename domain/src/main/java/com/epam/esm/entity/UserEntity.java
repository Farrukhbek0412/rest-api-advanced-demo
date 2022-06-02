package com.epam.esm.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{

    private String name;
//    @Positive(message = "age must be positive")
//    @Min(message = "user under 18 years old cannot use our system", value = 18)
    private int age;
    private String email;
    private String password;

}
