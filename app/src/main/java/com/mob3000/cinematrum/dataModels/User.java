package com.mob3000.cinematrum.dataModels;

import java.util.ArrayList;

public class User {

    private int user_id;
    private String name;
    private String email;


    private String passwordHash;
    private String salt;
    private String telephone;
    private String userType;
    private ArrayList<Ticket> tickets;
    private ArrayList<Wishlist> wishlist;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.passwordHash = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public ArrayList<Wishlist> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }
}
