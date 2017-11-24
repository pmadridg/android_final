package com.isil.am2template.model.entity;

import java.io.Serializable;

/**
 * Created by Pablo Claus on 11/2/2017.
 */

public class Buffet implements Serializable {

    private String buffetId;
    private String title;
    private String description;
    private String bimage;
    private int price;
    private String qty;
    private boolean checked;

    public Buffet() {

    }

    public Buffet(String buffetId, String title, String description, String bimage, int price) {
        this.buffetId = buffetId;
        this.title = title;
        this.description = description;
        this.bimage = bimage;
        this.price = price;
    }

    public Buffet(String buffetId, String title, String description, String bimage, int price, String qty, boolean checked) {
        this.buffetId = buffetId;
        this.title = title;
        this.description = description;
        this.bimage = bimage;
        this.price = price;
        this.qty = qty;
        this.checked = checked;
    }

    public String getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(String buffetId) {
        this.buffetId = buffetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBimage() {
        return bimage;
    }

    public void setBimage(String bimage) {
        this.bimage = bimage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
