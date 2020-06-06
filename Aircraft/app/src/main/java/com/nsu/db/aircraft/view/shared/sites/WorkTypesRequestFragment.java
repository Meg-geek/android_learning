package com.nsu.db.aircraft.view.shared.sites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkTypesRequestFragment extends FragmentWithFragmentActivity {
    private Product product;
    private View view;
    private List<String> workTypes;

    public WorkTypesRequestFragment() {
        // Required empty public constructor
    }

    public WorkTypesRequestFragment(Product product) {
        this.product = product;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_work_types_request, container, false);
        sendWorkTypesRequest();
        return view;
    }

    private void updateWorkTypesList() {
        ListView workTypesListView = view.findViewById(R.id.work_types);
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, workTypes);
        workTypesListView.setAdapter(namesAdapter);
    }

    private void sendWorkTypesRequest() {
        NetworkService.getInstance()
                .getAccountingApi()
                .getWorkTypesByProductId(product.getId())
                .enqueue(new Callback<GeneralResponse<List<String>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<String>>> call,
                                           Response<GeneralResponse<List<String>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        workTypes = response.body().getData();
                        updateWorkTypesList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<String>>> call, Throwable t) {
                        showError();
                    }
                });
    }
}
