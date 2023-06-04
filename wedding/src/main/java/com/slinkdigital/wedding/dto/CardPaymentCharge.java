package com.slinkdigital.wedding.dto;

/**
 *
 * @author TEGA
 */
public class CardPaymentCharge {
    
    private final boolean isCardDebited;

    public CardPaymentCharge(boolean isCardDebited) {
        this.isCardDebited = isCardDebited;
    }

    public boolean isCardDebited() {
        return isCardDebited;
    }

    @Override
    public String toString() {
        return "CardPaymentCharge{" +
                "isCardDebited=" + isCardDebited +
                '}';
    }
    
}
