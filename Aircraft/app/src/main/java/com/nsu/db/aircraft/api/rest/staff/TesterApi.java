package com.nsu.db.aircraft.api.rest.staff;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.api.model.tests.Range;

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
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_PRODUCT_RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_HANG_GLIDERS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_HELICOPTERS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_PLANES;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_ROCKETS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.STAFF;
import static com.nsu.db.aircraft.api.AircraftPath.TESTERS;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface TesterApi {
    @POST(APP + STAFF + TESTERS + ADD)
    Call<GeneralResponse> add(@Body Employee employee);

    @DELETE(APP + STAFF + TESTERS + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("id") int id);

    @GET(APP + STAFF + TESTERS + GET_ALL)
    Call<GeneralResponse<List<Employee>>> getAll();

    @PUT(APP + STAFF + TESTERS + UPDATE)
    Call<GeneralResponse<Employee>> update(@Body Employee employee);

    @GET(APP + STAFF + TESTERS + GET_BY_COMPANY)
    Call<GeneralResponse<List<Employee>>> getByCompany(@Query("companyId") int companyId);

    @GET(APP + STAFF + TESTERS + GET_BY_GUILD)
    Call<GeneralResponse<List<Employee>>> getByGuild(@Query("guildId") int guildId);

    @GET(APP + STAFF + TESTERS + GET_BY_DATE_PRODUCT_RANGE)
    Call<GeneralResponse<List<Employee>>> getByDateAndProductAndRange(
            @Query("productId") int productId,
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + STAFF + TESTERS + GET_BY_DATE_RANGE_HANG_GLIDERS)
    Call<GeneralResponse<List<Employee>>> getByDateAndRangeAndHangGliders(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + STAFF + TESTERS + GET_BY_DATE_RANGE_HELICOPTERS)
    Call<GeneralResponse<List<Employee>>> getByDateAndRangeAndHelicopters(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + STAFF + TESTERS + GET_BY_DATE_RANGE_ROCKETS)
    Call<GeneralResponse<List<Employee>>> getByDateAndRangeAndRockets(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + STAFF + TESTERS + GET_BY_DATE_RANGE_PLANES)
    Call<GeneralResponse<List<Employee>>> getByDateAndRangeAndPlanes(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + STAFF + TESTERS + GET_BY_DATE_RANGE)
    Call<GeneralResponse<List<Employee>>> getByDateAndRange(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @PUT(APP + STAFF + TESTERS + GET_BY_RANGE)
    Call<GeneralResponse<List<Employee>>> getByRange(@Body Range range);
}
