package com.nsu.db.aircraft.view.staff.brigade;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.AdapterView.INVALID_POSITION;
import static java.util.Arrays.asList;


public class BrigadeDetailFragment extends FragmentWithFragmentActivity {
    private View view;
    private Brigade brigade;
    private List<Employee> brigadeWorkers;
    private List<Employee> foremen = new ArrayList<>();
    private List<Site> sites;
    private boolean isAddFragment = true;
    private AtomicBoolean isSiteToBrigadeSent = new AtomicBoolean(false),
            isWorkersBrigadeSent = new AtomicBoolean(false),
            isForemanSet = new AtomicBoolean(false);

    public BrigadeDetailFragment(Brigade brigade) {
        this.brigade = brigade;
        this.isAddFragment = false;
    }

    public BrigadeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_brigade_detail, container, false);
        if (isAddFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }

        return view;
    }

    private void setAddFragment() {
        setVisibilityForAdd();
        sendFreeWorkersRequest();
        setWorkersList();
        sendSitesRequest();
        setAddButton();
    }

    private void setDetailFragment() {
        setVisibilityForDetail();
        setNotEnabledElements();
        setBrigadeName();
        sendWorkersRequest();
        sendGetForemanRequest();
        sendSiteRequest();
        setDetailButtons();
    }

    private void setDetailButtons() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> sendDeleteRequest());
        Button changeButton = view.findViewById(R.id.button_change);
        changeButton.setOnClickListener(v -> {
            setVisibility(view, R.id.button_save, VISIBLE);
            foremen = brigadeWorkers;
            updateForemansSpinner();
            sendSitesRequest();
            setEnabled(view.findViewById(R.id.sites_spinner), true);
            setEnabled(view.findViewById(R.id.foremans_spinner), true);
        });
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> sendUpdateRequests());
    }

    private void sendUpdateRequests() {
        isWorkersBrigadeSent.set(true);
        sendSetForemanRequest();
        sendSetSiteToBrigadeRequest();
    }

    private void sendDeleteRequest() {
        NetworkService.getInstance()
                .getBrigadeApi().deleteById(brigade.getId())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.success_delete);
                        startFragment(new BrigadeMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendSiteRequest() {
        NetworkService.getInstance()
                .getSiteApi()
                .getByBrigadeId(brigade.getId())
                .enqueue(new Callback<GeneralResponse<Site>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Site>> call,
                                           Response<GeneralResponse<Site>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        sites = new ArrayList<>();
                        Site site = response.body().getData();
                        sites.add(site);
                        updateSpinner(view, R.id.sites_spinner, asList(site.toString()));
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Site>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendGetForemanRequest() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .getForeman(brigade.getId())
                .enqueue(new Callback<GeneralResponse<Employee>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Employee>> call,
                                           Response<GeneralResponse<Employee>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        Employee foreman = response.body().getData();
                        if (foreman.isEmpty()) {
                            return;
                        }
                        foremen.add(foreman);
                        updateForemansSpinner();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Employee>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setNotEnabledElements() {
        setEnabled(view.findViewById(R.id.workers_list), false);
        setEnabled(view.findViewById(R.id.foremans_spinner), false);
        setEnabled(view.findViewById(R.id.sites_spinner), false);
    }

    private void setVisibilityForDetail() {
        setVisibility(view, R.id.button_add, INVISIBLE);
        setVisibility(view, R.id.button_save, INVISIBLE);
    }

    private void setVisibilityForAdd() {
        setVisibility(view, R.id.brigade_name, INVISIBLE);
        setVisibility(view, R.id.button_delete, INVISIBLE);
        setVisibility(view, R.id.button_change, INVISIBLE);
        setVisibility(view, R.id.button_save, INVISIBLE);
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            sendCreateBrigadeRequest();
        });
    }

    private void checkBrigadeAdding() {
        System.out.println(isForemanSet.get() + " " + isSiteToBrigadeSent.get()
                + " " + isWorkersBrigadeSent.get());
        if (isForemanSet.get() &&
                isSiteToBrigadeSent.get() &&
                isWorkersBrigadeSent.get()) {
            showExitText();
            startFragment(new BrigadeMainFragment());
        }
    }

    private void showExitText() {
        if (isAddFragment) {
            showText(R.string.load_success);
        } else {
            showText(R.string.update_success);
        }
    }

    private void sendCreateBrigadeRequest() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .addBrigade(new Brigade())
                .enqueue(new Callback<GeneralResponse<Brigade>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Brigade>> call,
                                           Response<GeneralResponse<Brigade>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        brigade = response.body().getData();
                        sendSetSiteToBrigadeRequest();
                        sendSetForemanRequest();
                        updateWorkers();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Brigade>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateWorkers() {
        if (foremen == null) {
            isForemanSet.set(true);
            checkBrigadeAdding();
        }
        foremen.forEach(foreman -> foreman.setBrigade(brigade));
        foremen.forEach(this::sendWorkerUpdateRequest);
        isWorkersBrigadeSent.set(true);
        checkBrigadeAdding();
    }

    private void sendWorkerUpdateRequest(Employee worker) {
        NetworkService.getInstance()
                .getWorkerApi()
                .update(worker.getId(), brigade.getId())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendSetSiteToBrigadeRequest() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .setSite(brigade.getId(), getSelectedSite().getId())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                        }
                        isSiteToBrigadeSent.set(true);
                        checkBrigadeAdding();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendSetForemanRequest() {
        Employee foreman = getSelectedForeman();
        if (foreman == null) {
            isForemanSet.set(true);
            checkBrigadeAdding();
            return;
        }
        NetworkService.getInstance()
                .getBrigadeApi()
                .setForeman(brigade.getId(), foreman.getId())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                        }
                        isForemanSet.set(true);
                        checkBrigadeAdding();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private Site getSelectedSite() {
        Spinner spinner = view.findViewById(R.id.sites_spinner);
        return sites.get(spinner.getSelectedItemPosition());
    }

    private Employee getSelectedForeman() {
        Spinner spinner = view.findViewById(R.id.foremans_spinner);
        int position = spinner.getSelectedItemPosition();
        if (position == INVALID_POSITION) {
            return null;
        }
        return foremen.get(position);
    }

    private void sendSitesRequest() {
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
                                .map(Site::toString).collect(Collectors.toList());
                        updateSpinner(view, R.id.sites_spinner, siteNames);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Site>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setWorkersList() {
        ListView workersList = view.findViewById(R.id.workers_list);
        workersList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        workersList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    SparseBooleanArray sparseBooleanArray = workersList
                            .getCheckedItemPositions();
                    foremen = new ArrayList<>();
                    for (int i = 0; i < sparseBooleanArray.size(); i++) {
                        int key = sparseBooleanArray.keyAt(i);
                        if (sparseBooleanArray.get(key)) {
                            foremen.add(brigadeWorkers.get(key));
                        }
                    }
                    updateForemansSpinner();
                }
        );
    }

    private void updateForemansSpinner() {
        Spinner spinner = view.findViewById(R.id.foremans_spinner);
        List<String> foremanNames = foremen.stream()
                .map(Employee::toString)
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                foremanNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void sendFreeWorkersRequest() {
        brigadeWorkers = new ArrayList<>();
        sendFreePickersRequest();
        sendFreeWeldersRequest();
        sendFreeLocksmithRequest();
        sendFreeTurnersRequest();
    }

    private void sendFreePickersRequest() {
        NetworkService.getInstance()
                .getPickerApi()
                .getFreeWorkers()
                .enqueue(new BrigadeWorkersCallback(false));
    }

    private void sendFreeWeldersRequest() {
        NetworkService.getInstance()
                .getWelderApi()
                .getFreeWorkers()
                .enqueue(new BrigadeWorkersCallback(false));
    }

    private void sendFreeLocksmithRequest() {
        NetworkService.getInstance()
                .getLocksmithApi()
                .getFreeWorkers()
                .enqueue(new BrigadeWorkersCallback(false));
    }

    private void sendFreeTurnersRequest() {
        NetworkService.getInstance()
                .getTurnerApi()
                .getFreeWorkers()
                .enqueue(new BrigadeWorkersCallback(false));
    }

    private void sendWorkersRequest() {
        NetworkService.getInstance()
                .getWorkerApi()
                .getByBrigade(brigade)
                .enqueue(new BrigadeWorkersCallback(true));
    }

    private void setBrigadeName() {
        TextView brigadeName = view.findViewById(R.id.brigade_name);
        brigadeName.setText(brigade.toString());
    }

    private void updateWorkersList() {
        ListView workersList = view.findViewById(R.id.workers_list);
        List<String> workersNames = new ArrayList<>();
        brigadeWorkers.forEach(worker -> workersNames.add(worker.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, workersNames);
        workersList.setAdapter(namesAdapter);
    }

    @AllArgsConstructor
    private class BrigadeWorkersCallback implements Callback<GeneralResponse<List<Employee>>> {
        private boolean isSingleRequest;

        @Override
        public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                               Response<GeneralResponse<List<Employee>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            if (isSingleRequest) {
                brigadeWorkers = response.body().getData();
            } else {
                brigadeWorkers.addAll(response.body().getData());
            }

            updateWorkersList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
            showError();
        }
    }
}
