package com.example.roadsideassistance.Models;

public class OrderModel {
    String order_id,status,user_id,provider_id,provider_name,provider_number,provider_address,provider_image,user_name,user_image,user_number,user_address;

    public OrderModel(String order_id, String status, String user_id, String provider_id, String provider_name, String provider_number, String provider_address, String provider_image, String user_name, String user_image, String user_number, String user_address) {
        this.order_id = order_id;
        this.status = status;
        this.user_id = user_id;
        this.provider_id = provider_id;
        this.provider_name = provider_name;
        this.provider_number = provider_number;
        this.provider_address = provider_address;
        this.provider_image = provider_image;
        this.user_name = user_name;
        this.user_image = user_image;
        this.user_number = user_number;
        this.user_address = user_address;
    }

    public OrderModel() {
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProvider_number() {
        return provider_number;
    }

    public void setProvider_number(String provider_number) {
        this.provider_number = provider_number;
    }

    public String getProvider_address() {
        return provider_address;
    }

    public void setProvider_address(String provider_address) {
        this.provider_address = provider_address;
    }

    public String getProvider_image() {
        return provider_image;
    }

    public void setProvider_image(String provider_image) {
        this.provider_image = provider_image;
    }
}
