package com.epam.esm.dto.request;

import com.epam.esm.entity.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GiftCertificatePostRequest {
    @NotBlank(message = "name can not be null or empty")
    private String name;
    @NotBlank(message = "description can not be null or empty")
    private String description;
    private String price;
    private String duration;
    private List<TagEntity> tagEntities;
}
