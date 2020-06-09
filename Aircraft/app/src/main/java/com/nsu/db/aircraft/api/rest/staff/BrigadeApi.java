package com.nsu.db.aircraft.api.rest.staff;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Brigade;
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
import static com.nsu.db.aircraft.api.AircraftPath.BRIGADES;
import static com.nsu.db.aircraft.api.AircraftPath.DELETE_BY_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_GUILD_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_PRODUCT_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_SITE_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_FOREMAN;
import static com.nsu.db.aircraft.api.AircraftPath.SET_FOREMAN;
import static com.nsu.db.aircraft.api.AircraftPath.SET_SITE;
import static com.nsu.db.aircraft.api.AircraftPath.STAFF;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface BrigadeApi {
    @POST(APP + STAFF + BRIGADES + ADD)
    Call<GeneralResponse<Brigade>> addBrigade(@Body Brigade brigade);

    @DELETE(APP + STAFF + BRIGADES + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @GET(APP + STAFF + BRIGADES + GET_ALL)
    Call<GeneralResponse<List<Brigade>>> getAllBrigades();

    @PUT(APP + STAFF + BRIGADES + UPDATE)
    Call<GeneralResponse<Brigade>> updateBrigade(@Body Brigade brigade);

    @GET(APP + STAFF + BRIGADES + GET_BY_PRODUCT_ID)
    Call<GeneralResponse<List<Brigade>>> getByProductId(@Query("productId") int productId);

    @GET(APP + STAFF + BRIGADES + GET_BY_GUILD_ID)
    Call<GeneralResponse<List<Brigade>>> getByGuildId(@Query("guildId") int guildId);

    @GET(APP + STAFF + BRIGADES + GET_BY_SITE_ID)
    Call<GeneralResponse<List<Brigade>>> getBySiteId(@Query("siteId") int siteId);

    @PUT(APP + STAFF + BRIGADES + SET_SITE)
    Call<GeneralResponse> setSite(@Query("brigadeId") int brigadeId,
                                  @Query("siteId") int siteId);

    @PUT(APP + STAFF + BRIGADES + SET_FOREMAN)
    Call<GeneralResponse> setForeman(@Query("brigadeId") int brigadeId,
                                     @Query("staffId") int foremanId);

    @GET(APP + STAFF + BRIGADES + GET_FOREMAN)
    Call<GeneralResponse<Employee>> getForeman(@Query("brigadeId") int brigadeId);
}
