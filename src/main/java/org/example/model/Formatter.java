package org.example.model;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    private static SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy h:mm:ss a");

    public static String FormatDateTime(LocalDateTime localDateTime){
        return formatter6.format(localDateTime);
    }
}
