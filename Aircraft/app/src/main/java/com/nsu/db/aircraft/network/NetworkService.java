package com.nsu.db.aircraft.network;


import com.nsu.db.aircraft.api.rest.ProductApi;
import com.nsu.db.aircraft.api.rest.accounting.StageApi;
import com.nsu.db.aircraft.api.rest.company.CompanyApi;
import com.nsu.db.aircraft.api.rest.company.GuildApi;
import com.nsu.db.aircraft.api.rest.staff.EngineeringStaffApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static final String BASE_URL = "http://10.0.2.2:10000/";
    private static NetworkService networkService;
    private Retrofit retrofit;

    private NetworkService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

    }

    public static NetworkService getInstance() {
        if (networkService == null) {
            networkService = new NetworkService();
        }
        return networkService;
    }

    public CompanyApi getCompanyJsonApi() {
        return retrofit.create(CompanyApi.class);
    }

    public GuildApi getGuildJsonApi() {
        return retrofit.create(GuildApi.class);
    }

    public ProductApi getProductJsonApi() {
        return retrofit.create(ProductApi.class);
    }

    public StageApi getStageApi() {
        return retrofit.create(StageApi.class);
    }

    public EngineeringStaffApi getEngineeringStaffApi() {
        return retrofit.create(EngineeringStaffApi.class);
    }
}

