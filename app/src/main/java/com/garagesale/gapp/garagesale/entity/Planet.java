package com.garagesale.gapp.garagesale.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gimbyeongjin on 2017. 7. 3..
 */
public class Planet {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("endDay")
    @Expose
    private String endDay;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("startDay")
    @Expose
    private String startDay;
    @SerializedName("comments")
    @Expose
    private List<Object> comments = null;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
