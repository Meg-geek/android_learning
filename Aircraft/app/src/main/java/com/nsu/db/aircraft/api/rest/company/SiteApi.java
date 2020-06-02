package com.nsu.db.aircraft.api.rest.company;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Site;
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
import static com.nsu.db.aircraft.api.AircraftPath.GET_MANAGERS;
import static com.nsu.db.aircraft.api.AircraftPath.SITE;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface SiteApi {
    @GET(APP + SITE + GET_MANAGERS)
    Call<GeneralResponse<List<Employee>>> getSiteManagers();

    @GET(APP + SITE + GET_ALL)
    Call<GeneralResponse<List<Site>>> getAll();

    @POST(APP + SITE + ADD)
    Call<GeneralResponse> add(@Body Site site);

    @PUT(APP + SITE + UPDATE)
    Call<GeneralResponse<Site>> update(@Body Site site);

    @DELETE(APP + SITE + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("siteId") int siteId);
}
