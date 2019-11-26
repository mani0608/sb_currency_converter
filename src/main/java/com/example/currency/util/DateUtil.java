package com.example.currency.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate convertToLocalDate (Object param) {

        //string to date
        return LocalDate.parse(param.toString(), dateTimeFormatter);

    }

}
