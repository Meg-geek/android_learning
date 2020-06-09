package com.nsu.db.aircraft.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nsu.db.aircraft.R;

import java.util.List;

public class FragmentWithFragmentActivity extends Fragment {
    protected final static String WRONG_NAME_INPUT = "Неправильно введено название: " +
            "название состоит из букв русского или английского алфавита и пробелов";
    private final static String REGEX_NAME = "^[a-zA-Zа-яА-Я ]*$";
    protected FragmentActivity fragmentActivity;

    public FragmentWithFragmentActivity() {

    }

    protected void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    protected void setStartFragmentButton(View view, int buttonId, Fragment fragment) {
        Button startFragmentButton = view.findViewById(buttonId);
        startFragmentButton.setOnClickListener(v -> startFragment(fragment));
    }

    protected void showError() {
        Toast.makeText(getContext(), R.string.error_text, Toast.LENGTH_LONG).show();
    }

    protected void showText(int textId) {
        Toast.makeText(getContext(), textId, Toast.LENGTH_LONG).show();
    }

    protected void showInputError(String errorText, int textInputLayoutId) {
        TextInputLayout textInputLayout = getView().findViewById(textInputLayoutId);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorText);
    }

    protected void hideInputError(int textInputLayoutId) {
        TextInputLayout textInputLayout = getView().findViewById(textInputLayoutId);
        textInputLayout.setErrorEnabled(false);
    }

    protected boolean isNameFieldWrong(int textInputEditTextId) {
        String name = getEnteredName(textInputEditTextId);
        if (name.isEmpty()) {
            return true;
        }
        return !name.matches(REGEX_NAME);
    }

    protected String getEnteredName(int textInputEditTextId) {
        TextInputEditText name = getView().findViewById(textInputEditTextId);
        return name.getText().toString();
    }

    protected void setVisibility(View view, int viewId, int visibility) {
        view.findViewById(viewId).setVisibility(visibility);
    }

    protected void setEnabledInputEditText(View view, int inputEditTextId, boolean enabled) {
        TextInputEditText stageName = view.findViewById(inputEditTextId);
        stageName.setEnabled(enabled);
    }

    protected void updateSpinner(View view, int spinnerId, List<String> resources) {
        Spinner spinner = view.findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                resources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    protected void setEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
    }
}
