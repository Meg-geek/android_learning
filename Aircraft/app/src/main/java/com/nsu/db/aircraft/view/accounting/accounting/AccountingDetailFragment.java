package com.nsu.db.aircraft.view.accounting.accounting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Accounting;
import com.nsu.db.aircraft.api.model.accounting.Stage;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Test;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;

public class AccountingDetailFragment extends FragmentWithFragmentActivity {
    private boolean isAddFragment = true;
    private Accounting accounting;
    private View view;
    private List<Product> products = new ArrayList<>();
    private List<Stage> stages;
    private List<Site> sites;
    private List<Test> tests;

    public AccountingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accounting_detail, container, false);
        if (isAddFragment) {
            setAddFragment();
        }
        setProductsSpinner();
        return view;
    }

    private void setProductsSpinner() {
        Spinner productSpinner = view.findViewById(R.id.product_spinner);
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendSitesRequest(products.get(position).getGuild());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendSitesRequest(Guild guild) {
        NetworkService.getInstance()
                .getSiteApi()
                .getByGuild(guild)
                .enqueue(new SitesCallBack());
    }

    private void setAddFragment() {
        setVisibilityForAdd();
        sendProductsRequest();
        sendStagesRequest();
    }

    private void sendStagesRequest() {
        NetworkService.getInstance()
                .getStageApi()
                .getAll()
                .enqueue(new StagesCallBack());
    }

    private void sendProductsRequest() {
        NetworkService.getInstance()
                .getRocketApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.ROCKET));
        NetworkService.getInstance()
                .getHangGliderApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.HANG_GLIDER));
        NetworkService.getInstance()
                .getHelicopterApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.HELICOPTER));
        NetworkService.getInstance()
                .getPlaneApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.PLANE));
    }

    private void setVisibilityForAdd() {
        setVisibility(view, R.id.button_change, INVISIBLE);
        setVisibility(view, R.id.button_delete, INVISIBLE);
        setVisibility(view, R.id.button_save, INVISIBLE);
    }

    private void updateProductsSpinner() {
        List<String> names = products.stream()
                .map(product -> product.toString())
                .collect(Collectors.toList());
        updateSpinner(view, R.id.product_spinner, names);
    }

    @AllArgsConstructor
    private class ProductsCallBack implements Callback<GeneralResponse<List<Product>>> {
        private String productCategory;

        @Override
        public void onResponse(Call<GeneralResponse<List<Product>>> call,
                               Response<GeneralResponse<List<Product>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Product> responseProducts = response.body().getData();
            responseProducts.forEach(rocket -> rocket.setProductCategory(productCategory));
            products.addAll(responseProducts);
            updateProductsSpinner();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Product>>> call, Throwable t) {
            showError();
        }
    }

    private class SitesCallBack implements Callback<GeneralResponse<List<Site>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Site>>> call, Response<GeneralResponse<List<Site>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            sites = response.body().getData();
            List<String> siteNames = sites.stream()
                    .map(site -> site.toString()).collect(Collectors.toList());
            updateSpinner(view, R.id.sites_spinner, siteNames);
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Site>>> call, Throwable t) {
            showError();
        }
    }

    private class StagesCallBack implements Callback<GeneralResponse<List<Stage>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Stage>>> call,
                               Response<GeneralResponse<List<Stage>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            stages = response.body().getData();
            List<String> stagesNames = stages.stream()
                    .map(stage -> stage.getStageName()).collect(Collectors.toList());
            updateSpinner(view, R.id.sites_spinner, stagesNames);
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Stage>>> call, Throwable t) {
            showError();
        }
    }
}
