package com.example.salt.Model;

public class Upload {
    private String name;
    private String url;
    private String songsCategory;
    private String uploadedBy;

    public Upload(String name, String url, String songsCategory, String uploadedBy) {
        this.name = name;
        this.url = url;
        this.songsCategory = songsCategory;
        this.uploadedBy = uploadedBy;
    }

    public Upload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
