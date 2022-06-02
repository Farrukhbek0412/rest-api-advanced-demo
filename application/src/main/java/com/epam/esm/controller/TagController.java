package com.epam.esm.controller;


import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.response.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.exception.InvalidInputException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.service.tag.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);
        TagGetResponse response = tagService.create(tag);
        return ResponseEntity.status(201).body(new BaseResponse<>(201, "tag created", response));
    }

    @GetMapping(value = "/get",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<TagGetResponse>> get(
            @RequestParam Long id
    ){
        TagGetResponse response = tagService.get(id);
        return ResponseEntity.ok(new BaseResponse<>(200, "success", response));
    }

    @GetMapping(value = "/get_all",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<TagGetResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "5") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset
    ){
        List<TagGetResponse> allTags = tagService.getAll(limit, offset);
        return ResponseEntity.ok(new BaseResponse<>(200, "tag list", allTags));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<BaseResponse> delete(
            @RequestParam Long id
    ){
        int delete = tagService.delete(id);
        if(delete == 1)
            return ResponseEntity.status(200).body(new BaseResponse(204, "tag deleted", null));
        throw new DataNotFoundException("tag  ( id = " + id+ " ) not found to delete");
    }

    @GetMapping(value = "/getMostUsed")
    public ResponseEntity<BaseResponse<List<TagGetResponse>>> getMostUsed(
            @RequestParam(name = "id") Long userId
    ){
        List<TagGetResponse> mostWidelyUsedTagsOfUser = tagService.getMostWidelyUsedTagsOfUser(userId);
        return ResponseEntity.ok(new BaseResponse<>(200, "tag", mostWidelyUsedTagsOfUser));
    }
}
