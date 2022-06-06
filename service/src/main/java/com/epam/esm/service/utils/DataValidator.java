package com.epam.esm.service.utils;

public interface DataValidator {

    void validatePriceExist(String price);
    void validatePriceNumeric(String price);
    void validatePricePositiveOrZero(String price);
    void validateDurationExist(String duration);
    void validateDurationNumeric(String duration);
    void validateDurationPositive(String duration);

    void validateAgeNumeric(String age);
    void isAgeOlderThan18(String age);
}
