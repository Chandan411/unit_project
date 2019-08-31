package com.example.unitproject;

public class Details {
    private String name, address, dob, mobile;
    private int personal_number;

    public Details(String name, int personal_number, String mobile, String dob, int address) {

    }

    public Details(String name, int personal_number, String mobile, String dob, String address) {
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.personal_number = personal_number;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public int getPersonal_number() {
        return personal_number;
    }

    public String getMobile() {
        return mobile;
    }
}
