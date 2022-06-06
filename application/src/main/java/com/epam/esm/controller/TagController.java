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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/tags")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping(value = "",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TagGetResponse> create(
            @Valid @RequestBody TagPostRequest tag,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult);
        }
        TagGetResponse response = tagService.create(tag);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<TagGetResponse>> get(
            @PathVariable Long id) {
        TagGetResponse response = tagService.get(id);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(),
                "success", response));
    }

    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResponse<List<TagGetResponse>> getAll(
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNo) {

        List<TagGetResponse> allTags = tagService.getAll(pageSize, pageNo);
        BaseResponse<List<TagGetResponse>> response = new BaseResponse<>(
                HttpStatus.OK.value(), "tag list", allTags);

        if (!allTags.isEmpty())
            response.add(linkTo(methodOn(TagController.class)
                    .getAll(pageSize, pageNo + 1))
                    .withRel("next page"));

        if (pageNo > 1) {
            response.add(linkTo(methodOn(TagController.class)
                    .getAll(pageSize, pageNo - 1))
                    .withRel("previous page"));
        }
        return response;
    }

    @DeleteMapping(value = "/{id}")
    public String delete(
            @PathVariable Long id) {
        tagService.delete(id);
        return "tag deleted successfully";
    }

    @GetMapping(value = "/most-used")
    public MostUsedTagResponse getMostUsed() {
        MostUsedTagResponse mostWidelyUsedTagsOfUser = tagService.getMostWidelyUsedTagsOfUser();
        return mostWidelyUsedTagsOfUser;
    }

}
