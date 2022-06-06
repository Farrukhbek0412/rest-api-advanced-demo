package com.epam.esm.dto.request;

import com.epam.esm.entity.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GiftCertificateUpdateRequest {
    @NotBlank(message = "name can not be empty")
    private String name;
    private String description;
    @NotBlank(message = "price can not be empty")
    private String price;
    @NotBlank(message = "duration can not be empty")
    private String duration;
    private List<TagEntity> tagEntities;
}
