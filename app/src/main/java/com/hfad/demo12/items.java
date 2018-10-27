package com.hfad.demo12;

/**
 * Created by HP on 16-05-2018.
 */

public class items{
    private String item;
    private int imageId;
    private String price;



    public items() {
    }

    public items(int imageId, String item, String price) {
        this.imageId = imageId;
        this.item = item;
        this.price = price;
    }

    public int getImageId() {
        return imageId;
    }


    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

