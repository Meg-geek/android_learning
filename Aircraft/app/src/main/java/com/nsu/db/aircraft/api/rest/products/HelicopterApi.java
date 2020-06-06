package com.nsu.db.aircraft.api.rest.products;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.product.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_COMPANY;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_SITE;
import static com.nsu.db.aircraft.api.AircraftPath.GET_NOW_BUILD_BY_COMPANY;
import static com.nsu.db.aircraft.api.AircraftPath.GET_NOW_BUILD_BY_GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.GET_NOW_BUILD_BY_SITE;
import static com.nsu.db.aircraft.api.AircraftPath.GET_TYPES_BY_COMPANY_ID;
import static com.nsu.db.aircraft.api.AircraftPath.GET_TYPES_BY_GUILD_ID;
import static com.nsu.db.aircraft.api.AircraftPath.HELICOPTER;
import static com.nsu.db.aircraft.api.AircraftPath.PRODUCTS;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface HelicopterApi {
    @GET(APP + PRODUCTS + HELICOPTER + GET_ALL)
    Call<GeneralResponse<List<Product>>> getAll();

    @PUT(APP + PRODUCTS + HELICOPTER + UPDATE)
    Call<GeneralResponse<Product>> update(@Body Product product);

    @GET(APP + PRODUCTS + HELICOPTER + GET_TYPES_BY_COMPANY_ID)
    Call<GeneralResponse<List<String>>> getTypesByCompanyId(@Query("companyId") int companyId);

    @GET(APP + PRODUCTS + HELICOPTER + GET_TYPES_BY_GUILD_ID)
    Call<GeneralResponse<List<String>>> getTypesByGuildId(@Query("guildId") int guildId);

    @GET(APP + PRODUCTS + HELICOPTER + GET_BY_DATE_COMPANY)
    Call<GeneralResponse<List<Product>>> getByDateIntervalAndCompany(
            @Query("companyId") int companyId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate
    );

    @GET(APP + PRODUCTS + HELICOPTER + GET_BY_DATE_GUILD)
    Call<GeneralResponse<List<Product>>> getByDateIntervalAndGuild(
            @Query("guildId") int guildId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate
    );

    @GET(APP + PRODUCTS + HELICOPTER + GET_BY_DATE_SITE)
    Call<GeneralResponse<List<Product>>> getByDateIntervalAndSite(
            @Query("siteId") int siteId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate
    );

    @GET(APP + PRODUCTS + HELICOPTER + GET_BY_DATE_RANGE)
    Call<GeneralResponse<List<Product>>> getByDateIntervalAndRange(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate
    );

    @GET(APP + PRODUCTS + HELICOPTER + GET_NOW_BUILD_BY_COMPANY)
    Call<GeneralResponse<List<Product>>> getNowBuildingByCompany(
            @Query("id") int id
    );

    @GET(APP + PRODUCTS + HELICOPTER + GET_NOW_BUILD_BY_GUILD)
    Call<GeneralResponse<List<Product>>> getNowBuildingByGuild(
            @Query("id") int id
    );

    @GET(APP + PRODUCTS + HELICOPTER + GET_NOW_BUILD_BY_SITE)
    Call<GeneralResponse<List<Product>>> getNowBuildingBySite(
            @Query("id") int id
    );
}
