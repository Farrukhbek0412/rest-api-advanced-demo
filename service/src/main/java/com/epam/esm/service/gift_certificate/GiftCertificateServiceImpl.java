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
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (StringUtils.isBlank(certificatePostRequest.getName())) {
            throw new InvalidCertificateException("Enter name first!");
        }

        if (!NumberUtils.isParsable(String.valueOf(certificatePostRequest.getPrice()))) {
            String price = "";
            if(certificatePostRequest.getPrice()==null){
                price = "is empty";
            }else {
                price = certificatePostRequest.getPrice();
            }
            throw new InvalidCertificateException("The price " + price + ", not valid");
        }else if(Integer.parseInt(certificatePostRequest.getPrice()) <0){
            throw new InvalidCertificateException(
                    "The price ( "+certificatePostRequest.getPrice()+" ) can not be negative");
        }

        if (!NumberUtils.isParsable(String.valueOf(certificatePostRequest.getDuration()))) {
            String duration = "";
            if(certificatePostRequest.getDuration()==null){
                duration = "is empty";
            }else {
                duration = certificatePostRequest.getDuration();
            }
            throw new InvalidCertificateException("The duration " + duration + ", not valid");
        }else if(Integer.parseInt(certificatePostRequest.getDuration()) <=0){
            throw new InvalidCertificateException(
                    "The duration ( "+certificatePostRequest.getDuration()+" ) must be positive");
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
            boolean isDescending, int limit, int offset) {
        List<GiftCertificateEntity> certificateEntities;
        if(tagName != null) {
            try {
                Long tagId = tagRepository.findByName(tagName).getId();
                certificateEntities = giftCertificateRepository.getAllWithSearchAndTagName(
                        searchWord, tagId, doNameSort, doDateSort, isDescending, limit, offset);
            }catch (NullPointerException e){
                throw new DataNotFoundException("Gift certificate ( tag name = "  + tagName+ " ) not found");
            }
        }else if(searchWord.equals("")){
            certificateEntities = giftCertificateRepository.getAllOnly(
                    doNameSort, doDateSort, isDescending, limit, offset);
        }else
            certificateEntities = giftCertificateRepository.getAllWithSearch(
                    searchWord, doNameSort, doDateSort, isDescending, limit, offset);
        if(certificateEntities.size() == 0)
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

        if (!NumberUtils.isParsable(String.valueOf(certificateUpdateRequest.getPrice()))) {
            String price = "";
            if(certificateUpdateRequest.getPrice()==null){
                price = "is empty";
            }else {
                price = certificateUpdateRequest.getPrice();
            }
            throw new InvalidCertificateException("The price " + price + ", not valid");
        }else if(Integer.parseInt(certificateUpdateRequest.getPrice()) <0){
            throw new InvalidCertificateException(
                    "The price ( "+certificateUpdateRequest.getPrice()+" ) can not be negative");
        }

        if (!NumberUtils.isParsable(String.valueOf(certificateUpdateRequest.getDuration()))) {
            String duration = "";
            if(certificateUpdateRequest.getDuration()==null){
                duration = "is empty";
            }else {
                duration = certificateUpdateRequest.getDuration();
            }
            throw new InvalidCertificateException("The duration " + duration + ", not valid");
        }else if(Integer.parseInt(certificateUpdateRequest.getDuration()) <=0){
            throw new InvalidCertificateException(
                    "The duration ( "+certificateUpdateRequest.getDuration()+" ) must be positive");
        }

    }

    @Override
    @Transactional
    //MUST CHECK FOR INVALID STRING INPUT
    public GiftCertificateGetResponse updateDuration(String duration, Long id) {
        int durationValue;
        try{
            durationValue=Integer.parseInt(duration);
        }catch(NumberFormatException e){
            throw new InvalidCertificateException("Duration must be numeric");
        }
        if(Integer.parseInt(duration)<=0){
            throw new InvalidCertificateException(
                    "The duration ( "+duration+" ) must be positive");
        }

        int updateDuration = giftCertificateRepository.updateDuration(Integer.parseInt(duration), id);
        if(updateDuration == 1) {
            GiftCertificateEntity giftCertificateEntity = giftCertificateRepository.findById(id).get();
            return modelMapper.map(giftCertificateEntity, GiftCertificateGetResponse.class);
        }
        throw new DataNotFoundException("cannot find gift certificate with id: " + id);
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
