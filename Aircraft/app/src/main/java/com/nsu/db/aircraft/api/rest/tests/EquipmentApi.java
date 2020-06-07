package com.nsu.db.aircraft.api.rest.tests;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.tests.Equipment;

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
import static com.nsu.db.aircraft.api.AircraftPath.EQUIPMENT;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_PRODUCT_RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_HANG_GLIDERS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_HELICOPTERS;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_PLANES;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_DATE_RANGE_ROCKETS;
import static com.nsu.db.aircraft.api.AircraftPath.TESTS;
import static com.nsu.db.aircraft.api.AircraftPath.UPDATE;

public interface EquipmentApi {
    @POST(APP + TESTS + EQUIPMENT + ADD)
    Call<GeneralResponse> add(@Body Equipment equipment);

    @DELETE(APP + TESTS + EQUIPMENT + DELETE_BY_ID)
    Call<GeneralResponse> deleteById(@Query("equipmentId") int id);

    @GET(APP + TESTS + EQUIPMENT + GET_ALL)
    Call<GeneralResponse<List<Equipment>>> getAll();

    @PUT(APP + TESTS + EQUIPMENT + UPDATE)
    Call<GeneralResponse<Equipment>> update(@Body Equipment equipment);

    @GET(APP + TESTS + EQUIPMENT + GET_BY_DATE_PRODUCT_RANGE)
    Call<GeneralResponse<List<Equipment>>> getByDateAndProductAndRange(
            @Query("productId") int productId,
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + TESTS + EQUIPMENT + GET_BY_DATE_RANGE_HANG_GLIDERS)
    Call<GeneralResponse<List<Equipment>>> getByDateAndRangeAndHangGliders(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + TESTS + EQUIPMENT + GET_BY_DATE_RANGE_HELICOPTERS)
    Call<GeneralResponse<List<Equipment>>> getByDateAndRangeAndHelicopters(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + TESTS + EQUIPMENT + GET_BY_DATE_RANGE_ROCKETS)
    Call<GeneralResponse<List<Equipment>>> getByDateAndRangeAndRockets(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + TESTS + EQUIPMENT + GET_BY_DATE_RANGE_PLANES)
    Call<GeneralResponse<List<Equipment>>> getByDateAndRangeAndPlanes(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);

    @GET(APP + TESTS + EQUIPMENT + GET_BY_DATE_RANGE)
    Call<GeneralResponse<List<Equipment>>> getByDateAndRange(
            @Query("rangeId") int rangeId,
            @Query("beginDate") long beginDate,
            @Query("endDate") long endDate);
}
