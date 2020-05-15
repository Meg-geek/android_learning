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
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;


public class GuildDetailFragment extends FragmentWithFragmentActivity {
    private static final String COMPANY_LOAD_ERROR = "Не удалось загрузить список компаний";
    private static final String COMPANY_SPINNER_ERROR = "Выберите предприятие для цеха";
    private final static String WRONG_GUILD_NAME_INPUT = "Неправильно введено название предприятия: " +
            "название состоит из букв русского или английского алфавита и пробелов";
    private final static String GUILD_ADD_ERROR = "Не удалось добавить цех";
    private final static String GUILD_SUCCESSFUL_ADD = "Цех успешно добавлен";

    private Guild guild;
    private List<Company> companies;
    private boolean isAddFragment = true;

    public GuildDetailFragment() {
        // Required empty public constructor
    }

    GuildDetailFragment(Guild guild) {
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
        sendGuildManagerRequest();
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
        Guild guild = new Guild(getEnteredName(), getCompany(view));
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
        String name = getEnteredName();
        if (name.isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Zа-яА-Я ]*$");
    }

    private String getEnteredName() {
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
        view.findViewById(R.id.load_company).setVisibility(INVISIBLE);
    }

    private void showTextOnScreen(String errorText) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_LONG).show();
    }

    private void sendGuildManagerRequest() {

    }

    private void setDetailFragment(View view) {
        setVisibilityForDetailChange(view);
    }

    private void setVisibilityForDetailChange(View view) {
        view.findViewById(R.id.button_add_guild).setVisibility(INVISIBLE);
    }

    private void setVisibilityForAdd(View view) {
        view.findViewById(R.id.button_change_guild_name).setVisibility(INVISIBLE);
        view.findViewById(R.id.button_change_guild_company).setVisibility(INVISIBLE);
        view.findViewById(R.id.button_change_guild_manager).setVisibility(INVISIBLE);
        view.findViewById(R.id.button_delete_detail_guild).setVisibility(INVISIBLE);

        view.findViewById(R.id.load_guild_name).setVisibility(INVISIBLE);
    }
}
