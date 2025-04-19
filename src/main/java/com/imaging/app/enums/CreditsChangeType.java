package com.imaging.app.enums;

import com.imaging.app.exception.CustomIllegalArgumentException;

public enum CreditsChangeType {
    PURCHASE,
    REFUND,
    IMAGE_GENERATION,
    BONUS;

    public static CreditsChangeType from(String value) {
        try {
            return CreditsChangeType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new CustomIllegalArgumentException(ex.getMessage());
        }
    }
}
