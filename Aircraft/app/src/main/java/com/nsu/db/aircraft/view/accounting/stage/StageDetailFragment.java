package com.nsu.db.aircraft.view.accounting.stage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Stage;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class StageDetailFragment extends FragmentWithFragmentActivity {
    private Stage stage;
    private boolean isAddingFragment = true;
    private View view;

    public StageDetailFragment() {
        // Required empty public constructor
    }

    public StageDetailFragment(Stage stage) {
        this.stage = stage;
        this.isAddingFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage_detail, container, false);
        this.view = view;
        if (isAddingFragment) {
            setAddingFragment();
        } else {
            setEditFragment();
        }
        return view;
    }

    private void setAddingFragment() {
        Button addButton = view.findViewById(R.id.stage_add);
        addButton.setVisibility(VISIBLE);
        addButton.setOnClickListener(v -> sendAddRequest());
    }

    private void sendAddRequest() {
        if (isNameFieldWrong(R.id.detail_stage_name)) {
            showInputError(WRONG_NAME_INPUT, R.id.stage_text_input);
            return;
        }
        NetworkService.getInstance()
                .getStageApi()
                .add(new Stage(getEnteredName(R.id.detail_stage_name)))
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.isSuccessful()) {
                            showText(R.string.load_success);
                        }
                        hideKeyboard();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setEditFragment() {
        setButtonsVisibility();
        setStageName();
        setChangeButton();
        setSaveButton();
        setDeleteButton();
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.stage_delete);
        deleteButton.setOnClickListener(v -> sendDeleteRequest());
    }

    private void sendDeleteRequest() {
        NetworkService.getInstance()
                .getStageApi()
                .deleteById(stage.getId())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.success_delete);
                        startFragment(new StageMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.stage_save);
        saveButton.setOnClickListener(v -> {
            hideKeyboard();
            if (isNameFieldWrong(R.id.detail_stage_name)) {
                showInputError(WRONG_NAME_INPUT, R.id.stage_text_input);
                return;
            }
            hideInputError(R.id.stage_text_input);
            sendUpdateRequest();
        });
    }

    private void sendUpdateRequest() {
        stage.setStageName(getEnteredName(R.id.detail_stage_name));
        NetworkService.getInstance()
                .getStageApi()
                .update(stage)
                .enqueue(new Callback<GeneralResponse<Stage>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Stage>> call,
                                           Response<GeneralResponse<Stage>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        stage = response.body().getData();
                        setStageName();
                        setVisibility(view, R.id.stage_save, INVISIBLE);
                        showText(R.string.update_success);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Stage>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setStageName() {
        TextInputEditText stageName = view.findViewById(R.id.detail_stage_name);
        setEnabledInputEditText(view, R.id.detail_stage_name, false);
        stageName.setText(stage.getStageName());
    }

    private void setChangeButton() {
        Button changeButton = view.findViewById(R.id.stage_change);
        changeButton.setOnClickListener(v -> {
            setVisibility(view, R.id.stage_save, VISIBLE);
            setEnabledInputEditText(view, R.id.detail_stage_name, true);
        });
    }

    private void setButtonsVisibility() {
        setVisibility(view, R.id.stage_change, VISIBLE);
        setVisibility(view, R.id.stage_delete, VISIBLE);
    }
}
