package com.nsu.db.aircraft.api.rest.staff;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.nsu.db.aircraft.api.AircraftPath.APP;
import static com.nsu.db.aircraft.api.AircraftPath.ENGINEERING_STAFF;
import static com.nsu.db.aircraft.api.AircraftPath.GET_FREE_MANAGERS_FOR_GUILD;
import static com.nsu.db.aircraft.api.AircraftPath.GET_FREE_MANAGERS_FOR_SITE;
import static com.nsu.db.aircraft.api.AircraftPath.STAFF;

public interface EngineeringStaffApi {
    @GET(APP + STAFF + ENGINEERING_STAFF + GET_FREE_MANAGERS_FOR_GUILD)
    Call<GeneralResponse<List<Employee>>> getFreeManagersForGuild(@Query("guildId") int guildId);

    @GET(APP + STAFF + ENGINEERING_STAFF + GET_FREE_MANAGERS_FOR_SITE)
    Call<GeneralResponse<List<Employee>>> getFreeManagersForSite();
}
