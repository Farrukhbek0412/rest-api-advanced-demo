package com.epam.esm.service.utils;

import com.epam.esm.dto.response.GiftCertificateGetResponse;
import com.epam.esm.dto.request.GiftCertificatePostRequest;
import com.epam.esm.dto.request.GiftCertificateUpdateRequest;
import com.epam.esm.entity.GiftCertificateEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class GiftCertificateServiceTestUtils {

    public static GiftCertificateEntity getGiftCertificateEntity() {
        GiftCertificateEntity certificateEntity = new GiftCertificateEntity();
        certificateEntity.setId(1L);
        certificateEntity.setName("Test");
        certificateEntity.setDescription("Test description");
        certificateEntity.setPrice(new BigDecimal("1.0"));
        certificateEntity.setDuration(1);
        certificateEntity.setTagEntities(TagServiceTestUtils.getTagEntities());
        certificateEntity.setCreateDate(LocalDateTime.of(2002, Month.AUGUST, 20, 22, 22));
        certificateEntity.setLastUpdateDate(LocalDateTime.now());
        return certificateEntity;
    }

    public static GiftCertificateGetResponse getGiftCertificateGetResponse() {
        GiftCertificateGetResponse certificateGetResponse = new GiftCertificateGetResponse();
        certificateGetResponse.setId(1L);
        certificateGetResponse.setName("Data");
        certificateGetResponse.setDescription("this is for test");
        certificateGetResponse.setPrice(new BigDecimal(1));
        certificateGetResponse.setDuration(1);
        certificateGetResponse.setCreateDate(LocalDateTime.of(2022, Month.AUGUST, 20, 22, 22));
        return certificateGetResponse;
    }

    public static GiftCertificatePostRequest getGiftCertificatePostRequest() {
        GiftCertificatePostRequest giftCertificatePostRequest = new GiftCertificatePostRequest();
        giftCertificatePostRequest.setName("Store");
        giftCertificatePostRequest.setDescription("Gift certificate for test");
        giftCertificatePostRequest.setPrice("2");
        giftCertificatePostRequest.setDuration("1");
        giftCertificatePostRequest.setTagEntities((TagServiceTestUtils.getTagEntities()));
        return giftCertificatePostRequest;
    }

    public static GiftCertificateUpdateRequest getGiftCertificateUpdateRequest() {
        GiftCertificateUpdateRequest giftCertificateUpdateRequest = new GiftCertificateUpdateRequest();
        giftCertificateUpdateRequest.setName("Data");
        giftCertificateUpdateRequest.setDescription("Gift certificate for test");
        giftCertificateUpdateRequest.setPrice("20");
        giftCertificateUpdateRequest.setDuration("1");
        giftCertificateUpdateRequest.setTagEntities((TagServiceTestUtils.getTagEntities()));
        return giftCertificateUpdateRequest;
    }

    public static List<GiftCertificateEntity> getGiftCertificateEntities() {
        GiftCertificateEntity certificateEntity = new GiftCertificateEntity();
        certificateEntity.setId(1L);
        certificateEntity.setName("Store");

        GiftCertificateEntity certificateEntity1 = new GiftCertificateEntity();
        certificateEntity1.setId(2L);
        certificateEntity1.setName("Kids Clothes");
        certificateEntity1.setTagEntities(TagServiceTestUtils.getTagEntities());

        GiftCertificateEntity certificateEntity2 = new GiftCertificateEntity();
        certificateEntity2.setId(3L);
        certificateEntity2.setName("Men Clothes");

        return Arrays.asList(
                certificateEntity, certificateEntity1, certificateEntity2);
    }

    public static List<GiftCertificateGetResponse> getGiftCertificateGetResponses() {
        GiftCertificateGetResponse certificateGetResponse = new GiftCertificateGetResponse();
        certificateGetResponse.setId(1L);
        certificateGetResponse.setName("Store");

        GiftCertificateGetResponse certificateGetResponse1 = new GiftCertificateGetResponse();
        certificateGetResponse1.setId(2L);
        certificateGetResponse1.setName("Kids Clothes");
        certificateGetResponse1.setTagEntities(TagServiceTestUtils.getTagEntities());

        GiftCertificateGetResponse certificateGetResponse2 = new GiftCertificateGetResponse();
        certificateGetResponse2.setId(3L);
        certificateGetResponse2.setName("Men Clothes");
        return Arrays.asList(
                certificateGetResponse, certificateGetResponse1, certificateGetResponse2);
    }
}
