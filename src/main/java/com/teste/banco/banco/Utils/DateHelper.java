package com.teste.banco.banco.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateHelper {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public String getCurrentDateFormatted() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    public String getCurrentDateFormatted(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
}