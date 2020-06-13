package com.nsu.db.aircraft.api.rest.tests;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.tests.Range;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.ADD;
import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.DELETE_BY_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_PRODUCT_ID;
import static com.nsu.db.aircraft.api.AircraftPath.RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.TESTS;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface RangeApi {
    @GET(APP + TESTS + RANGE + GET_ALL)
    Call<GeneralResponse<List<Range>>> getAll();

    @GET(APP + TESTS + RANGE + GET_BY_PRODUCT_ID)
    Call<GeneralResponse<List<Range>>> getByProductId(@Query("productId") int productId);

    @POST(APP + TESTS + RANGE + ADD)
    Call<GeneralResponse<Range>> add(@Body Range range);

    @DELETE(APP + TESTS + RANGE + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @PUT(APP + TESTS + RANGE + UPDATE)
    Call<GeneralResponse<Range>> update(@Body Range range);
}
