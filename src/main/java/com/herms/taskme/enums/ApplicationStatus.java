package com.herms.taskme.enums;

public enum ApplicationStatus {
    PENDING( 'P' ),
    ACCEPTED( 'A' ),
    DECLINED( 'D' );

    private final char code;

    private ApplicationStatus(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static ApplicationStatus fromCode(char code) {
        if ( code == 'P' || code == 'p' ) {
            return PENDING;
        }
        if ( code == 'A' || code == 'a' ) {
            return ACCEPTED;
        }
        if ( code == 'D' || code == 'd' ) {
            return DECLINED;
        }
		return null;
    }
}
