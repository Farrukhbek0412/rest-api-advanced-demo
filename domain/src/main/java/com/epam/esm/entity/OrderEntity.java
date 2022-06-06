package com.epam.esm.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    private BigDecimal price;

    @ManyToOne
    @JsonIgnore
    private GiftCertificateEntity certificate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private UserEntity user;
}
