package com.nsu.db.aircraft.view.company;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.Status;
import com.nsu.db.aircraft.api.model.Company;
import com.nsu.db.aircraft.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyAllFragment extends Fragment {
    private List<Company> companies;

    public CompanyAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_all, container, false);
        TextView textView = view.findViewById(R.id.text_view_all_companies);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Загрузка....");
        sendCompaniesRequest();
        return view;
    }

    private void sendCompaniesRequest() {
        NetworkService.getInstance()
                .getCompanyJsonApi()
                .getAllCompanies()
                .enqueue(new Callback<GeneralResponse<List<Company>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Company>>> call,
                                           Response<GeneralResponse<List<Company>>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Something went wrong",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        GeneralResponse<List<Company>> generalResponse = response.body();
                        if (generalResponse.getStatus() != Status.OK) {
                            Toast.makeText(getContext(),
                                    "Something went wrong",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        companies = generalResponse.getData();
                        updateCompaniesList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Company>>> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "Something went wrong",
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void updateCompaniesList() {
        ListView companiesView = getView().findViewById(R.id.all_companies_list_view);
        final List<String> companiesNames = new ArrayList<>();
        companies.forEach(company -> companiesNames.add(company.getName()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, companiesNames);
        companiesView.setAdapter(namesAdapter);
        TextView textView = getView().findViewById(R.id.text_view_all_companies);
        textView.setVisibility(View.INVISIBLE);
    }
}
