package com.nsu.db.aircraft.api.rest.accounting;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Accounting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.ACCOUNTING;
import static com.nsu.db.aircraft.api.AircraftPath.ADD;
import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.DELETE_BY_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.GET_WORK_TYPES_BY_PRODUCT_ID;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface AccountingApi {
    @GET(APP + ACCOUNTING + ACCOUNTING + GET_ALL)
    Call<GeneralResponse<List<Accounting>>> getAll();

    @POST(APP + ACCOUNTING + ACCOUNTING + ADD)
    Call<GeneralResponse> add(@Body Accounting accounting);

    @DELETE(APP + ACCOUNTING + ACCOUNTING + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @PUT(APP + ACCOUNTING + ACCOUNTING + UPDATE)
    Call<GeneralResponse<Accounting>> update(@Body Accounting accounting);

    @GET(APP + ACCOUNTING + ACCOUNTING + GET_WORK_TYPES_BY_PRODUCT_ID)
    Call<GeneralResponse<List<String>>> getWorkTypesByProductId(@Query("productId") int productId);
}
