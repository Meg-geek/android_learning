package com.nsu.db.aircraft.api.rest.products;

import com.nsu.db.aircraft.api.GeneralResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.DELETE_BY_ID;
import static com.nsu.db.aircraft.api.AircraftPath.PRODUCTS;

public interface ProductApi {
    @DELETE(APP + PRODUCTS + DELETE_BY_ID)
    Call<GeneralResponse> deleteByProductId(@Query("productId") int productId);
}
