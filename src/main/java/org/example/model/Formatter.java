package org.example.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class Formatter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm:ss a");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter monthSpecialFormatter = new DateTimeFormatterBuilder()
            // case insensitive
            .parseCaseInsensitive()
            // pattern with full month name (MMMM)
            .appendPattern("MMMM yyyy")
            // set locale
            .toFormatter(new Locale("es", "ES")
            );;

    public static String FormatDateTime(LocalDateTime localDateTime){
        return dateTimeFormatter.format(localDateTime);
    }

    public static String FormatDate(LocalDate localDate){
        return dateFormatter.format(localDate);
    }

    public static String FormatMonth(LocalDate localDate){
        return monthSpecialFormatter.format(localDate);
    }

}
