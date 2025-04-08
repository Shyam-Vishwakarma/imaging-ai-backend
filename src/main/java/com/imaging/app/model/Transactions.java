package com.imaging.app.model;

import com.imaging.app.enums.PaymentMethod;
import com.imaging.app.enums.TransactionStatus;
import com.imaging.app.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String transactionId;
    private String userId;
    @Setter
    private TransactionStatus status;
    private PaymentMethod paymentMethod;
    private TransactionType transactionType;
    private double amount;
    @Setter
    private String referenceId;
    private String description;
    private LocalDateTime createdAt;
    @Setter
    private LocalDateTime updatedAt;
}
