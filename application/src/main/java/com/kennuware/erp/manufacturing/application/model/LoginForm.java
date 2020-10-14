package com.kennuware.erp.manufacturing.application.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class LoginForm {

    @NotNull
    @NotBlank
    @NotEmpty
    private String user;

    @NotNull
    @NotBlank
    @NotEmpty
    private String pass;

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "Username: " + user + ", Pass: " + pass;
    }
}
