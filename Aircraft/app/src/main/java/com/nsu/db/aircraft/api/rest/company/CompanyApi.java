package com.nsu.db.aircraft.api.rest.company;

import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Company;

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
import static com.nsu.db.aircraft.api.AircraftPath.CHANGE_NAME;
import static com.nsu.db.aircraft.api.AircraftPath.COMPANY;
import static com.nsu.db.aircraft.api.AircraftPath.DELETE_BY_NAME;
import static com.nsu.db.aircraft.api.AircraftPath.GET_ALL;


public interface CompanyApi {
    @POST(APP + COMPANY + ADD)
    Call<GeneralResponse<Company>> addCompany(@Body Company company);

    @GET(APP + COMPANY + GET_ALL)
    Call<GeneralResponse<List<Company>>> getAllCompanies();

    @PUT(APP + COMPANY + CHANGE_NAME)
    Call<GeneralResponse> changeCompanyName(@Body Company company,
                                            @Query("newName") String newName);

    @DELETE(APP + COMPANY + DELETE_BY_NAME)
    Call<GeneralResponse> deleteCompanyByName(@Query("companyName") String companyName);
}
