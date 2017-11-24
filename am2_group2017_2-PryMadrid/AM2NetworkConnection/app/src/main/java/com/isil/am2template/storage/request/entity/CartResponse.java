package com.isil.am2template.storage.request.entity;

/**
 * Created by Pablo Claus on 11/24/2017.
 */

public class CartResponse {

    private String id;
    private String title;
    private String qty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
