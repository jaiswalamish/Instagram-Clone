package com.example.instagram.model;

public class post {
    private String description;
    private String postid;
    private String url;
    private String uploader;

    public post(String description, String postid, String url, String uploader) {
        this.description = description;
        this.postid = postid;
        this.url = url;
        this.uploader = uploader;
    }
    public post(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }



}
