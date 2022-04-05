package com.playtomic.tests.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playtomic.tests.wallet.enums.PaymentProviderType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(name = "payment_provider_id")
    private String paymentProviderId;

    @Column(name = "amount")
    @Builder.Default
    private BigDecimal amount = BigDecimal.valueOf(0);

    @Column(name = "is_refunded")
    @Builder.Default
    private Boolean isRefunded = false;

    @Column(name = "payment_provider")
    @Enumerated(EnumType.STRING)
    private PaymentProviderType paymentProvider;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

}