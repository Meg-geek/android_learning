package com.nsu.db.aircraft.api;

import com.nsu.db.aircraft.api.model.Company;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface CompanyApi {
    @POST("/aircraft/company/add")
    Call<GeneralResponse<Company>> addCompany(@Body Company company);

    @GET("/aircraft/company/get-all")
    Call<GeneralResponse<List<Company>>> getAllCompanies();

    @PUT("/aircraft/company/change-name")
    Call<GeneralResponse> changeCompanyName(@Body Company company,
                                            @Query("newName") String newName);

    @DELETE("/aircraft/company/delete-by-name")
    Call<GeneralResponse> deleteCompanyByName(@Query("companyName") String companyName);
}
