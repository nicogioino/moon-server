package com.example.demo.dto.user;

public class UserUpdatePasswordDTO {
    private String oldPassword;
    private  String token;

    //@ValidPassword A password validator could be implemented if there is a restriction
    private String newPassword;

    public UserUpdatePasswordDTO() {}

    public void setPassword(String password) {this.newPassword = password;}

    public void setToken(String token) {this.token = token;}

    public String getToken() {return token;}

    public String getNewPassword() {return newPassword;}

    public String getOldPassword() {return oldPassword;}
}
