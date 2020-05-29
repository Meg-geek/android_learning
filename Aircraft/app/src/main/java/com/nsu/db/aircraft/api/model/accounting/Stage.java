package com.nsu.db.aircraft.api.model.accounting;

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
public class Stage {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("stageName")
    private String stageName;

    public Stage(String stageName) {
        this.stageName = stageName;
    }
}
