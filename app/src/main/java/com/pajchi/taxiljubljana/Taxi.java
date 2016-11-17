package com.pajchi.taxiljubljana;

/**
 * Created by Matic on 2.11.2016.
 */

public class Taxi {

    private String city;
    private String name;
    private String phone;
    private String startFee;
    private String regularKm;
    private String contractKm;
    private String randomKm;
    private String waitingHour;
    private String regular10Km;
    private String random10Km;

    public Taxi(String city, String name, String phone, String startFee, String regularKm,
                String contractKm, String randomKm, String waitingHour, String regular10Km,
                String random10Km) {
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.startFee = startFee;
        this.regularKm = regularKm;
        this.contractKm = contractKm;
        this.randomKm = randomKm;
        this.waitingHour = waitingHour;
        this.regular10Km = regular10Km;
        this.random10Km = random10Km;
    }

    @Override
    public String toString() {
        return String.format("[%s; %s; %s; %s; %s; %s; %s; %s; %s; %s]", city, name, phone,
                startFee, regularKm, contractKm, randomKm, waitingHour, regular10Km, random10Km);
    }

    public String getName() {
        return name;
    }

    public String getRandom10Km() {
        return random10Km;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getStartFee() {
        return startFee;
    }

    public String getRegularKm() {
        return regularKm;
    }

    public String getContractKm() {
        return contractKm;
    }

    public String getRandomKm() {
        return randomKm;
    }

    public String getWaitingHour() {
        return waitingHour;
    }

    public String getRegular10Km() {
        return regular10Km;
    }
}
