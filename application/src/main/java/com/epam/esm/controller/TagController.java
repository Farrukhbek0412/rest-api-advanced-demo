package com.epam.esm.controller;


import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.response.MostUsedTagResponse;
import com.epam.esm.dto.response.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.exception.InvalidInputException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.service.tag.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/tag")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping(value = "/create",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<TagGetResponse>> create(
            @Valid @RequestBody TagPostRequest tag,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);
        TagGetResponse response = tagService.create(tag);
        return ResponseEntity.status(201).body(new BaseResponse<>(201, "tag created", response));
    }

    @GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<TagGetResponse>> get(
            @RequestParam Long id) {
        TagGetResponse response = tagService.get(id);
        return ResponseEntity.ok(new BaseResponse<>(200, "success", response));
    }

    @GetMapping(value = "/get_all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<TagGetResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNo) {


        if (pageNo < 1 || pageSize < 0)
            throw new InvalidCertificateException("Please enter valid number");
        List<TagGetResponse> allTags = tagService.getAll(pageSize, pageNo);
        BaseResponse<List<TagGetResponse>> response = new BaseResponse<>(
                200, "tag list", allTags);

        if (!allTags.isEmpty())
            response.add(linkTo(methodOn(TagController.class)
                    .getAll(pageSize, pageNo + 1))
                    .withRel("next page"));

        if (pageNo > 1) {
            response.add(linkTo(methodOn(TagController.class)
                    .getAll(pageSize, pageNo - 1))
                    .withRel("previous page"));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<BaseResponse> delete(
            @RequestParam Long id) {
        int delete = tagService.delete(id);
        if (delete == 1)
            return ResponseEntity.status(200).body(new BaseResponse(204, "tag deleted", null));
        throw new DataNotFoundException("tag  ( id = " + id + " ) not found to delete");
    }

    @GetMapping(value = "/getMostUsed")
    public ResponseEntity<BaseResponse<MostUsedTagResponse>> getMostUsed() {
        MostUsedTagResponse mostWidelyUsedTagsOfUser = tagService.getMostWidelyUsedTagsOfUser();
        return ResponseEntity.ok(new BaseResponse<>(200, "tag", mostWidelyUsedTagsOfUser));
    }

}
