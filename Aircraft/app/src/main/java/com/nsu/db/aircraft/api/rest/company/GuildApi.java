package com.nsu.db.aircraft.api.rest.company;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
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
import static com.nsu.db.aircraft.api.AircraftPath.GET_MANAGERS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_MANAGER_BY_GUILD_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface GuildApi {
    @POST(APP + GUILD + ADD)
    Call<GeneralResponse<Guild>> addGuild(@Body Guild guild);

    @GET(APP + GUILD + GET_ALL)
    Call<GeneralResponse<List<Guild>>> getAllGuilds();

    @GET(APP + GUILD + GET_BY_COMPANY)
    Call<GeneralResponse<List<Guild>>> getByCompanyId(@Query("companyId") int companyId);

    @GET(APP + GUILD + GET_MANAGER_BY_GUILD_ID)
    Call<GeneralResponse<Employee>> getGuildManagerByGuildId(@Query("guildId") int guildId);

    @GET(APP + GUILD + GET_MANAGERS)
    Call<GeneralResponse<List<Employee>>> getGuildManagers();

    @PUT(APP + GUILD + UPDATE)
    Call<GeneralResponse<Guild>> updateGuild(@Body Guild guild);

    @DELETE(APP + GUILD + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("guildId") int guildId);
}
