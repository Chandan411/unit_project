package com.example.unitproject;

import java.util.Date;

public class Details {
    private String name;
    private String address;
    private Date dob;
    private long mobile;
    private int personal_number;
    

    public Details() {

    }

    public Details(String name, int personal_number, long mobile, Date dob, String address) {
        this.name = name;
        this.personal_number = personal_number;
        this.mobile = mobile;
        this.dob = dob;
        this.address = address;


    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Date getDob() {
        return dob;
    }

    public int getPersonal_number() {
        return personal_number;
    }

    public long getMobile() {
        return mobile;
    }
}
