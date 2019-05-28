package com.tfg.hipersch;

public class ApiResponse {
    private String token;
    private String message;
    private int height;
    private int bodyWeight;
    private double bmi;

    public String getToken() {
        return this.token;
    }
    public String getMessage() { return this.message; }

    public int getHeight () { return this.height; }
    public int getBodyWeight() { return this.bodyWeight; }
    public double getBodyMassIndex() { return this.bmi; }

    public void setToken(String token) {
        this.token = token;
    }
    public void setMessage(String message) { this.message = message; }
}
