package com.herms.taskme.util;

import com.herms.taskme.enums.FrequencyEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    public static Date nextDateAccordingToFrequency(Date date, FrequencyEnum frequency) {
        Date resultDate;

        LocalDate localDate = Converter.convertToLocalDateViaSqlDate(date);
        if(FrequencyEnum.DAILY.equals(frequency)){
            localDate = localDate.plusDays(1);
        } else if(FrequencyEnum.MONDAYFRIDAY.equals(frequency)){
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            //if it's friday, we need to skip the weekend
            if(DayOfWeek.FRIDAY.equals(dayOfWeek)){
                localDate = localDate.plusDays(3);
            } else if(DayOfWeek.SATURDAY.equals(dayOfWeek)) {
                localDate = localDate.plusDays(2);
            //if it's sunday or a week day(but friday), we just add one day
            } else {
                localDate = localDate.plusDays(1);
            }
        } else if(FrequencyEnum.WEEKLY.equals(frequency)){
            localDate = localDate.plusWeeks(1);
        } else if(FrequencyEnum.MONTHLY.equals(frequency)){
            localDate = localDate.plusMonths(1);
        } else if(FrequencyEnum.YEARLY.equals(frequency)){
            localDate = localDate.plusYears(1);
        }

        resultDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return resultDate;
    }

}
