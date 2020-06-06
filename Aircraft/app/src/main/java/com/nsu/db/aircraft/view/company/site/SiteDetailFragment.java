package com.nsu.db.aircraft.view.company.site;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.shared.products.ProductRequestDetails;
import com.nsu.db.aircraft.view.shared.staff.BrigadeRequestFragment;
import com.nsu.db.aircraft.view.shared.staff.StaffRequestFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.AdapterView.INVALID_POSITION;
import static java.util.Arrays.asList;


public class SiteDetailFragment extends FragmentWithFragmentActivity {
    private View view;
    private Site site;
    private List<Guild> guilds;
    private List<Employee> siteManagers;
    private boolean isAddFragment = true;

    public SiteDetailFragment() {
        // Required empty public constructor
    }

    public SiteDetailFragment(Site site) {
        this.site = site;
        this.isAddFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_site_detail, container, false);
        if (isAddFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }
        return view;
    }

    private void setDetailFragment() {
        setVisibilityForDetail();
        changeAccessibility(false);
        setDeleteButton();
        setChangeButton();
        setSaveButton();
        setSiteData();
        setStartFragmentButton(view, R.id.button_products, new ProductRequestDetails(site));
        setStartFragmentButton(view, R.id.button_staff, new StaffRequestFragment(site));
        setStartFragmentButton(view, R.id.button_brigades, new BrigadeRequestFragment(site));
    }

    private void setSiteData() {
        TextInputEditText workType = view.findViewById(R.id.work_type_edit_text);
        workType.setText(site.getWorkType());
        guilds = new ArrayList<>(asList(site.getGuild()));
        updateGuildsSpinner();
        sendSiteManagerRequest();
    }

    private void sendSiteManagerRequest() {
        NetworkService.getInstance()
                .getSiteApi()
                .getSiteManager(site.getId())
                .enqueue(new Callback<GeneralResponse<Employee>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Employee>> call, Response<GeneralResponse<Employee>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        Employee employee = response.body().getData();
                        siteManagers = new ArrayList<>();
                        if (employee.getName() != null && !employee.getName().isEmpty()) {
                            siteManagers.add(employee);

                        }
                        updateManagerSpinner();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Employee>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            hideKeyboard();
            sendSiteUpdateRequest();
            sendSiteManagerUpdateRequest();
        });
    }

    private void sendSiteManagerUpdateRequest() {
        int siteManagerId = getSelectedManagerId();
        if (siteManagerId == INVALID_POSITION) {
            return;
        }
        NetworkService.getInstance()
                .getSiteApi()
                .updateSiteManager(site.getId(), siteManagerId)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.update_success);
                        startFragment(new SiteMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private int getSelectedManagerId() {
        Spinner managersSpinner = view.findViewById(R.id.site_managers_spinner);
        int position = managersSpinner.getSelectedItemPosition();
        return siteManagers.get(position).getId();
    }

    private void sendSiteUpdateRequest() {
        site.setWorkType(getEnteredName(R.id.work_type_edit_text));
        site.setGuild(getSelectedGuild());
        NetworkService.getInstance()
                .getSiteApi()
                .update(site)
                .enqueue(new Callback<GeneralResponse<Site>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Site>> call,
                                           Response<GeneralResponse<Site>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        site = response.body().getData();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Site>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private Guild getSelectedGuild() {
        Spinner guildSpinner = view.findViewById(R.id.guilds_spinner);
        int position = guildSpinner.getSelectedItemPosition();
        return guilds.get(position);
    }

    private void setChangeButton() {
        Button changeButton = view.findViewById(R.id.button_change);
        changeButton.setOnClickListener(v -> {
            setVisibility(view, R.id.button_save, View.VISIBLE);
            changeAccessibility(true);
            sendSiteManagersRequest();
            sendGuildsRequest();
        });
    }

    private void sendSiteManagersRequest() {
        NetworkService.getInstance()
                .getEngineeringStaffApi()
                .getFreeManagersForSite()
                .enqueue(new Callback<GeneralResponse<List<Employee>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                                           Response<GeneralResponse<List<Employee>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        if (siteManagers == null) {
                            siteManagers = new ArrayList<>();
                        }
                        siteManagers.addAll(response.body().getData());
                        updateManagerSpinner();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateManagerSpinner() {
        List<String> managerNames = siteManagers.stream()
                .map(siteManager ->
                        String.join(" ", siteManager.getName(), siteManager.getSurname()))
                .collect(Collectors.toList());
        Spinner spinner = view.findViewById(R.id.site_managers_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                managerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void sendGuildsRequest() {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getAllGuilds()
                .enqueue(new Callback<GeneralResponse<List<Guild>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Guild>>> call,
                                           Response<GeneralResponse<List<Guild>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        guilds = response.body().getData();
                        updateGuildsSpinner();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Guild>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateGuildsSpinner() {
        List<String> guildNames = guilds.stream()
                .map(guild -> guild.getGuildName())
                .collect(Collectors.toList());
        updateSpinner(view, R.id.guilds_spinner, guildNames);
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> {
            NetworkService.getInstance()
                    .getSiteApi()
                    .deleteById(site.getId())
                    .enqueue(new DeleteCallback(new SiteMainFragment()));
        });
    }


    private void changeAccessibility(boolean isEnabled) {
        setEnabledInputEditText(view, R.id.work_type_edit_text, isEnabled);
        Spinner guildsSpinner = view.findViewById(R.id.guilds_spinner);
        guildsSpinner.setEnabled(isEnabled);
        Spinner siteManagersSpinner = view.findViewById(R.id.site_managers_spinner);
        siteManagersSpinner.setEnabled(isEnabled);
    }

    private void setAddFragment() {
        setVisibilityForAdd();
        sendGuildsRequest();
        sendSiteManagersRequest();
        setAddButton();
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            hideKeyboard();
            sendAddRequest();
            sendSiteManagerUpdateRequest();
        });
    }

    private void sendAddRequest() {
        site = new Site();
        site.setGuild(getSelectedGuild());
        site.setWorkType(getEnteredName(R.id.work_type_edit_text));
        NetworkService.getInstance()
                .getSiteApi()
                .add(site)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (response.isSuccessful()) {
                            showText(R.string.load_success);
                            startFragment(new SiteMainFragment());
                            return;
                        }
                        showError();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setVisibilityForAdd() {
        setVisibility(view, R.id.button_add, View.VISIBLE);
        setVisibility(view, R.id.button_change, View.INVISIBLE);
        setVisibility(view, R.id.button_save, View.INVISIBLE);
        setVisibility(view, R.id.button_delete, View.INVISIBLE);
    }

    private void setVisibilityForDetail() {
        setVisibility(view, R.id.button_add, View.INVISIBLE);
        setVisibility(view, R.id.button_change, View.VISIBLE);
        setVisibility(view, R.id.button_save, View.INVISIBLE);
        setVisibility(view, R.id.button_delete, View.VISIBLE);
    }

    @AllArgsConstructor
    private class DeleteCallback
            implements Callback<GeneralResponse> {
        private Fragment fragment;

        @Override
        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            showText(R.string.success_delete);
            startFragment(fragment);
        }

        @Override
        public void onFailure(Call<GeneralResponse> call, Throwable t) {
            showError();
        }
    }

}
