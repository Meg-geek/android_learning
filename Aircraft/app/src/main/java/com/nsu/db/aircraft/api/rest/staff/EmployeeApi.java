package com.nsu.db.aircraft.api.rest.staff;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.nsu.db.aircraft.api.AircraftPath.ALL;
import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;
import static com.nsu.db.aircraft.api.AircraftPath.STAFF;

public interface EmployeeApi {
    @GET(APP + STAFF + ALL + GET_ALL)
    Call<GeneralResponse<List<Employee>>> getAll();
}
