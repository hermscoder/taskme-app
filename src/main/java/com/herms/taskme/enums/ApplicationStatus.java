package com.herms.taskme.enums;

public enum ApplicationStatus {
    PENDING( "P" ),
    ACCEPTED( "A" ),
    DECLINED( "D" ),
	CANCELLED_BY_APPLICANT("C"),
	TASK_CLOSED("T");

    private final String code;

    private ApplicationStatus(String string) {
        this.code = string;
    }

    public String getCode() {
        return code;
    }

    public static ApplicationStatus fromCode(String code) {
        if ( "P".equals(code) || "p".equals(code) ) {
            return PENDING;
        }
        if ( "A".equals(code) || "a".equals(code) ) {
            return ACCEPTED;
        }
        if ( "D".equals(code) || "d".equals(code) ) {
            return DECLINED;
        }
        if ( "C".equals(code) || "c".equals(code) ) {
            return CANCELLED_BY_APPLICANT;
        }
        if ( "T".equals(code) || "t".equals(code) ) {
            return TASK_CLOSED;
        }
		return null;
    }
}
