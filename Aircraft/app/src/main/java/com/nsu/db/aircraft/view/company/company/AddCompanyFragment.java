package com.nsu.db.aircraft.view.company.company;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.ErrorCause;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.Status;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddCompanyFragment extends Fragment {
    final static String WRONG_COMPANY_NAME_INPUT = "Неправильно введено название компании: " +
            "название состоит из букв русского или английского алфавита и пробелов";
    private final static String COMPANY_ALREADY_EXISTS_ERROR = "Предприятие с данным названием " +
            "уже существует";

    public AddCompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_company, container, false);
        setAddCompanyButton(view);
        return view;
    }

    private void setAddCompanyButton(View view) {
        Button addCompanyButton = view.findViewById(R.id.button_send_add_company);
        addCompanyButton.setOnClickListener(v -> {
            hideKeyboard();
            if (isNameFieldWrong()) {
                showInputError(WRONG_COMPANY_NAME_INPUT);
                return;
            }
            Company company = new Company(getEnteredName());
            NetworkService.getInstance()
                    .getCompanyJsonApi()
                    .addCompany(company)
                    .enqueue(new Callback<GeneralResponse<Company>>() {
                        @Override
                        public void onResponse(Call<GeneralResponse<Company>> call,
                                               Response<GeneralResponse<Company>> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getContext(), R.string.error_text, Toast.LENGTH_LONG)
                                        .show();
                                return;
                            }
                            checkResponse(response.body());
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse<Company>> call, Throwable t) {
                            Toast.makeText(getContext(), R.string.error_text, Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void checkResponse(GeneralResponse<?> serverResponse) {
        if (serverResponse.getStatus().equals(Status.OK)) {
            Toast.makeText(getContext(), R.string.load_success, Toast.LENGTH_LONG)
                    .show();
            hideErrorsIfPresent();
            return;
        }
        if (serverResponse.getCause().equals(ErrorCause.ALREADY_EXITS)) {
            showInputError(COMPANY_ALREADY_EXISTS_ERROR);
        }
    }

    private void hideErrorsIfPresent() {
        TextInputLayout textInputLayout = getView().findViewById(R.id.company_name_layout);
        textInputLayout.setErrorEnabled(false);
    }

    private void showInputError(String errorText) {
        TextInputLayout textInputLayout = getView().findViewById(R.id.company_name_layout);
        textInputLayout.setError(errorText);
    }

    private boolean isNameFieldWrong() {
        String name = getEnteredName();
        if (name.isEmpty()) {
            return true;
        }
        return !name.matches("^[a-zA-Zа-яА-Я ]*$");
    }

    private String getEnteredName() {
        TextInputEditText companyName = getView().findViewById(R.id.company_name_input);
        return companyName.getText().toString();
    }
}
