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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService{
    private final GiftCertificateRepository giftCertificateRepository;

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GiftCertificateGetResponse create(GiftCertificatePostRequest createCertificate) {
        isValid(createCertificate);

        List<TagEntity> tagEntities = createCertificate.getTagEntities();
        GiftCertificateEntity certificateEntity = modelMapper.map(createCertificate, GiftCertificateEntity.class);
        if(createCertificate.getTagEntities() != null && certificateEntity.getTagEntities().size() != 0)
            certificateEntity.setTagEntities(createTags(tagEntities));
        GiftCertificateEntity saved = giftCertificateRepository.create(certificateEntity);
        return modelMapper.map(saved, GiftCertificateGetResponse.class);
    }

    void isValid(GiftCertificatePostRequest certificatePostRequest){
        if(certificatePostRequest.getName().equals("null")){
            throw new InvalidCertificateException("name can not be null");
        }
        if (StringUtils.isBlank(certificatePostRequest.getPrice())) {
            throw new InvalidCertificateException("Enter price first!");
        }

        try{
            Double price = Double.parseDouble(certificatePostRequest.getPrice());
            if(price <0){
                throw new InvalidCertificateException(
                        "The price ( "+price+" ) can not be negative");
            }
        }catch(NumberFormatException e){
            throw new InvalidCertificateException("Do not enter String ("+
                    certificatePostRequest.getPrice()+" )");
        }

        if (StringUtils.isBlank(certificatePostRequest.getDuration())) {
            throw new InvalidCertificateException("Enter duration first!");
        }

        try{
            Integer duration = Integer.parseInt(certificatePostRequest.getDuration());
            if(duration <=0){
                throw new InvalidCertificateException(
                        "The duration ( "+duration+" ) must be positive");
            }
        }catch(NumberFormatException e){
            throw new InvalidCertificateException("Do not enter String ("+
                    certificatePostRequest.getDuration()+" )");
        }

    }

    @Override
    @Transactional
    public GiftCertificateGetResponse get(Long certificateId) {
        Optional<GiftCertificateEntity> certificate = giftCertificateRepository.findById(certificateId);
        if(certificate.isPresent()){
            return modelMapper.map(certificate.get(), GiftCertificateGetResponse.class);
        }
        throw new DataNotFoundException("certificate ( id = " + certificateId+" ) not found");
    }

    @Override
    @Transactional
    public int delete(Long certificateId) {
        try {
            return giftCertificateRepository.delete(certificateId);
        }catch (Exception e){
            throw new BreakingDataRelationshipException("this certificate is " +
                    "ordered by many users, so it cannot be deleted");
        }
    }

    @Override
    @Transactional
    public List<GiftCertificateGetResponse> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int pageSize, int pageNo) {
        List<GiftCertificateEntity> certificateEntities;
        if(!tagName.isEmpty()) {
            try {
                Long tagId = tagRepository.findByName(tagName).getId();
                certificateEntities = giftCertificateRepository.getAllWithSearchAndTagName(
                        searchWord, tagId, doNameSort, doDateSort, isDescending, pageSize, pageNo);
            }catch (NullPointerException e){
                throw new DataNotFoundException("Gift certificate ( tag name = "  + tagName+ " ) not found");
            }
        }else if(searchWord.equals("")){
            certificateEntities = giftCertificateRepository.getAllOnly(
                    doNameSort, doDateSort, isDescending, pageSize, pageNo);
        }else
            certificateEntities = giftCertificateRepository.getAllWithSearch(
                    searchWord, doNameSort, doDateSort, isDescending, pageSize, pageNo);
        if(certificateEntities.isEmpty())
            throw new DataNotFoundException("no matching gift certificate found");
        return modelMapper.map(certificateEntities, new TypeToken<List<GiftCertificateGetResponse>>() {}.getType());
    }

    @Override
    @Transactional
    public GiftCertificateGetResponse update(GiftCertificateUpdateRequest update, Long certificateId) {
        Optional<GiftCertificateEntity> old = giftCertificateRepository.findById(certificateId);
        if(old.isEmpty())
            throw new DataNotFoundException("Certificate ( id = " + certificateId + " ) not found");
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        isValid(update);
        GiftCertificateEntity certificate = old.get();
        List<TagEntity> tagEntities;
        if(certificate.getTagEntities() == null)
            tagEntities = new ArrayList<>();
        else
            tagEntities= certificate.getTagEntities();
        modelMapper.map(update, certificate);
        if(update.getTagEntities() != null && !update.getTagEntities().isEmpty()) {
            tagEntities.addAll(createTags(update.getTagEntities()));
        }
        certificate.setTagEntities(tagEntities);
        GiftCertificateEntity updated = giftCertificateRepository.update(certificate);
        return modelMapper.map(updated, GiftCertificateGetResponse.class);
    }
    void isValid(GiftCertificateUpdateRequest certificateUpdateRequest){
        if (StringUtils.isBlank(certificateUpdateRequest.getName())) {
            throw new InvalidCertificateException("Enter name first!");
        }
        if (StringUtils.isBlank(certificateUpdateRequest.getPrice())) {
            throw new InvalidCertificateException("Enter price first!");
        }

        try{
            Double price = Double.parseDouble(certificateUpdateRequest.getPrice());
            if(price <0){
                throw new InvalidCertificateException(
                        "The price ( "+price+" ) can not be negative");
            }
        }catch(NumberFormatException e){
            throw new InvalidCertificateException("Do not enter String ("+
                    certificateUpdateRequest.getPrice()+" )");
        }

        if (StringUtils.isBlank(certificateUpdateRequest.getDuration())) {
            throw new InvalidCertificateException("Enter duration first!");
        }

        try{
            Integer duration = Integer.parseInt(certificateUpdateRequest.getDuration());
            if(duration <=0){
                throw new InvalidCertificateException(
                        "The duration ( "+duration+" ) must be positive");
            }
        }catch(NumberFormatException e){
            throw new InvalidCertificateException("Do not enter String ("+
                    certificateUpdateRequest.getDuration()+" )");
        }
    }

    @Override
    @Transactional
    public GiftCertificateGetResponse updateDuration(String duration, Long id) {
        if (StringUtils.isBlank(duration)) {
            throw new InvalidCertificateException("Enter duration first!");
        }

        try{
            Integer durationCheck = Integer.parseInt(duration);
            if(durationCheck <=0){
                throw new InvalidCertificateException(
                        "The duration ( "+duration+" ) must be positive");
            }
        }catch(NumberFormatException e){
            throw new InvalidCertificateException("Do not enter String ("+duration+" )");
        }
        int updateDuration = giftCertificateRepository.updateDuration(Integer.parseInt(duration), id);
        if(updateDuration == 1) {
            GiftCertificateEntity giftCertificateEntity = giftCertificateRepository.findById(id).get();
            return modelMapper.map(giftCertificateEntity, GiftCertificateGetResponse.class);
        }
        throw new DataNotFoundException("cannot find gift certificate with id: " + id);
    }

    @Override
    @Transactional
    public GiftCertificateGetResponse updatePrice(String price, Long id) {
        if (StringUtils.isBlank(price)) {
            throw new InvalidCertificateException("Enter price first!");
        }
        try{
            Double priceValue = Double.parseDouble(price);
            if(priceValue <0){
                throw new InvalidCertificateException(
                        "The price ( "+priceValue+" ) can not be negative");
            }
        }catch(NumberFormatException e){
            throw new InvalidCertificateException("Do not enter String ("+
                    price+" )");
        }

        int updatePrice = giftCertificateRepository.updatePrice(
                BigDecimal.valueOf(Double.parseDouble(price)), id);
        if(updatePrice == 1) {
            GiftCertificateEntity giftCertificateEntity = giftCertificateRepository.findById(id).get();
            return modelMapper.map(giftCertificateEntity, GiftCertificateGetResponse.class);
        }
        throw new DataNotFoundException("gift certificate ( id = " + id+" ) not found");
    }

    @Override
    public List<GiftCertificateGetResponse> searchWithMultipleTags(
            List<String> tags, int limit, int offset) {
        List<TagEntity> tagEntities = new ArrayList<>();
        tags.forEach(tag -> {
            TagEntity byName = tagRepository.findByName(tag);
            if(byName != null)
                tagEntities.add(byName);
        });
        List<GiftCertificateEntity> certificateEntities
                = giftCertificateRepository.searchWithMultipleTags(tagEntities, limit, offset);
        if(certificateEntities.isEmpty())
            throw new DataNotFoundException("no certificate found with these tags");
        return modelMapper.map(certificateEntities, new TypeToken<List<GiftCertificateGetResponse>>() {}.getType());
    }

    private List<TagEntity> createTags(List<TagEntity> tagEntities){
        List<TagEntity> tagEntityList = new ArrayList<>();
        tagEntities.forEach(tag -> {
            if(tag.getName() == null || tag.getName().isEmpty()) {
                throw new InvalidTagException("tag name cannot be empty or null");
            }
            TagEntity byName = tagRepository.findByName(tag.getName());
            if(byName != null){
                tagEntityList.add(byName);
            }else{
                tagEntityList.add(tagRepository.create(tag));
            }
        });
        return tagEntityList;
    }

}
