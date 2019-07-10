package com.tfg.hipersch;

public class ApiResponse {
    private String token;
    private String message;
    private int height;
    private int bodyWeight;
    private double bmi;

    //  Running
    private double vo2max;
    private double MAVvVo2max;
    private double vat;
    private String date;
    //  private double

    public String getToken() {
        return this.token;
    }
    public String getMessage() { return this.message; }

    public int getHeight () { return this.height; }
    public int getBodyWeight() { return this.bodyWeight; }
    public double getBodyMassIndex() { return this.bmi; }

    public double getVo2max() { return this.vo2max; }
    public double getMavVo2max() { return this.MAVvVo2max; }
    public double getVat() { return this.vat; }

    public String getDate() { return this.date; };

    public void setToken(String token) {
        this.token = token;
    }
    public void setMessage(String message) { this.message = message; }
}
