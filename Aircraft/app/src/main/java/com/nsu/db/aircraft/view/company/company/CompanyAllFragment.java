package com.nsu.db.aircraft.view.company.company;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.Status;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyAllFragment extends Fragment {
    private FragmentActivity fragmentActivity;

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
        setCompaniesListView(view);
        sendCompaniesRequest();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    private void setCompaniesListView(View view) {
        ListView companiesView = view.findViewById(R.id.all_companies_list_view);
        companiesView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < 0 || position >= companies.size()) {
                return;
            }
            startCompanyDetailFragment(companies.get(position));
        });
    }

    private void startCompanyDetailFragment(Company company) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new CompanyDetailFragment(company));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
                                    R.string.error_text,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        GeneralResponse<List<Company>> generalResponse = response.body();
                        if (generalResponse.getStatus() != Status.OK) {
                            Toast.makeText(getContext(),
                                    R.string.error_text,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        companies = generalResponse.getData();
                        updateCompaniesList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Company>>> call, Throwable t) {
                        Toast.makeText(getContext(),
                                R.string.error_text,
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
