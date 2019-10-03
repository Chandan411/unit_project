package com.example.unitproject;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Details {
    private String name;
    private String address;
    private @ServerTimestamp
    Date dob;
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


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }


    public void setDob(Date dob) {
        this.dob = dob;
    }
    public Date getDob() {
        return dob;
    }


    public void setPersonal_number(int personal_number) {
        this.personal_number = personal_number;
    }
    public int getPersonal_number() {
        return personal_number;
    }


    public void setMobile(long mobile) {
        this.mobile = mobile;
    }
    public long getMobile() {
        return mobile;
    }
}
