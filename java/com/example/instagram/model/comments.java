package com.example.instagram.model;

public class comments {

    private String comment;
    private String publisher;

    public comments(String username) {
        this.username = username;
    }

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    public comments(){

    }

    public comments(String comment, String publisher,String url) {
        this.comment = comment;
        this.publisher = publisher;
        this.url=url;
    }




    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
