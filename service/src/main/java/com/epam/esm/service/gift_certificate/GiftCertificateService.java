package com.epam.esm.service.gift_certificate;

import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.response.GiftCertificateGetResponse;
import com.epam.esm.dto.request.GiftCertificatePostRequest;
import com.epam.esm.dto.request.GiftCertificateUpdateRequest;
import com.epam.esm.service.BaseService;
import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificatePostRequest, GiftCertificateGetResponse> {

    List<GiftCertificateGetResponse> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int limit, int offset
    );

    GiftCertificateGetResponse update(GiftCertificateUpdateRequest update, Long certificateId);

    GiftCertificateGetResponse updateDuration(String duration, Long id);
    GiftCertificateGetResponse updatePrice(String price, Long id);

    List<GiftCertificateGetResponse> searchWithMultipleTags(List<String> tags, int limit, int offset);

}
