package com.nsu.db.aircraft.api;

import com.nsu.db.aircraft.api.model.Company;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface CompanyApi {
    @POST("/aircraft/company/add")
    Call<GeneralResponse<?>> addCompany(@Body Company company);

    @GET("/aircraft/company/get-all")
    Call<GeneralResponse<List<Company>>> getAllCompanies();
}
