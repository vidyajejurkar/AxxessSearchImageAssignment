package com.vidya.axxesssearchimageassignment.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Processing {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}