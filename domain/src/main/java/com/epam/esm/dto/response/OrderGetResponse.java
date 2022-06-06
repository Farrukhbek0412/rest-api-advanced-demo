package com.epam.esm.dto.response;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderGetResponse {
    private Long id;
    private UserEntity user;
    private BigDecimal price;
    private GiftCertificateEntity certificate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
