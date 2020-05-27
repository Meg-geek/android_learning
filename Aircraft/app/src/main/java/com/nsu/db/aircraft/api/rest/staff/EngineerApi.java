package com.nsu.db.aircraft.api.rest.staff;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Employee;

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
import static com.nsu.db.aircraft.api.AircraftPath.ENGINEERS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.STAFF;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface EngineerApi {
    @POST(APP + STAFF + ENGINEERS + ADD)
    Call<GeneralResponse> add(@Body Employee employee);

    @DELETE(APP + STAFF + ENGINEERS + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @GET(APP + STAFF + ENGINEERS + GET_ALL)
    Call<GeneralResponse<List<Employee>>> getAll();

    @PUT(APP + STAFF + ENGINEERS + UPDATE)
    Call<GeneralResponse<Employee>> update(@Body Employee employee);
}
