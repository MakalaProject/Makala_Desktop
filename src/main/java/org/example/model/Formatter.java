package org.example.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm:ss a");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static String FormatDateTime(LocalDateTime localDateTime){
        return dateTimeFormatter.format(localDateTime);
    }

    public static String FormatDate(LocalDate localDate){
        return dateFormatter.format(localDate);
    }
}
