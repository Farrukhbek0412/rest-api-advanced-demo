package com.epam.esm.controller;

import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.response.GiftCertificateGetResponse;
import com.epam.esm.dto.request.GiftCertificatePostRequest;
import com.epam.esm.dto.request.GiftCertificateUpdateRequest;
import com.epam.esm.dto.response.UserGetResponse;
import com.epam.esm.exception.InvalidInputException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.service.gift_certificate.GiftCertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/gift-certificates")
@AllArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GiftCertificateGetResponse> create(
            @Valid @RequestBody GiftCertificatePostRequest createCertificate,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult);
        }
        GiftCertificateGetResponse response = giftCertificateService.create(createCertificate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public GiftCertificateGetResponse get(
            @PathVariable Long id

    ) {
        GiftCertificateGetResponse response = giftCertificateService.get(id);
        addLinkForOrders(response);
        return response;
    }

    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResponse<List<GiftCertificateGetResponse>> getAll(
            @RequestParam(required = false, name = "search_word", defaultValue = "") String searchWord,
            @RequestParam(required = false, name = "tag_name", defaultValue = "") String tagName,
            @RequestParam(required = false, name = "do_name_sort") boolean doNameSort,
            @RequestParam(required = false, name = "do_date_sort") boolean doDateSort,
            @RequestParam(required = false, name = "is_descending") boolean isDescending,
            @RequestParam(required = false, name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(required = false, name = "pageNo", defaultValue = "1") int pageNo

    ) {
        List<GiftCertificateGetResponse> certificates = giftCertificateService.getAll(
                searchWord, tagName, doNameSort, doDateSort, isDescending, pageSize, pageNo);
        for (GiftCertificateGetResponse certificate : certificates) {
            addLinkForOrders(certificate);
        }

        BaseResponse<List<GiftCertificateGetResponse>> response = new BaseResponse<>(
                HttpStatus.OK.value(), "certificate list", certificates);

        if (!certificates.isEmpty())
            response.add(linkTo(methodOn(GiftCertificateController.class)
                    .getAll(searchWord, tagName, doNameSort, doDateSort, isDescending, pageSize, pageNo + 1))
                    .withRel("next page"));

        if (pageNo > 1) {
            response.add(linkTo(methodOn(GiftCertificateController.class)
                    .getAll(searchWord, tagName, doNameSort, doDateSort, isDescending, pageSize, pageNo - 1))
                    .withRel("previous page"));
        }
        return response;
    }

    @GetMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResponse<List<GiftCertificateGetResponse>> getWithMultipleTags(
            @RequestParam(value = "tag") List<String> tags,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "1") int pageNo) {

        List<GiftCertificateGetResponse> certificates
                = giftCertificateService.searchWithMultipleTags(tags, pageSize, pageNo);
        for (GiftCertificateGetResponse certificate : certificates) {
            addLinkForOrders(certificate);
        }
        BaseResponse<List<GiftCertificateGetResponse>> response = new BaseResponse<>(
                HttpStatus.OK.value(), "certificate list", certificates);

        if (!certificates.isEmpty())
            response.add(linkTo(methodOn(GiftCertificateController.class)
                    .getWithMultipleTags(tags, pageSize, pageNo + 1))
                    .withRel("next page"));

        if (pageNo > 1) {
            response.add(linkTo(methodOn(GiftCertificateController.class)
                    .getWithMultipleTags(tags, pageSize, pageNo - 1))
                    .withRel("previous page"));
        }
        return response;
    }

    @DeleteMapping(value = "/{id}")
    public String delete(
            @PathVariable Long id) {
        int delete = giftCertificateService.delete(id);
        // it returns here integer, because in the service layer I catch for another exception
        //Named BreakingDataRelationshipException,to catch "id not found" exception
        // I had to throw here in the controller, I came to this solution
        if (delete == 1)
            return "gift-certificate deleted successfully";
        throw new DataNotFoundException("Certificate ( id = " + id + " ) not found to delete");


    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GiftCertificateGetResponse> update(
            @PathVariable(value = "id") Long certificateId,
            @Valid @RequestBody GiftCertificateUpdateRequest update,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult);
        }
        GiftCertificateGetResponse response = giftCertificateService.update(update, certificateId);
        addLinkForOrders(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/duration/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GiftCertificateGetResponse> updateDuration(
            @PathVariable Long id,
            @RequestParam String duration) {
        GiftCertificateGetResponse response = giftCertificateService.updateDuration(duration, id);
        addLinkForOrders(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/price/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GiftCertificateGetResponse> updatePrice(
            @PathVariable Long id,
            @RequestParam String price
    ) {
        GiftCertificateGetResponse response = giftCertificateService.updatePrice(price, id);
        addLinkForOrders(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static void addLinkForOrders(GiftCertificateGetResponse certificate) {
        certificate.add(linkTo(methodOn(OrderController.class)
                .getOrdersByCertificate(certificate.getId(), 10, 1))
                .withRel("orders"));
    }

}
