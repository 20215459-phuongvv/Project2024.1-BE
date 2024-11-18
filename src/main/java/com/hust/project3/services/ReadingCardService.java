package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

public interface ReadingCardService {
    Page<ReadingCard> getReadingCardByUser(String jwt, ReadingCardRequestDTO dto, PagingRequestDTO pagingRequestDTO) throws NotFoundException;

    ReadingCard getUserReadingCardById(String jwt, Long id) throws NotFoundException;

    ReadingCard addReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException;

    ReadingCard addPayment(String jwt, ReadingCardRequestDTO dto);

    ReadingCard renewReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException;

    ReadingCard cancelReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException;
}
