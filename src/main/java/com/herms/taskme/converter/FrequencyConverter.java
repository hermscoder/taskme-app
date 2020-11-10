package com.herms.taskme.converter;

import com.herms.taskme.enums.FrequencyEnum;
import com.herms.taskme.enums.TaskState;

import javax.persistence.AttributeConverter;

public class FrequencyConverter implements AttributeConverter<FrequencyEnum, String> {

    @Override
    public String convertToDatabaseColumn(FrequencyEnum attribute) {
        if(attribute != null) {
            return attribute.getCode();
        }
        return null;
    }

    @Override
    public FrequencyEnum convertToEntityAttribute(String dbData) {
        if(dbData != null) {
            return FrequencyEnum.toEnum(dbData);
        }
        return null;
    }
}
