package com.shapovalova.api.core.user;

public class User {
    private int userId;
    private String name;
    private String lastName;
    private String login;
    private String password;
    private String ServiceAddress;

    public User(int userId, String name, String lastName, String login, String password, String serviceAddress) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.ServiceAddress = serviceAddress;
    }

    public User() {
        ServiceAddress = null;
        userId = 0;
        name = null;
        lastName = null;
        login = null;
        password = null;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public String getServiceAddress() {
        return ServiceAddress;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setServiceAddress(String serviceAddress) {
        ServiceAddress = serviceAddress;
    }
}
