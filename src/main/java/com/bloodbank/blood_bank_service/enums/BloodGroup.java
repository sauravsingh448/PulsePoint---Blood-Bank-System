package com.bloodbank.blood_bank_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BloodGroup {
    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-");

    private final String display;

    BloodGroup(String display) {  // display like A+
        this.display = display;
    }

    @JsonValue
    public String getDisplay(){
        return display;
    }

    // fronted se kya ayga
    @JsonCreator
    public static BloodGroup from(String value){
        // fronted give the null
        if(value == null) return null;

        String v = value.trim().toUpperCase(); // input like a+, A+

        try{
            return BloodGroup.valueOf(v);
        }catch (Exception ignored) {}

        // symbol matching
        for(BloodGroup bloodgroup : values()){
            if(bloodgroup.display.equalsIgnoreCase(v)){
                return bloodgroup;
            }
        }
        throw new IllegalArgumentException("Invalid blood Group :"+ value);
    }
}
