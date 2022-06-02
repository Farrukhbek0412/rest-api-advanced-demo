package com.epam.esm.dto.request;

import com.epam.esm.entity.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GiftCertificateUpdateRequest {
    private String name;
    private String description;
    @Nullable
    private String price;
    @Positive(message = "duration should be positive")
    private String  duration;
    private List<TagEntity> tagEntities;
}
