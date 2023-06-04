package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.constant.Currency;
import com.slinkdigital.wedding.domain.GiftAndSupport;
import com.slinkdigital.wedding.domain.Payment;
import com.slinkdigital.wedding.dto.CardPaymentCharge;
import com.slinkdigital.wedding.dto.CardPaymentCharger;
import com.slinkdigital.wedding.dto.PaymentRequest;
import com.slinkdigital.wedding.mapper.GSMapper;
import com.slinkdigital.wedding.repository.PaymentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class PaymentService {

    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD, Currency.GBP);
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;
    private final GSMapper gsMapper;

    public void chargeCard(PaymentRequest paymentRequest) {
        // 1. Does customer exists if not throw
//        boolean isCustomerFound = customerRepository.findById(customerId).isPresent();
//        if (!isCustomerFound) {
//            throw new IllegalStateException(String.format("Customer with id [%s] not found", customerId));
//        }

        // 2. Do we support the currency if not throw
        boolean isCurrencySupported = ACCEPTED_CURRENCIES.contains(paymentRequest.getCurrency());

        if (!isCurrencySupported) {
            String message = String.format(
                    "Currency [%s] not supported",
                    paymentRequest.getCurrency());
            throw new IllegalStateException(message);
        }

        // 3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getSource(),
                paymentRequest.getAmount(),
                paymentRequest.getCurrency(),
                paymentRequest.getDescription()
        );

        // 4. If not debited throw
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(String.format("Card not debited for customer"));
        }

        // 5. Insert payment
//        paymentRequest.getPayment().setCustomerId(customerId);
        GiftAndSupport giftAndSupport = gsMapper.mapDtoToGiftAndSupport(paymentRequest.getGiftAndSupportDto());
        Payment payment = Payment.builder()
                .amount(paymentRequest.getAmount())
                .currency(paymentRequest.getCurrency())
                .description(paymentRequest.getDescription())
                .source(paymentRequest.getSource())
                .giftAndSupport(giftAndSupport)
                .build();
        paymentRepository.save(payment);
        // 6. TODO: send sms
    }

}
