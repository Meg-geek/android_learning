package com.example.starbuzz;

import androidx.annotation.NonNull;

public class Drink {
    private String name, description;
    private int imageResourceId;

    static final Drink[] drinks = {
            new Drink("Latte",
                    "A couple of espresso shots with steamed milk",
                    R.drawable.latte),
            new Drink("Cappuccino",
                    "Espresso, hot milk, and a steamed milk foam",
                    R.drawable.cappuccino),
            new Drink("Filter",
                    "Highest quality beans roasted and brewed fresh",
                    R.drawable.filter)
    };

    private Drink(String name, String description, int imageResourceId){
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    int getImageResourceId() {
        return imageResourceId;
    }

    String getDescription() {
        return description;
    }

    String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
