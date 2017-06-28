package com.garagesale.gapp.garagesale.entity;

/**
 * Created by juyeol on 2017-06-28.
 * 아이템정보, 댓글정보 데이터
 */



public class listData {
    public String header;
    public String option;
    public String body;
    public int img;

    public listData(String header, String option, String body, int img) {
        this.header = header;
        this.option = option;
        this.body = body;
        this.img = img;
    }
}