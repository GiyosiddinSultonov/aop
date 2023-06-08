package uz.najottalim.javan6.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.najottalim.javan6.validation.customervalidations.PINFL;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PINFLValidator implements ConstraintValidator<PINFL, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() != 16) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        String date = value.substring(1, 7);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
            LocalDate.parse(date, formatter);
        } catch (Exception ex) {
            return false;
        }
        // TODO continue next logic
        return true;
    }
}
