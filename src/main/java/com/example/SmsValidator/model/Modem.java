package com.example.SmsValidator.model;


import com.example.SmsValidator.entity.ModemEntity;

public class Modem {
    private Long id;
    private String phoneNumber;
    private String IMSI;
    private String ICCID;

    public static Modem toModel(ModemEntity modemEntity) {
        Modem modem = new Modem();
        modem.setId(modemEntity.getId());
        modem.setPhoneNumber(modemEntity.getPhoneNumber());
        modem.setIMSI(modemEntity.getIMSI());
        modem.setICCID(modemEntity.getICCID());
        return modem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

    public String getICCID() {
        return ICCID;
    }

    public void setICCID(String ICCID) {
        this.ICCID = ICCID;
    }

    private Modem() {
    }
}
