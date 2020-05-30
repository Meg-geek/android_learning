package com.nsu.db.aircraft.view.company.guild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.Status;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.util.Arrays.asList;


public class GuildDetailFragment extends FragmentWithFragmentActivity {
    private static final String COMPANY_LOAD_ERROR = "Не удалось загрузить список компаний";
    private static final String COMPANY_SPINNER_ERROR = "Выберите предприятие для цеха";
    private final static String WRONG_GUILD_NAME_INPUT = "Неправильно введено название предприятия: " +
            "название состоит из букв русского или английского алфавита и пробелов";
    private final static String GUILD_ADD_ERROR = "Не удалось добавить цех";
    private final static String GUILD_SUCCESSFUL_ADD = "Цех успешно добавлен";

    private Guild guild;
    private List<Company> companies;
    private List<Employee> guildManagers = new ArrayList<>();
    private boolean isAddFragment = true;

    public GuildDetailFragment() {
        // Required empty public constructor
    }

    public GuildDetailFragment(Guild guild) {
        this.guild = guild;
        this.isAddFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guild_detail, container, false);
        if (isAddFragment) {
            setAddFragment(view);
        } else {
            setDetailFragment(view);
        }
        return view;
    }

    private void setAddFragment(View view) {
        setVisibilityForAdd(view);
        sendCompaniesRequest(view);
        setAddGuildButton(view);
    }

    private void setAddGuildButton(View view) {
        Button addGuildButton = view.findViewById(R.id.button_add_guild);
        addGuildButton.setOnClickListener(v -> {
            hideKeyboard();
            if (areGuildFieldsCorrect(view)) {
                hideErrorsIfPresent();
                sendAddGuildRequest(view);
            }
        });
    }

    private void sendAddGuildRequest(View view) {
        hideKeyboard();
        Guild guild = new Guild(getEnteredGuildName(), getCompany(view));
        NetworkService.getInstance()
                .getGuildJsonApi()
                .addGuild(guild)
                .enqueue(new Callback<GeneralResponse<Guild>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Guild>> call,
                                           Response<GeneralResponse<Guild>> response) {
                        if (!response.isSuccessful()) {
                            showTextOnScreen(GUILD_ADD_ERROR);
                            return;
                        }
                        if (!response.body().getStatus().equals(Status.OK)) {
                            showTextOnScreen(GUILD_ADD_ERROR);
                            return;
                        }
                        showTextOnScreen(GUILD_SUCCESSFUL_ADD);
                        startFragment(new GuildMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Guild>> call, Throwable t) {
                        showTextOnScreen(GUILD_ADD_ERROR);
                    }
                });
    }

    private Company getCompany(View view) {
        Spinner companySpinner = view.findViewById(R.id.companies_spinner);
        return new Company((String) companySpinner.getSelectedItem());
    }

    private boolean areGuildFieldsCorrect(View view) {
        if (!isGuildNameCorrect(view)) {
            showInputError(WRONG_GUILD_NAME_INPUT);
            return false;
        }
        if (!isSpinnerCorrect(view)) {
            showTextOnScreen(COMPANY_SPINNER_ERROR);
            return false;
        }
        return true;
    }

    private void hideErrorsIfPresent() {
        TextInputLayout textInputLayout = getView().findViewById(R.id.guild_detail_text_input);
        textInputLayout.setErrorEnabled(false);
    }

    private void showInputError(String errorText) {
        TextInputLayout textInputLayout = getView().findViewById(R.id.guild_detail_text_input);
        textInputLayout.setError(errorText);
    }

    private boolean isGuildNameCorrect(View view) {
        String name = getEnteredGuildName();
        if (name.isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Zа-яА-Я ]*$");
    }

    private String getEnteredGuildName() {
        TextInputEditText guildName = getView().findViewById(R.id.detail_guild_name);
        return guildName.getText().toString();
    }

    private boolean isSpinnerCorrect(View view) {
        Spinner company = view.findViewById(R.id.companies_spinner);
        return company.getSelectedItem() != null;
    }

    private void sendCompaniesRequest(View view) {
        NetworkService.getInstance()
                .getCompanyJsonApi()
                .getAllCompanies()
                .enqueue(new Callback<GeneralResponse<List<Company>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Company>>> call,
                                           Response<GeneralResponse<List<Company>>> response) {
                        if (!response.isSuccessful()) {
                            showTextOnScreen(COMPANY_LOAD_ERROR);
                            return;
                        }
                        if (response.body().getStatus().equals(Status.OK)) {
                            companies = response.body().getData();
                            showCompanies(view);
                            return;
                        }
                        showTextOnScreen(COMPANY_LOAD_ERROR);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Company>>> call,
                                          Throwable t) {
                        showTextOnScreen(COMPANY_LOAD_ERROR);
                    }
                });
    }

    private void showCompanies(View view) {
        Spinner companiesSpinner = view.findViewById(R.id.companies_spinner);
        List<String> companyNames = companies.stream()
                .map(Company::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                companyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companiesSpinner.setAdapter(adapter);
    }

    private void setManagers(View view) {
        Spinner managersSpinner = view.findViewById(R.id.guild_manager_spinner);
        List<String> managerNames = guildManagers.stream()
                .map(manager -> manager.getName() + manager.getSurname())
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                managerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        managersSpinner.setAdapter(adapter);
    }

    private void showTextOnScreen(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_LONG).show();
    }

    private void setDetailFragment(View view) {
        setVisibilityForDetailChange(view);
        updateFields(view);
        setDetailButtons(view);
    }

    private void setDetailButtons(View view) {
        setDeleteButton(view);
        setChangeButton(view);
        setSaveButton(view);
    }

    private void setDeleteButton(View view) {
        Button deleteButton = view.findViewById(R.id.button_delete_detail_guild);
        deleteButton.setOnClickListener(v -> NetworkService.getInstance()
                .getGuildJsonApi()
                .deleteById(guild.getId())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.success_delete);
                        startFragment(new GuildMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                }));
    }

    private void setChangeButton(View view) {
        setEnabledInputEditText(view, R.id.detail_guild_name, true);
        setVisibility(view, R.id.button_save_all_guild_changes, VISIBLE);
        Spinner companySpinner = view.findViewById(R.id.companies_spinner);
        companySpinner.setEnabled(true);
        Spinner managerSpinner = view.findViewById(R.id.guild_manager_spinner);
        managerSpinner.setEnabled(true);
        loadFreeManagers();
        sendCompaniesRequest(view);
    }

    private void loadFreeManagers() {
        NetworkService.getInstance()
                .getEngineeringStaffApi()
                .getFreeManagersForGuild(guild.getId())
                .enqueue(new Callback<GeneralResponse<List<Employee>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                                           Response<GeneralResponse<List<Employee>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        List<Employee> freeManagers = response.body().getData();
                        guildManagers.addAll(freeManagers);
                        setManagers(getView());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setSaveButton(View view) {
        Button saveButton = view.findViewById(R.id.button_save_all_guild_changes);
        saveButton.setOnClickListener(v -> NetworkService.getInstance()
                .getGuildJsonApi()
                .updateGuild(getGuildFromForm())
                .enqueue(new Callback<GeneralResponse<Guild>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Guild>> call,
                                           Response<GeneralResponse<Guild>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        guild = response.body().getData();
                        showText(R.string.update_success);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Guild>> call, Throwable t) {
                        showError();
                    }
                }));
    }

    private Guild getGuildFromForm() {
        Guild newGuild = new Guild();
        newGuild.setGuildName(getEnteredGuildName());
        Spinner companySpinner = getView().findViewById(R.id.companies_spinner);
        newGuild.setCompany(companies.get(companySpinner.getSelectedItemPosition()));
        return newGuild;
    }


    private void updateFields(View view) {
        setGuildName(view);
        setCompany(view);
        setGuildManager(view);
    }

    private void setGuildManager(View view) {
        Spinner managerSpinner = view.findViewById(R.id.guild_manager_spinner);
        managerSpinner.setEnabled(false);
        sendManagerRequest(view);
    }

    private void sendManagerRequest(View view) {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getGuildManagerByGuildId(guild.getId())
                .enqueue(new Callback<GeneralResponse<Employee>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Employee>> call,
                                           Response<GeneralResponse<Employee>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        Employee manager = response.body().getData();
                        if (manager.getName() == null || manager.getName().isEmpty()) {
                            return;
                        }
                        guildManagers.add(manager);
                        setManagers(view);
                        Spinner managerSpinner = view.findViewById(R.id.guild_manager_spinner);
                        managerSpinner.setSelection(0);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Employee>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setCompany(View view) {
        companies = asList(guild.getCompany());
        showCompanies(view);
        Spinner companySpinner = view.findViewById(R.id.companies_spinner);
        companySpinner.setEnabled(false);
        companySpinner.setSelection(0);
    }

    private void setGuildName(View view) {
        TextInputEditText guildName = view.findViewById(R.id.detail_guild_name);
        setEnabledInputEditText(view, R.id.detail_guild_name, false);
        guildName.setText(guild.getGuildName());
    }


    private void setVisibilityForDetailChange(View view) {
        setVisibility(view, R.id.button_change_guild_details, VISIBLE);
        setVisibility(view, R.id.textView9, VISIBLE);
        setVisibility(view, R.id.guild_manager_spinner, VISIBLE);

        setVisibility(view, R.id.button_add_guild, INVISIBLE);
        setVisibility(view, R.id.button_save_all_guild_changes, INVISIBLE);
        setVisibility(view, R.id.button_delete_detail_guild, VISIBLE);
    }

    private void setVisibilityForAdd(View view) {
        setVisibility(view, R.id.button_change_guild_details, INVISIBLE);
        setVisibility(view, R.id.textView9, INVISIBLE);
        setVisibility(view, R.id.guild_manager_spinner, INVISIBLE);

        setVisibility(view, R.id.button_add_guild, VISIBLE);
        setVisibility(view, R.id.button_save_all_guild_changes, INVISIBLE);
        setVisibility(view, R.id.button_delete_detail_guild, INVISIBLE);
    }
}
