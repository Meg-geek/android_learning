package com.nsu.db.aircraft.api.model.accounting;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.String.join;
import static java.lang.String.valueOf;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accounting {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("product")
    private Product product;

    @Expose
    @SerializedName("stage")
    private Stage stage;

    @Expose
    @SerializedName("site")
    private Site site;

    @Expose
    @SerializedName("test")
    private Test test;

    @Expose
    @SerializedName("beginTime")
    private long beginTime;

    @Expose
    @SerializedName("endTime")
    private long endTime;

    @NonNull
    @Override
    public String toString() {
        return join(" ", valueOf(id), product.toString());
    }
}
