package com.example.roadsideassistance.Models;

public class UserModel {
    String ImageURL,username,email,password,id,device_token;

    public UserModel(String imageURL, String username, String email, String password, String id,String device_token) {
        ImageURL = imageURL;
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
        this.device_token=device_token;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
