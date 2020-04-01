package com.ittsport.ittsportapp.utils;

public class UploadImage {
    private String name;
    private String imageUrl;

    public UploadImage() {
        //Empty constructor needed
    }

    public UploadImage(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
