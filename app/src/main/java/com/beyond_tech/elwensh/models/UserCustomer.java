package com.beyond_tech.elwensh.models;

import com.beyond_tech.elwensh.interfaces.User;

public class UserCustomer {

    private String userEmailAddress;
    private String userPassword;
    private String userFireId;
    //    private boolean driverStatus;
    private String userPhoneNumber;
    private String userFullName;

    public UserCustomer() {
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFireId() {
        return userFireId;
    }

    public void setUserFireId(String userFireId) {
        this.userFireId = userFireId;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
