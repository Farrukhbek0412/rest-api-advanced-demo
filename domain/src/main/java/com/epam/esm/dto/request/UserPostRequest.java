package com.epam.esm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPostRequest {
    @NotBlank(message = "Enter name first!")
    private String name;
    @NotBlank(message = "Enter age first!")
    private String age;
    @Column(unique = true)
    @NotBlank(message = "Enter email first!")
    private String email;
    @NotBlank(message = "Enter password first!")
    private String password;
}
