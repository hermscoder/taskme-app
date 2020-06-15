package com.herms.taskme.converter;

import javax.persistence.AttributeConverter;

import com.herms.taskme.enums.ApplicationStatus;


public class ApplicationStatusConverter implements AttributeConverter<ApplicationStatus, String> {

	public String convertToDatabaseColumn(ApplicationStatus value) {
	    if ( value == null ) {
	        return null;
	    }
	
	    return value.getCode();
	}

	public ApplicationStatus convertToEntityAttribute(String value) {
	    if ( value == null ) {
	        return null;
	    }
	
	    return ApplicationStatus.fromCode( value );
	}


}
