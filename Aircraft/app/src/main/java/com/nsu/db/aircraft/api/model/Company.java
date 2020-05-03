package com.nsu.db.aircraft.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Company {
    @SerializedName("id")
    @Expose(serialize = false)
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
