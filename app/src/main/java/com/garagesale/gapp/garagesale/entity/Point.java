package com.garagesale.gapp.garagesale.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gimbyeongjin on 2017. 7. 3..
 */
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
