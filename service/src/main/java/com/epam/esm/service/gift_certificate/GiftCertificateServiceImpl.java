package com.epam.esm.service.gift_certificate;

import com.epam.esm.dto.response.GiftCertificateGetResponse;
import com.epam.esm.dto.request.GiftCertificatePostRequest;
import com.epam.esm.dto.request.GiftCertificateUpdateRequest;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.BreakingDataRelationshipException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.exception.tag.InvalidTagException;
import com.epam.esm.repository.gift_certificate.GiftCertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.utils.DataValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;

    private final TagRepository tagRepository;
    private final DataValidator dataValidator;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GiftCertificateGetResponse create(GiftCertificatePostRequest createCertificate) {

        dataValidator.validatePriceNumeric(createCertificate.getPrice());
        dataValidator.validatePricePositiveOrZero(createCertificate.getPrice());
        dataValidator.validateDurationNumeric(createCertificate.getDuration());
        dataValidator.validateDurationPositive(createCertificate.getDuration());

        List<TagEntity> tagEntities = createCertificate.getTagEntities();
        GiftCertificateEntity certificateEntity = modelMapper.map(createCertificate, GiftCertificateEntity.class);
        if (createCertificate.getTagEntities() != null && certificateEntity.getTagEntities().size() != 0)
            certificateEntity.setTagEntities(createTags(tagEntities));
        GiftCertificateEntity saved = giftCertificateRepository.create(certificateEntity);
        return modelMapper.map(saved, GiftCertificateGetResponse.class);
    }

    @Override
    @Transactional
    public GiftCertificateGetResponse get(Long certificateId) {
        Optional<GiftCertificateEntity> certificate = giftCertificateRepository.findById(certificateId);
        return certificate.map(gift -> modelMapper.map(gift, GiftCertificateGetResponse.class))
                .orElseThrow(() ->
                        new DataNotFoundException("certificate ( id = " + certificateId + " ) not found"));

    }

    @Override
    @Transactional
    public void delete(Long certificateId) {
        try {
            int delete = giftCertificateRepository.delete(certificateId);

            if (delete != 1) {
                throw new DataNotFoundException("Certificate ( id = " + certificateId + " ) not found to delete");
            }
        } catch (Exception e) {
            throw new BreakingDataRelationshipException("this certificate is " +
                    "ordered by many users, so it cannot be deleted");
        }
    }

    @Override
    @Transactional
    public List<GiftCertificateGetResponse> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int pageSize, int pageNo) {
        if (pageNo < 1 || pageSize < 0) {
            throw new InvalidCertificateException("Please enter valid number");
        }
        List<GiftCertificateEntity> certificateEntities;

        if (!tagName.isEmpty()) {
            Long tagId;
            Optional<TagEntity> byName = tagRepository.findByName(tagName);
            if (byName.isPresent()) {
                tagId = byName.get().getId();
            } else {
                throw new DataNotFoundException("Gift certificate ( tag name = " + tagName + " ) not found");
            }
            certificateEntities = giftCertificateRepository.getAllWithSearchAndTagName(
                    searchWord, tagId, doNameSort, doDateSort, isDescending, pageSize, pageNo);

        } else if (searchWord.isEmpty()) {
            certificateEntities = giftCertificateRepository.getAllOnly(
                    doNameSort, doDateSort, isDescending, pageSize, pageNo);
        } else
            certificateEntities = giftCertificateRepository.getAllWithSearch(
                    searchWord, doNameSort, doDateSort, isDescending, pageSize, pageNo);
        if (certificateEntities.isEmpty())
            throw new DataNotFoundException("no matching gift certificate found");
        return modelMapper.map(certificateEntities, new TypeToken<List<GiftCertificateGetResponse>>() {
        }.getType());
    }

    @Override
    @Transactional
    public GiftCertificateGetResponse update(GiftCertificateUpdateRequest update, Long certificateId) {
        Optional<GiftCertificateEntity> old = giftCertificateRepository.findById(certificateId);
        if (old.isEmpty()) {
            throw new DataNotFoundException("Certificate ( id = " + certificateId + " ) not found");
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        dataValidator.validatePriceNumeric(update.getPrice());
        dataValidator.validatePricePositiveOrZero(update.getPrice());
        dataValidator.validateDurationNumeric(update.getDuration());
        dataValidator.validateDurationPositive(update.getDuration());

        GiftCertificateEntity certificate = old.get();
        List<TagEntity> tagEntities;
        if (certificate.getTagEntities() == null) {
            tagEntities = new ArrayList<>();
        } else {
            tagEntities = certificate.getTagEntities();
        }
        modelMapper.map(update, certificate);
        if (update.getTagEntities() != null && !update.getTagEntities().isEmpty()) {
            tagEntities.addAll(createTags(update.getTagEntities()));
        }
        certificate.setTagEntities(tagEntities);
        GiftCertificateEntity updated = giftCertificateRepository.update(certificate);
        return modelMapper.map(updated, GiftCertificateGetResponse.class);
    }

    @Override
    @Transactional
    public GiftCertificateGetResponse updateDuration(String duration, Long id) {

        dataValidator.validateDurationExist(duration);
        dataValidator.validateDurationNumeric(duration);
        dataValidator.validateDurationPositive(duration);


        int updateDuration = giftCertificateRepository.updateDuration(Integer.parseInt(duration), id);
        if (updateDuration == 1) {
            GiftCertificateEntity giftCertificateEntity = giftCertificateRepository.findById(id).get();
            return modelMapper.map(giftCertificateEntity, GiftCertificateGetResponse.class);
        }
        throw new DataNotFoundException("cannot find gift certificate with id: " + id);
    }

    @Override
    @Transactional
    public GiftCertificateGetResponse updatePrice(String price, Long id) {


        dataValidator.validatePriceExist(price);
        dataValidator.validatePriceNumeric(price);
        dataValidator.validatePricePositiveOrZero(price);

        int updatePrice = giftCertificateRepository.updatePrice(
                BigDecimal.valueOf(Double.parseDouble(price)), id);
        if (updatePrice == 1) {
            GiftCertificateEntity giftCertificateEntity = giftCertificateRepository.findById(id).get();
            return modelMapper.map(giftCertificateEntity, GiftCertificateGetResponse.class);
        }
        throw new DataNotFoundException("gift certificate ( id = " + id + " ) not found");
    }

    @Override
    public List<GiftCertificateGetResponse> searchWithMultipleTags(
            List<String> tags, int pageSize, int pageNo) {
        if (pageNo < 1 || pageSize < 0) {
            throw new InvalidCertificateException("Please enter valid number");
        }
        List<TagEntity> tagEntities = new ArrayList<>();
        for (String tag : tags) {
            Optional<TagEntity> byName = tagRepository.findByName(tag);
            byName.ifPresent(tagEntities::add);
        }

        List<GiftCertificateEntity> certificateEntities
                = giftCertificateRepository.searchWithMultipleTags(tagEntities, pageSize, pageNo);
        if (certificateEntities.isEmpty())
            throw new DataNotFoundException("no certificate found with these tags");
        return modelMapper.map(certificateEntities, new TypeToken<List<GiftCertificateGetResponse>>() {
        }.getType());
    }

    private List<TagEntity> createTags(List<TagEntity> tagEntities) {
        List<TagEntity> tagEntityList = new ArrayList<>();
        tagEntities.forEach(tag -> {
            if (tag.getName() == null || tag.getName().isEmpty()) {
                throw new InvalidTagException("tag name cannot be empty or null");
            }
            Optional<TagEntity> byName = tagRepository.findByName(tag.getName());
            if (byName.isPresent()) {
                tagEntityList.add(byName.get());
            } else {
                tagEntityList.add(tagRepository.create(tag));
            }
        });
        return tagEntityList;
    }

}
