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
@RequestMapping("/api/subscription")
public class ReadingCardController {
    private final ReadingCardService readingCardService;

    @GetMapping
    public Result getReadingCardByUser(@RequestHeader("Authorization") String jwt,
                                       ReadingCardRequestDTO dto,
                                       PagingRequestDTO pagingRequestDTO) throws NotFoundException {
        Page<ReadingCard> page = readingCardService.getReadingCardByUser(jwt, dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getUserReadingCardById(@RequestHeader("Authorization") String jwt,
                                              @PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(readingCardService.getUserReadingCardById(jwt, id));
    }

    @PostMapping("/register")
    public Result addReadingCard(@RequestHeader("Authorization") String jwt,
                                      @RequestBody @Valid ReadingCardRequestDTO dto) throws NotFoundException {
        return Result.ok(readingCardService.addReadingCard(jwt, dto));
    }

    @PostMapping("/payment")
    public Result addPayment(@RequestHeader("Authorization") String jwt,
                                  @RequestBody @Valid ReadingCardRequestDTO dto) {
        return Result.ok(readingCardService.addPayment(jwt, dto));
    }

    @PutMapping("/renew")
    public Result renewReadingCard(@RequestHeader("Authorization") String jwt,
                                        @RequestBody @Valid ReadingCardRequestDTO dto) throws NotFoundException {
        return Result.ok(readingCardService.renewReadingCard(jwt, dto));
    }

    @DeleteMapping("/cancel")
    public Result cancelReadingCard(@RequestHeader("Authorization") String jwt,
                                         @RequestBody @Valid ReadingCardRequestDTO dto) throws NotFoundException {
        return Result.ok(readingCardService.cancelReadingCard(jwt, dto));
    }
}
