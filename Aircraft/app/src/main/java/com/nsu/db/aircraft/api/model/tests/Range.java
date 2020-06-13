package com.nsu.db.aircraft.api.model.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsu.db.aircraft.api.model.company.Guild;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.String.valueOf;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Range {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("guilds")
    private List<Guild> guilds;

    @NonNull
    @Override
    public String toString() {
        return String.join(" ", valueOf(id), name);
    }
}
