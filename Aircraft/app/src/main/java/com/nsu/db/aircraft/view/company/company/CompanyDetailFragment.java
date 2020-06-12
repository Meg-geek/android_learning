package com.nsu.db.aircraft.view.company.company;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.Status;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.company.guild.GuildDetailFragment;
import com.nsu.db.aircraft.view.shared.products.ProductRequestDetails;
import com.nsu.db.aircraft.view.shared.sites.SitesRequestFragment;
import com.nsu.db.aircraft.view.shared.staff.StaffRequestFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyDetailFragment extends FragmentWithFragmentActivity {
    private Company company;
    private List<Guild> guilds;

    public CompanyDetailFragment() {
        // Required empty public constructor
    }

    CompanyDetailFragment(Company company) {
        this.company = company;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_detail, container, false);
        putCompanyInfo(view);
        setChangeNameButton(view);
        setSaveButton(view);
        setDeleteButton(view);
        fillGuilds(view);
        setGuildsListView(view);
        setStartFragmentButton(view, R.id.button_products, new ProductRequestDetails(company));
        setStartFragmentButton(view, R.id.button_staff, new StaffRequestFragment(company));
        setStartFragmentButton(view, R.id.button_sites, new SitesRequestFragment(company));
        return view;
    }

    private void setGuildsListView(View view) {
        ListView guildsView = view.findViewById(R.id.company_detail_guilds);
        guildsView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < 0 || guilds == null || position >= guilds.size()) {
                return;
            }
            startFragment(new GuildDetailFragment(guilds.get(position)));
        });
    }

    private void fillGuilds(View view) {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getByCompanyId(company.getId())
                .enqueue(new Callback<GeneralResponse<List<Guild>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Guild>>> call,
                                           Response<GeneralResponse<List<Guild>>> response) {
                        if (!response.isSuccessful()) {
                            showToast(R.string.error_text);
                            return;
                        }
                        guilds = response.body().getData();
                        if (guilds == null) {
                            return;
                        }
                        updateGuildsListView();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Guild>>> call, Throwable t) {
                        showToast(R.string.error_text);
                    }
                });
    }

    private void updateGuildsListView() {
        ListView guildsList = getView().findViewById(R.id.company_detail_guilds);
        List<String> guildNames = new ArrayList<>();
        guilds.forEach(guild -> guildNames.add(guild.getGuildName()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, guildNames);
        guildsList.setAdapter(namesAdapter);
        TextView textView = getView().findViewById(R.id.detail_company_load_text);
        textView.setVisibility(View.INVISIBLE);
    }

    private void setDeleteButton(View view) {
        Button deleteButton = view.findViewById(R.id.button_delete_detail_guild);
        deleteButton.setOnClickListener(v -> showDialog());
    }

    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fragmentActivity);
        dialogBuilder.setMessage(R.string.delete_company_dialog_message)
                .setTitle(R.string.delete_dialog_title);
        dialogBuilder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> deleteCompany());
        dialogBuilder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> dialog.cancel());
        dialogBuilder.create().show();
    }

    private void deleteCompany() {
        NetworkService.getInstance()
                .getCompanyJsonApi()
                .deleteCompanyByName(company.getName())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showToast(R.string.error_text);
                            return;
                        }
                        if (response.body().getStatus().equals(Status.OK)) {
                            showToast(R.string.company_success_delete);
                            changeToCompanyFragment();
                            return;
                        }
                        showToast(R.string.error_text);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showToast(R.string.error_text);
                    }
                });
    }

    private void changeToCompanyFragment() {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new CompanyFragment());
        fragmentTransaction.commit();
    }

    private void showToast(int stringId) {
        Toast.makeText(getContext(), stringId, Toast.LENGTH_LONG).show();
    }

    private void setSaveButton(View view) {
        Button saveButton = view.findViewById(R.id.button_detail_guild_save);
        saveButton.setOnClickListener(v -> {
            hideKeyboard();
            if (isNameFieldWrong()) {
                showInputError();
                return;
            }
            NetworkService.getInstance()
                    .getCompanyJsonApi()
                    .changeCompanyName(company, getEnteredName())
                    .enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            if (!response.isSuccessful()) {
                                showToast(R.string.error_text);
                                return;
                            }
                            if (response.body().getStatus().equals(Status.OK)) {
                                showToast(R.string.success_company_name_change);
                                hideKeyboard();
                                return;
                            }
                            showToast(R.string.error_text);
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            showToast(R.string.error_text);
                        }
                    });
        });
    }

    private void showInputError() {
        TextInputLayout textInputLayout = getView().findViewById(R.id.company_detail_text_input);
        textInputLayout.setError(AddCompanyFragment.WRONG_COMPANY_NAME_INPUT);
    }


    private boolean isNameFieldWrong() {
        String name = getEnteredName();
        if (name.isEmpty()) {
            return true;
        }
        return !name.matches("^[a-zA-Zа-яА-Я ]*$")
                || name.matches("^[ ]*$");
    }

    private String getEnteredName() {
        TextInputEditText companyName = getView().findViewById(R.id.detail_guild_name);
        return companyName.getText().toString();
    }


    private void setChangeNameButton(View view) {
        Button changeNameButton = view.findViewById(R.id.button_change_guild_name);
        changeNameButton.setOnClickListener(v -> {
            TextInputEditText companyName = view.findViewById(R.id.detail_guild_name);
            companyName.setEnabled(true);
            Button saveButton = view.findViewById(R.id.button_detail_guild_save);
            saveButton.setVisibility(View.VISIBLE);
        });
    }

    private void putCompanyInfo(View view) {
        setCompanyNameField(view);
        //цеха
    }

    private void setCompanyNameField(View view) {
        TextInputEditText companyName = view.findViewById(R.id.detail_guild_name);
        companyName.setText(company.getName());
        companyName.setEnabled(false);
    }
}
