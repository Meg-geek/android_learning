package com.nsu.db.aircraft.api.rest.staff;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.api.model.staff.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.GET_BY_BRIGADE;
import static com.nsu.db.aircraft.api.AircraftPath.SET_BRIGADE;
import static com.nsu.db.aircraft.api.AircraftPath.STAFF;
import static com.nsu.db.aircraft.api.AircraftPath.WORKERS;

public interface WorkerApi {
    @POST(APP + STAFF + WORKERS + GET_BY_BRIGADE)
    Call<GeneralResponse<List<Employee>>> getByBrigade(@Body Brigade brigade);

    @PUT(APP + STAFF + WORKERS + SET_BRIGADE)
    Call<GeneralResponse> update(@Query("workerId") int workerId,
                                 @Query("brigadeId") int brigadeId);
}
