package com.nsu.db.aircraft.view.staff.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.nsu.db.aircraft.api.model.staff.Employee.ENGINEERING_STAFF;
import static com.nsu.db.aircraft.api.model.staff.Employee.TESTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.WORKER;


public class StaffDetailFragment extends FragmentWithFragmentActivity {
    private final static int MIN_SIZE = 1;
    private Employee employee;
    private View view;
    private boolean isAddingFragment = true;
    private List<Brigade> brigades;
    private List<Range> ranges;
    private List<Site> sites;

    public StaffDetailFragment() {
        // Required empty public constructor
    }

    StaffDetailFragment(Employee employee) {
        this.employee = employee;
        isAddingFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_detail, container, false);
        setSpecialitiesSpinner();
        if (isAddingFragment) {
            setAddingFragment();
        }
        return view;
    }

    private void setSpecialitiesSpinner() {
        updateSpinner(view, R.id.speciality_spinner, Employee.specialities);
        Spinner specialitiesSpinner = view.findViewById(R.id.speciality_spinner);
        specialitiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSpecialSpinner(Employee.specialities.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateSpecialSpinner(String speciality) {
        switch (speciality) {
            case WORKER:
                sendBrigadesRequest();
                break;
            case ENGINEERING_STAFF:
                sendSitesRequest();
                break;
            case TESTER:
                sendRangesRequest();
                break;
        }
    }

    private void sendRangesRequest() {
        if (ranges != null && ranges.size() > MIN_SIZE) {
            return;
        }
        NetworkService.getInstance()
                .getRangeApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Range>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Range>>> call,
                                           Response<GeneralResponse<List<Range>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        ranges = response.body().getData();
                        List<String> rangeNames = ranges.stream()
                                .map(range -> range.toString()).collect(Collectors.toList());
                        updateSpinner(view, R.id.staff_details_spinner, rangeNames);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Range>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendSitesRequest() {
        if (sites != null && sites.size() > MIN_SIZE) {
            return;
        }
        NetworkService.getInstance()
                .getSiteApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Site>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Site>>> call,
                                           Response<GeneralResponse<List<Site>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        sites = response.body().getData();
                        List<String> siteNames = sites.stream()
                                .map(site -> site.toString()).collect(Collectors.toList());
                        updateSpinner(view, R.id.staff_details_spinner, siteNames);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Site>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendBrigadesRequest() {
        if (brigades != null && brigades.size() > MIN_SIZE) {
            return;
        }
        NetworkService.getInstance()
                .getBrigadeApi()
                .getAllBrigades()
                .enqueue(new Callback<GeneralResponse<List<Brigade>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Brigade>>> call,
                                           Response<GeneralResponse<List<Brigade>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        brigades = response.body().getData();
                        List<String> brigadeNames = brigades.stream()
                                .map(brigade -> brigade.toString()).collect(Collectors.toList());
                        updateSpinner(view, R.id.staff_details_spinner, brigadeNames);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Brigade>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setAddingFragment() {
        setVisibilityForAdding();
    }

    private void setVisibility(int resourceId, int visibility) {
        setVisibility(view, resourceId, visibility);
    }

    private void setVisibilityForAdding() {
        Button button = view.findViewById(R.id.button_add);
        setVisibility(R.id.button_add, VISIBLE);
        button = view.findViewById(R.id.button_change);
        setVisibility(R.id.button_change, INVISIBLE);
        button = view.findViewById(R.id.button_save);
        setVisibility(R.id.button_save, INVISIBLE);
        button = view.findViewById(R.id.button_delete);
        setVisibility(R.id.button_delete, INVISIBLE);
    }
}
