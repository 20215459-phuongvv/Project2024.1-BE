package com.hust.project3.controllers;

import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.ReadingCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class ReadingCardController {
    private final ReadingCardService readingCardService;

    @GetMapping
    public Result getReadingCardByUser(@RequestHeader("Authorization") String jwt) throws NotFoundException {
        return Result.ok(readingCardService.getReadingCardByUser(jwt));
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
