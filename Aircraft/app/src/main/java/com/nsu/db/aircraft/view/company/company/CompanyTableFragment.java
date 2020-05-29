package com.nsu.db.aircraft.view.company.company;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.Status;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.TableFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

public class CompanyTableFragment extends TableFragment {
    private List<Company> companies;


    public CompanyTableFragment() {
        super(asList("id", "name"));
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_table, container, false);
        getCompanies(view);
        return view;
    }

    private void showTable(View view) {
        TableLayout tableLayout = view.findViewById(R.id.companies_table);
        addColumnNames(tableLayout);
        for (Company company : companies) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(getTextViewForTable(valueOf(company.getId())));
            tableRow.addView(getTextViewForTable(company.getName()));
            tableLayout.addView(tableRow);
        }
    }

    private void getCompanies(View view) {
        NetworkService.getInstance()
                .getCompanyJsonApi()
                .getAllCompanies()
                .enqueue(new Callback<GeneralResponse<List<Company>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Company>>> call,
                                           Response<GeneralResponse<List<Company>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        if (!response.body().getStatus().equals(Status.OK)) {
                            showError();
                            return;
                        }
                        companies = response.body().getData();
                        showTable(view);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Company>>> call,
                                          Throwable t) {
                        showError();
                    }
                });
    }

}
