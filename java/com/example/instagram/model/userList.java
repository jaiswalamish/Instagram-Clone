package com.example.instagram.model;

public class userList {

    private String username;
    private String name;
    private String id;
    private String imageUrl;
    private String bio;
    private String email;


    public userList() {

    }

    public userList(String username, String name, String userid, String imageUrl, String bio, String email) {
        this.username = username;
        this.name = name;
        this.id = userid;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

