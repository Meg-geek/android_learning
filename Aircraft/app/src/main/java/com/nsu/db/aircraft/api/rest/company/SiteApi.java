package com.nsu.db.aircraft.api.rest.company;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
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
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_BRIGADE_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_COMPANY_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.GET_MANAGERS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_SITE_MANAGER_BY_SITE_ID;
import static com.nsu.db.aircraft.api.AircraftPath.SITE;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE_MANAGER;

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

    @PUT(APP + SITE + UPDATE_MANAGER)
    Call<GeneralResponse> updateSiteManager(@Query("siteId") int siteId,
                                            @Query("managerId") int newManagerId);

    @GET(APP + SITE + GET_SITE_MANAGER_BY_SITE_ID)
    Call<GeneralResponse<Employee>> getSiteManager(@Query("siteId") int siteId);

    @POST(APP + SITE + GET_BY_GUILD)
    Call<GeneralResponse<List<Site>>> getByGuild(@Body Guild guild);

    @GET(APP + SITE + GET_BY_COMPANY_ID)
    Call<GeneralResponse<List<Site>>> getByCompanyId(@Query("companyId") int companyId);

    @GET(APP + SITE + GET_BY_BRIGADE_ID)
    Call<GeneralResponse<Site>> getByBrigadeId(@Query("brigadeId") int brigadeId);
}
