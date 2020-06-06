package com.nsu.db.aircraft.api.model.staff;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.String.valueOf;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brigade {
    private final static String BRIGADE = "Бригада";

    @Expose
    @SerializedName("id")
    private int id;

    @NonNull
    @Override
    public String toString() {
        return String.join(" ", BRIGADE, valueOf(id));
    }
}
