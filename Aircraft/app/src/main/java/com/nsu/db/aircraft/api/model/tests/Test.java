package com.nsu.db.aircraft.api.model.tests;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.staff.Employee;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static java.lang.String.valueOf;

@Getter
@Setter
@EqualsAndHashCode
public class Test {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("product")
    private Product product;

    @Expose
    @SerializedName("guild")
    private Guild guild;

    @Expose
    @SerializedName("range")
    private Range range;

    @Expose
    @SerializedName("tester")
    private Employee tester;

    @Expose
    @SerializedName("equipment")
    private List<Equipment> equipment;

    @NonNull
    @Override
    public String toString() {
        return String.join(" ", "тест", valueOf(id));
    }
}
