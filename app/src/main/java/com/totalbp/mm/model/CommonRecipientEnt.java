package com.totalbp.mm.model;

/**
 * Created by Ezra
 */

public class CommonRecipientEnt {


    public  String parameter;
    public  String value;

    public  String value2;
    public  String value3;
    public  String value4;
    public  String value5;
    public  String value6;
    public  String value7;
    public  String value8;
    public  String value9;

    public CommonRecipientEnt() {
    }

    public String getValue9() {
        return value9;
    }

    public void setValue9(String value9) {
        this.value9 = value9;
    }

    public CommonRecipientEnt(String parameter, String value, String value2, String value3, String value4, String value5,
                              String value6, String value7, String value8, String value9) {
        this.parameter = parameter;
        this.value = value;

        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.value9 = value9;
    }


    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public String getValue5() {
        return value5;
    }

    public void setValue5(String value5) {
        this.value5 = value5;
    }

    public String getValue6() {
        return value6;
    }

    public void setValue6(String value6) {
        this.value6 = value6;
    }

    public String getValue7() {
        return value7;
    }

    public void setValue7(String value7) {
        this.value7 = value7;
    }

    public String getValue8() {
        return value8;
    }

    public void setValue8(String value8) {
        this.value8 = value8;
    }



    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return parameter;
    }
}

