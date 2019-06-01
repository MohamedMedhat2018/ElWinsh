package com.beyond_tech.elwensh.models;

import com.beyond_tech.elwensh.factory.UserFactory;

public class UserDriver  {
    private String driverEmail;
    private String driverPassword;
    private String driverFireId;
    private boolean driverStatus;
    private String phoneNumber;
    private String fullName;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserDriver(String driverEmail, String driverFireId, boolean driverStatus) {
        this.driverEmail = driverEmail;
        this.driverFireId = driverFireId;
        this.driverStatus = driverStatus;
    }

    public UserDriver() {

    }

    public String getDriverFireId() {
        return driverFireId;
    }

    public void setDriverFireId(String driverFireId) {
        this.driverFireId = driverFireId;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }

    public boolean isDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(boolean driverStatus) {
        this.driverStatus = driverStatus;
    }
}
