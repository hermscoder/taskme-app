package com.herms.taskme.enums;

public enum FrequencyEnum {
    DAILY("D"), MONDAYFRIDAY("MF"), WEEKLY("W"), MONTHLY("M"), YEARLY("Y");

    private String code;

    FrequencyEnum(String code){
        this.code = code;
    }

    public static FrequencyEnum toEnum(String code){
        for(FrequencyEnum frequency : values()){
            if(frequency.getCode().equals(code)) {
                return frequency;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
