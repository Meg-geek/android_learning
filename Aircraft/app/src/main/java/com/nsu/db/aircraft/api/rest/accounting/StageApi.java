package com.nsu.db.aircraft.api.rest.accounting;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Stage;

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
import static com.nsu.db.aircraft.api.AircraftPath.STAGES;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface StageApi {
    @POST(APP + ACCOUNTING + STAGES + ADD)
    Call<GeneralResponse> add(@Body Stage stage);

    @DELETE(APP + ACCOUNTING + STAGES + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @GET(APP + ACCOUNTING + STAGES + GET_ALL)
    Call<GeneralResponse<List<Stage>>> getAll();

    @PUT(APP + ACCOUNTING + STAGES + UPDATE)
    Call<GeneralResponse<Stage>> update(@Body Stage stage);
}
