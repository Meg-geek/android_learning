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
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_COMPANY;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.GET_WITHOUT_BRIGADE;
import static com.nsu.db.aircraft.api.AircraftPath.STAFF;
import static com.nsu.db.aircraft.api.AircraftPath.TURNERS;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface TurnerApi {
    @POST(APP + STAFF + TURNERS + ADD)
    Call<GeneralResponse> add(@Body Employee employee);

    @DELETE(APP + STAFF + TURNERS + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @GET(APP + STAFF + TURNERS + GET_ALL)
    Call<GeneralResponse<List<Employee>>> getAll();

    @PUT(APP + STAFF + TURNERS + UPDATE)
    Call<GeneralResponse<Employee>> update(@Body Employee employee);

    @GET(APP + STAFF + TURNERS + GET_BY_COMPANY)
    Call<GeneralResponse<List<Employee>>> getByCompany(@Query("companyId") int companyId);

    @GET(APP + STAFF + TURNERS + GET_BY_GUILD)
    Call<GeneralResponse<List<Employee>>> getByGuild(@Query("guildId") int guildId);

    @GET(APP + STAFF + TURNERS + GET_WITHOUT_BRIGADE)
    Call<GeneralResponse<List<Employee>>> getFreeWorkers();
}
