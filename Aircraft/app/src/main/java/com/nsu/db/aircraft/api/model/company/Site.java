package com.nsu.db.aircraft.api.model.company;

import androidx.annotation.NonNull;

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
public class Site {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("workType")
    private String workType;

    @Expose
    @SerializedName("guild")
    private Guild guild;

    @NonNull
    @Override
    public String toString() {
        return String.join(" ", workType, "цех", guild.getGuildName());
    }
}
