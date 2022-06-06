package com.epam.esm.service.utils;

import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component()
public class DataValidatorImpl implements DataValidator{
    @Override
    public void validatePriceExist(String price) {
        if (StringUtils.isBlank(price)) {
            throw new InvalidCertificateException("Enter price first!");
        }
    }

    @Override
    public void validatePriceNumeric(String price) {
        try {
            Double priceValue = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            throw new InvalidCertificateException("Do not enter String (" +
                    price + " )");
        }
    }

    @Override
    public void validatePricePositiveOrZero(String price) {
            Double priceValue = Double.parseDouble(price);
            if (priceValue < 0) {
                throw new InvalidCertificateException(
                        "The price ( " + priceValue + " ) can not be negative");
            }

    }

    @Override
    public void validateDurationExist(String duration) {
        if (StringUtils.isBlank(duration)) {
            throw new InvalidCertificateException("Enter duration first!");
        }
    }

    @Override
    public void validateDurationNumeric(String duration) {
        try {
            Integer durationCheck = Integer.parseInt(duration);
        } catch (NumberFormatException e) {
            throw new InvalidCertificateException("Do not enter String (" + duration + " )");
        }
    }

    @Override
    public void validateDurationPositive(String duration) {
            Integer durationCheck = Integer.parseInt(duration);
            if (durationCheck <= 0) {
                throw new InvalidCertificateException(
                        "The duration ( " + duration + " ) must be positive");
            }
    }

    @Override
    public void validateAgeNumeric(String age) {
        try {
            Integer ageCheck = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            throw new InvalidCertificateException("Do not enter String (" + age + " )");
        }
    }

    @Override
    public void isAgeOlderThan18(String age) {
        Integer durationCheck = Integer.parseInt(age);
        if (durationCheck <18) {
            throw new InvalidCertificateException(
                    "User under 18 years old cannot use our system");
        }
    }
}
