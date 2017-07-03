package com.garagesale.gapp.garagesale.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("favoriteProducts")
    @Expose
    private List<Object> favoriteProducts = null;
    @SerializedName("favoritePlanet")
    @Expose
    private List<Object> favoritePlanet = null;
    @SerializedName("planet")
    @Expose
    private Planet planet;
    @SerializedName("point")
    @Expose
    private Point point;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Object> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(List<Object> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public List<Object> getFavoritePlanet() {
        return favoritePlanet;
    }

    public void setFavoritePlanet(List<Object> favoritePlanet) {
        this.favoritePlanet = favoritePlanet;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

}
