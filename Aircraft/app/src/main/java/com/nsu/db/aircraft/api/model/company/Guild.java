package com.nsu.db.aircraft.api.model.company;

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
public class Guild {
    @SerializedName("id")
    @Expose
    private int id;

    @Expose
    @SerializedName("guildName")
    private String guildName;

    @Expose
    private Company company;

    public Guild(String guildName, Company company) {
        this.guildName = guildName;
        this.company = company;
    }
}
