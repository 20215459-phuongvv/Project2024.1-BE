package com.hust.project3.services;

import com.hust.project3.dtos.payment.PaymentDTO;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentDTO.VNPayResponse createVnPayPayment(ReadingCardRequestDTO dto);
}
