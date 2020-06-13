package com.nsu.db.aircraft.api.model.product;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsu.db.aircraft.api.model.company.Guild;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.String.join;
import static java.util.Arrays.asList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    public final static String PLANE = "самолет";
    public final static String ROCKET = "ракета";
    public final static String HELICOPTER = "вертолет";
    public final static String HANG_GLIDER = "дельтаплан";
    public final static String ALL = "все";
    public static final List<String> categoriesList =
            asList(PLANE, ROCKET, HELICOPTER, HANG_GLIDER, ALL);
    public static final List<String> onlyCategories =
            asList(PLANE, ROCKET, HELICOPTER, HANG_GLIDER);

    private String productCategory;

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("guild")
    private Guild guild;

    @Expose
    @SerializedName("engineAmount")
    private int engineAmount;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("chargePower")
    private int chargePower;

    @Expose
    @SerializedName("weight")
    private int weight;

    @NonNull
    @Override
    public String toString() {
        return join(" ", productCategory, type);
    }
}
