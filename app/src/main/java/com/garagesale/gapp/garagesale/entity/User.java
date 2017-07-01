package com.garagesale.gapp.garagesale.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("_id")
    @Expose
    private String id = "";
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt = "";
    @SerializedName("createdAt")
    @Expose
    private String createdAt = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("email")
    @Expose
    private String email = "";
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

    public class Point {

        @SerializedName("coordinates")
        @Expose
        private List<Integer> coordinates = null;
        @SerializedName("type")
        @Expose
        private String type = "";

        public List<Integer> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Integer> coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class Planet {

        @SerializedName("description")
        @Expose
        private String description = "";
        @SerializedName("endDay")
        @Expose
        private String endDay = "";
        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("startDay")
        @Expose
        private String startDay = "";
        @SerializedName("comments")
        @Expose
        private List<Object> comments = null;
        @SerializedName("products")
        @Expose
        private List<Object> products = null;

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

        public List<Object> getProducts() {
            return products;
        }

        public void setProducts(List<Object> products) {
            this.products = products;
        }

    }

}
