package com.example.SmsValidator.model;

public class ModemState {
    private String IMSI;
    private String phoneNumber;
    private String port;


    public ModemState() {
    }

    public boolean isValid() {
        return this.phoneNumber != null && this.IMSI != null;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
