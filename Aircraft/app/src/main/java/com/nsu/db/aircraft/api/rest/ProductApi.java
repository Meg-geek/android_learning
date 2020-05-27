package com.nsu.db.aircraft.api.rest;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.product.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.DELETE_BY_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.HANG_GLIDER;
import static com.nsu.db.aircraft.api.AircraftPath.HELICOPTER;
import static com.nsu.db.aircraft.api.AircraftPath.PLANE;
import static com.nsu.db.aircraft.api.AircraftPath.PRODUCTS;
import static com.nsu.db.aircraft.api.AircraftPath.ROCKET;

public interface ProductApi {
    @GET(APP + PRODUCTS + PLANE + GET_ALL)
    Call<GeneralResponse<List<Product>>> getAllPlanes();

    @GET(APP + PRODUCTS + HELICOPTER + GET_ALL)
    Call<GeneralResponse<List<Product>>> getAllHelicopters();

    @GET(APP + PRODUCTS + ROCKET + GET_ALL)
    Call<GeneralResponse<List<Product>>> getAllRockets();

    @GET(APP + PRODUCTS + HANG_GLIDER + GET_ALL)
    Call<GeneralResponse<List<Product>>> getAllHangGliders();

    @DELETE(APP + PRODUCTS + DELETE_BY_ID)
    Call<GeneralResponse> deleteByProductId(@Query("productId") int productId);
}
