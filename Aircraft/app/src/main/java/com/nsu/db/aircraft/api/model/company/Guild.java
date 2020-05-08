package com.nsu.db.aircraft.api.model.company;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guild {
    @SerializedName("id")
    @Expose
    private int id;

    @Expose
    @SerializedName("guildName")
    private String guildName;

    @Expose
    private Company company;


}
