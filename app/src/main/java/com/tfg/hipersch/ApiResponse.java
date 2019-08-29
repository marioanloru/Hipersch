package com.tfg.hipersch;

import java.util.ArrayList;

public class ApiResponse {
    private String token;
    private String role;
    private String message;
    private int height;
    private int bodyWeight;
    private double bmi;
    private ArrayList<String> athletes;

    private String trainingZone;
    private String trainingZoneTag;
    private String trainingZoneTwoHundred;
    private String trainingZoneFourHundred;
    private String testId;
    private String email;

    //  Cycling
    private float p6sec;
    private float p1min;
    private float p6min;
    private float p20min;
    private String type;

    //  Running
    private double vo2max;
    private double MAVvVo2max;
    private double vat;
    private String date;

    //  Swimming
    private double indexLT;
    private double indexANAT;
    private double anaThreshold;
    private double lactateThreshold;
    //  private double

    public String getToken() {
        return this.token;
    }
    public String getMessage() { return this.message; }
    public String getUserRole() { return this.role; }
    public String getEmail() { return this.email; };
    //public ArrayList<String> getAthletes() { return this.athletes; }

    public int getHeight () { return this.height; }
    public int getBodyWeight() { return this.bodyWeight; }
    public double getBodyMassIndex() { return this.bmi; }

    public String getTrainingZone() { return this.trainingZone; }
    public String getTrainingZoneTwoHundred() { return this.trainingZoneTwoHundred; }
    public String getTrainingZoneFourHundred() { return this.trainingZoneFourHundred; }
    public String getTrainingZoneTag() { return this.trainingZoneTag; }


    //  Cycling
    public float getP6sec() { return this.p6sec; }
    public float getP1min() { return this.p1min; }
    public float getP6min() { return this.p6min; }
    public float getP20min() { return this.p20min; }
    public String getType() { return this.type; };

    //  Running
    public double getVo2max() { return this.vo2max; }
    public double getMavVo2max() { return this.MAVvVo2max; }
    public double getVat() { return this.vat; }

    //  Swimming
    public double getIndexLT() { return this.indexLT; }
    public double getIndexANAT() { return this.indexANAT; }
    public double getAnaThreshold() { return this.anaThreshold; }
    public double getLactateThreshold() { return this.lactateThreshold; }

    public String getTestId() { return this.testId; }
    public String getDate() { return this.date; };

    public void setToken(String token) {
        this.token = token;
    }
    public void setMessage(String message) { this.message = message; }
}
