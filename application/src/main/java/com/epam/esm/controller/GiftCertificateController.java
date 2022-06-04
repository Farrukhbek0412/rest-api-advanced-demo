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
@RequestMapping("/api/v1/gift_certificate")
@AllArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<GiftCertificateGetResponse>> create(
            @Valid @RequestBody GiftCertificatePostRequest createCertificate,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);

        GiftCertificateGetResponse response = giftCertificateService.create(createCertificate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(HttpStatus.CREATED.value(), "certificate created", response));
    }

    @GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<BaseResponse<GiftCertificateGetResponse>> get(
            @RequestParam Long id
    ) {
        GiftCertificateGetResponse response = giftCertificateService.get(id);
        addLinkForOrders(response);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),
                "gift certificate", response));
    }

    @GetMapping(value = "/get-all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<GiftCertificateGetResponse>>> getAll(
            @RequestParam(required = false, name = "search_word", defaultValue = "") String searchWord,
            @RequestParam(required = false, name = "tag_name", defaultValue = "") String tagName,
            @RequestParam(required = false, name = "do_name_sort") boolean doNameSort,
            @RequestParam(required = false, name = "do_date_sort") boolean doDateSort,
            @RequestParam(required = false, name = "is_descending") boolean isDescending,
            @RequestParam(required = false, name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(required = false, name = "pageNo", defaultValue = "1") int pageNo

    ) {

        if (pageNo < 1 || pageSize < 0)
            throw new InvalidCertificateException("Please enter valid number");
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


        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/get/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<GiftCertificateGetResponse>>> getWithMultipleTags(
            @RequestParam(value = "tag") List<String> tags,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "1") int pageNo) {
        if (pageNo < 1 || pageSize < 0)
            throw new InvalidCertificateException("Please enter valid number");
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
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<BaseResponse> delete(
            @RequestParam Long id) {
        int delete = giftCertificateService.delete(id);
        if (delete == 1)
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "certificate deleted", null));
        throw new DataNotFoundException("Certificate ( id = " + id + " ) not found to delete");
    }

    @PutMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<GiftCertificateGetResponse>> update(
            @Valid @RequestBody GiftCertificateUpdateRequest update,
            BindingResult bindingResult,
            @RequestParam(value = "id") Long certificateId) {
        if (bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);
        GiftCertificateGetResponse response = giftCertificateService.update(update, certificateId);
        addLinkForOrders(response);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(),
                "certificate updated", response));
    }

    @PatchMapping(value = "/update/duration", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<GiftCertificateGetResponse>> updateDuration(
            @RequestParam String duration,
            @RequestParam Long id) {
        GiftCertificateGetResponse response = giftCertificateService.updateDuration(duration, id);
        addLinkForOrders(response);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "duration updated", response));
    }

    @PatchMapping(value = "/update/price", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<GiftCertificateGetResponse>> updatePrice(
            @RequestParam String price,
            @RequestParam Long id) {
        GiftCertificateGetResponse response = giftCertificateService.updatePrice(price, id);
        addLinkForOrders(response);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "price updated", response));
    }

    private static void addLinkForOrders(GiftCertificateGetResponse certificate) {
        certificate.add(linkTo(methodOn(OrderController.class)
                .getOrdersByCertificate(certificate.getId(), 10, 1))
                .withRel("orders"));
    }

}
