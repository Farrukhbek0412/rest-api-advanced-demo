package com.epam.esm.resources;

import com.epam.esm.dto.response.GiftCertificateGetResponse;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class GiftCertificateResource extends RepresentationModel<GiftCertificateResource> {
    private final GiftCertificateGetResponse giftCertificate;

    public GiftCertificateResource(GiftCertificateGetResponse giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public void getData() {
    }
}
