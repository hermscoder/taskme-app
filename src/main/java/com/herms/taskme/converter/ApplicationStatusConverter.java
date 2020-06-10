package com.herms.taskme.converter;

import javax.persistence.AttributeConverter;

import com.herms.taskme.enums.ApplicationStatus;


public class ApplicationStatusConverter implements AttributeConverter<ApplicationStatus, Character> {

	public Character convertToDatabaseColumn(ApplicationStatus value) {
	    if ( value == null ) {
	        return null;
	    }
	
	    return value.getCode();
	}

	public ApplicationStatus convertToEntityAttribute(Character value) {
	    if ( value == null ) {
	        return null;
	    }
	
	    return ApplicationStatus.fromCode( value );
	}


}
