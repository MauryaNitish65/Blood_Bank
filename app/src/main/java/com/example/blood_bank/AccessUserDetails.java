package com.example.blood_bank;

public class AccessUserDetails {
    private final String phone,address,bloodgroup,dob,gender,diseases;
    public AccessUserDetails(){
        this.phone=null;
        this.address=null;
        this.bloodgroup=null;
        this.dob=null;
        this.gender=null;
        this.diseases=null;
    }
    public AccessUserDetails(String phone,String address,String bloodgroup,String dob,String gender,String diseases)
    {
        this.phone=phone;
        this.address=address;
        this.bloodgroup=bloodgroup;
        this.dob=dob;
        this.gender=gender;
        this.diseases=diseases;
    }
    public String getPhone(){
        return phone;
    }public String getAddress(){
        return address;
    }public String getBloodgroup(){
        return bloodgroup;
    }public String getDob(){
        return dob;
    }public String getGender(){
        return gender;
    }public String getDiseases(){
        return diseases;
    }





}
