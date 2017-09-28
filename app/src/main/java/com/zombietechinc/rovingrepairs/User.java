package com.zombietechinc.rovingrepairs;

/**
 * Created by User on 8/29/2017.
 */

public class User {
    String name;
    String address;
    String contactnumber;
    String lastName;
    String emailAddress;
    String bookeoId;


    public String getBookeoId() {
        return bookeoId;
    }

    public void setBookeoId(String bookeoId) {
        this.bookeoId = bookeoId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public User (String name, String address, String contactnumber){
        this.name = name;
        this.address = address;
        this.contactnumber = contactnumber;

    }
    public User (String name, String lastName, String address, String contactnumber){
        this.name = name;
        this.address = address;
        this.contactnumber = contactnumber;
        this.lastName = lastName;

    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public User (String name, String lastName, String address, String contactnumber, String emailAddress){
        this.name = name;
        this.address = address;
        this.contactnumber = contactnumber;
        this.lastName = lastName;
        this.emailAddress = emailAddress;

    }
    public User (String name, String lastName, String address, String contactnumber, String emailAddress, String bookeoId){
        this.name = name;
        this.address = address;
        this.contactnumber = contactnumber;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.bookeoId = bookeoId;

    }
    public User(){

    }

}
