package com.nsu.db.aircraft.api.rest.tests;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.tests.Range;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.TESTS;

public interface RangeApi {
    @GET(APP + TESTS + RANGE + GET_ALL)
    Call<GeneralResponse<List<Range>>> getAll();
}
