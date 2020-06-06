package com.nsu.db.aircraft.api.model.tests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("range")
    private Range range;
}
