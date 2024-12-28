package com.hust.project3.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "payment_transactions")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "email")
    private String email;

    @Column(name = "reading_card_code")
    private String readingCardCode;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "status")
    private String status;
}
