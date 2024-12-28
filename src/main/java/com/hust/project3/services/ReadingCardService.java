package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.payment.PaymentDTO;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

public interface ReadingCardService {
    ReadingCard getReadingCardByUser(String jwt) throws NotFoundException;

    ReadingCard addReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException;

    ReadingCard renewReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException;

    ReadingCard cancelReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException;

    Page<ReadingCard> getAllReadingCards(ReadingCardRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    ReadingCard getReadingCardById(Long id) throws NotFoundException;

    ReadingCard warnReadingCard(Long id) throws NotFoundException;

    ReadingCard toggleReadingCardStatus(Long id) throws NotFoundException;

    PaymentDTO.VNPayResponse requestPayment(ReadingCardRequestDTO dto);
}
