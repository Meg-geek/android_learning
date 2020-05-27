package com.nsu.db.aircraft.api.rest;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.ADD;
import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_COMPANY;
import static com.nsu.db.aircraft.api.AircraftPath.GUILD;

public interface GuildApi {
    @POST(APP + GUILD + ADD)
    Call<GeneralResponse<Guild>> addGuild(@Body Guild guild);

    @GET(APP + GUILD + GET_ALL)
    Call<GeneralResponse<List<Guild>>> getAllGuilds();

    @GET(APP + GUILD + GET_BY_COMPANY)
    Call<GeneralResponse<List<Guild>>> getByCompanyId(@Query("companyId") int companyId);
}
