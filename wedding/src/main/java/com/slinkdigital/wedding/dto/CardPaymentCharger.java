package com.slinkdigital.wedding.dto;

import com.slinkdigital.wedding.constant.Currency;
import java.math.BigDecimal;

/**
 *
 * @author TEGA
 */
public interface CardPaymentCharger {
    
    CardPaymentCharge chargeCard(
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description
    );
    
}
