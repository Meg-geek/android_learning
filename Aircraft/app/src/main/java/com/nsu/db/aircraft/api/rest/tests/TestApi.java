package com.nsu.db.aircraft.api.rest.tests;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.tests.Test;

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
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.TEST;
import static com.nsu.db.aircraft.api.AircraftPath.TESTS;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface TestApi {
    @POST(APP + TESTS + TEST + ADD)
    Call<GeneralResponse> add(@Body Test test);

    @DELETE(APP + TESTS + TEST + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @GET(APP + TESTS + TEST + GET_ALL)
    Call<GeneralResponse<List<Test>>> getAll();

    @PUT(APP + TESTS + TEST + UPDATE)
    Call<GeneralResponse<Test>> update(@Body Test test);

    @PUT(APP + TESTS + TEST + GET_BY_GUILD)
    Call<GeneralResponse<List<Test>>> getByGuild(@Body Guild guild);
}
