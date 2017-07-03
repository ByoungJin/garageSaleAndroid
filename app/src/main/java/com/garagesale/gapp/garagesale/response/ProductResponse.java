package com.garagesale.gapp.garagesale.response;

import com.garagesale.gapp.garagesale.entity.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by gimbyeongjin on 2017. 6. 17..
 * User 정보, Login Response 받는 객체
 */

public class ProductResponse {

    @SerializedName("product")
    @Expose
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
