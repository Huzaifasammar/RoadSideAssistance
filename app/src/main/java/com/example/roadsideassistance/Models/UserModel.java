package com.example.roadsideassistance.Models;

public class UserModel {
    String ImageURL,username,email,password,id;

    public UserModel(String imageURL, String username, String email, String password, String id) {
        ImageURL = imageURL;
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
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
