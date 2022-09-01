package com.example.demo.service.Request;

public class LoginRequest {
    private String principal;

    private String credential;


    public LoginRequest() {
    }

    public LoginRequest(String principal, String credential) {
        this.principal = principal;
        this.credential = credential;
    }
    public String getPrincipal() {return principal;}

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCredential() {return credential;}

    public void setCredential(String credential) {this.credential = credential;}
}
