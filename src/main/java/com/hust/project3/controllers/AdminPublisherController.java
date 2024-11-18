package com.hust.project3.controllers;

import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.publisher.PublisherRequestDTO;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/admin/publishers")
public class AdminPublisherController {
    private final PublisherService publisherService;

    @PostMapping
    public Result addPublisher(@RequestHeader("Authorization") String jwt,
                               @RequestBody @Valid PublisherRequestDTO dto) {
        return Result.ok(publisherService.addPublisher(jwt, dto));
    }

    @PutMapping
    public Result updatePublisher(@RequestHeader("Authorization") String jwt,
                                  @RequestBody @Valid PublisherRequestDTO dto) throws NotFoundException {
        return Result.ok(publisherService.updatePublisher(jwt, dto));
    }

    @DeleteMapping
    public Result deletePublishers(@RequestHeader("Authorization") String jwt,
                                   @RequestBody List<Long> idList) throws NotFoundException {
        return Result.ok(publisherService.deletePublishers(jwt, idList));
    }
}
