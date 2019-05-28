package com.beyond_tech.elwensh.models;

public class Driver {
    private String driverEmail;
    private String driverPassword;
    private String driverFireId;
    private boolean driverStatus;

    public Driver(String driverEmail, String driverFireId, boolean driverStatus) {
        this.driverEmail = driverEmail;
        this.driverFireId = driverFireId;
        this.driverStatus = driverStatus;
    }

    public Driver() {

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
