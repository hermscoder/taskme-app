package com.herms.taskme.util;

import java.time.LocalDate;
import java.util.Date;

public class Converter {
    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
}
