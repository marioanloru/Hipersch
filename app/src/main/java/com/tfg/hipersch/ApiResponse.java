package com.tfg.hipersch;

public class ApiResponse {
    private String token;
    private String message;
    public String getToken() {
        return this.token;
    }
    public String getMessage() { return this.message; }

    public void setToken(String token) {
        this.token = token;
    }
    public void setMessage(String message) { this.message = message; }
}
