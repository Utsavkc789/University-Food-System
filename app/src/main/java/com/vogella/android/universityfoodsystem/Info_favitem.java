package com.vogella.android.universityfoodsystem;

import android.util.EventLogTags;

public class Info_favitem {
    String description,name,price;

    public Info_favitem(String description, String name, String price) {
        this.description = description;
        this.name = name;
        this.price = price;
    }

    public Info_favitem() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String toString(){
        return this.name +"          " +
                " "+this.price;
    }
}


