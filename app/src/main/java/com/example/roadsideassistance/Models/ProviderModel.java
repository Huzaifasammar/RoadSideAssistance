package com.example.roadsideassistance.Models;

public class ProviderModel {
    String imageURL,username,email,password,service,id,device_token,phone_number;

    public ProviderModel(String imageURL, String username, String email, String password, String service, String id,String device_token,String phone_number) {
        this.imageURL = imageURL;
        this.username = username;
        this.email = email;
        this.password = password;
        this.service = service;
        this.id = id;
        this.device_token=device_token;
        this.phone_number=phone_number;

    }
    public ProviderModel(){}


    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
