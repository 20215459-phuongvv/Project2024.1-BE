package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.ReadingCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reading-cards")
public class AdminReadingCardController {

    private final ReadingCardService readingCardService;

    @GetMapping
    public Result getAllReadingCards(ReadingCardRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Page<ReadingCard> page = readingCardService.getAllReadingCards(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getReadingCardById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(readingCardService.getReadingCardById(id));
    }

    @PutMapping("/{id}/warn")
    public Result warnReadingCard(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(readingCardService.warnReadingCard(id));
    }

    @PutMapping("/{id}/ban")
    public Result banReadingCard(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(readingCardService.toggleReadingCardStatus(id));
    }

    @PutMapping("/{id}/unban")
    public Result unbanReadingCard(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(readingCardService.toggleReadingCardStatus(id));
    }
}
