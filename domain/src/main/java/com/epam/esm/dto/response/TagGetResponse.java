package com.epam.esm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagGetResponse {
    private Long id;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

}
