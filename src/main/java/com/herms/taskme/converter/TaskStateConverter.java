package com.herms.taskme.converter;

import com.herms.taskme.enums.TaskState;

import javax.persistence.AttributeConverter;

public class TaskStateConverter implements AttributeConverter<TaskState, String> {

    @Override
    public String convertToDatabaseColumn(TaskState attribute) {
        return attribute.getCode();
    }

    @Override
    public TaskState convertToEntityAttribute(String dbData) {
        return TaskState.toEnum(dbData);
    }
}
