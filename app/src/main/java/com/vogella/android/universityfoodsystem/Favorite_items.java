package com.vogella.android.universityfoodsystem;

public class Favorite_items {
    String Name,Description,Price;

    public Favorite_items() {
    }

    public Favorite_items(String description,String name, String price) {
        Name = name;
        Description = description;
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
