package com.accounts.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PeselValidator implements ConstraintValidator<AdultByPesel, String> {
    @Override
    public void initialize(AdultByPesel constraintAnnotation) {
    }

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext constraintValidatorContext) {
        return isValidAdult(pesel);
    }

    protected boolean isValidAdult(String pesel) {
        String dateString = getYear(pesel)+"-"+getMonth(pesel)+"-"+getDay(pesel);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date birthDate = sdf.parse(dateString);
            LocalDate from = LocalDate.from(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            return LocalDate.now().minusYears(18).isAfter(from);
        } catch (ParseException e) {
            return false;
        }
    }

    private int getYear(String pesel) {
        int decade = Integer.parseInt(pesel.substring(0,2));
        int month = Integer.parseInt(pesel.substring(2,4));
        return month > 20 ? 2000 + decade: 1900 + decade;
    }

    private int getMonth(String pesel) {
        int month = Integer.parseInt(pesel.substring(2,4));
        return month > 20 ? month - 20: month;
    }

    private int getDay(String pesel) {
        return Integer.parseInt(pesel.substring(4,6));
    }

}
