package com.nsu.db.aircraft.view.tests.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.tests.Equipment;
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
import static java.util.Arrays.asList;


public class EquipmentDetailFragment extends FragmentWithFragmentActivity {
    private Equipment equipment;
    private boolean isAddingFragment = true;
    private View view;
    private List<Range> ranges;

    public EquipmentDetailFragment() {
        // Required empty public constructor
    }

    public EquipmentDetailFragment(Equipment equipment) {
        this.equipment = equipment;
        this.isAddingFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equipment_detail, container, false);
        if (isAddingFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }
        return view;
    }

    private void setDetailFragment() {
        setVisibilityForDetail();
        setEnabledForDetail(false);
        setDetailData();
        setChangeButton();
        setSaveButton();
        setDeleteButton();
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> sendDeleteRequest());
    }

    private void sendDeleteRequest() {
        NetworkService.getInstance()
                .getEquipmentApi()
                .deleteById(equipment.getId())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.success_delete);
                        startFragment(new EquipmentMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setChangeButton() {
        Button changeButton = view.findViewById(R.id.button_change);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabledForDetail(true);
                sendRangesRequest();
                setVisibility(view, R.id.button_save, VISIBLE);
            }
        });
    }

    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            hideKeyboard();
            sendUpdateRequest();
        });
    }

    private void sendUpdateRequest() {
        equipment.setType(getEnteredName(R.id.type_edit_text));
        equipment.setRange(getSelectedRange());
        NetworkService.getInstance()
                .getEquipmentApi()
                .update(equipment)
                .enqueue(new Callback<GeneralResponse<Equipment>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Equipment>> call,
                                           Response<GeneralResponse<Equipment>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.update_success);
                        startFragment(new EquipmentMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Equipment>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setDetailData() {
        Range range = equipment.getRange();
        ranges = asList(range);
        updateSpinner(view, R.id.ranges_spinner, asList(range.toString()));
        TextInputEditText equipmentType = view.findViewById(R.id.type_edit_text);
        equipmentType.setText(equipment.getType());
    }

    private void setEnabledForDetail(boolean enabled) {
        setEnabled(view.findViewById(R.id.ranges_spinner), enabled);
        setEnabled(view.findViewById(R.id.type_edit_text), enabled);
    }

    private void setVisibilityForDetail() {
        setVisibility(view, R.id.button_add, INVISIBLE);
        setVisibility(view, R.id.button_save, INVISIBLE);
    }

    private void setAddFragment() {
        setVisibilityForAdd();
        sendRangesRequest();
        setAddButton();
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            if (getEnteredName(R.id.type_edit_text).isEmpty()) {
                showText(R.string.enter_type);
                return;
            }
            sendAddRequest();
            hideKeyboard();
        });
    }

    private void sendAddRequest() {
        equipment = new Equipment();
        equipment.setType(getEnteredName(R.id.type_edit_text));
        equipment.setRange(getSelectedRange());
        NetworkService.getInstance()
                .getEquipmentApi()
                .add(equipment)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.load_success);
                        startFragment(new EquipmentMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }

    private Range getSelectedRange() {
        Spinner rangesSpinner = view.findViewById(R.id.ranges_spinner);
        return ranges.get(rangesSpinner.getSelectedItemPosition());
    }

    private void sendRangesRequest() {
        NetworkService.getInstance()
                .getRangeApi()
                .getAll()
                .enqueue(new RangesCallback());
    }

    private void setVisibilityForAdd() {
        setVisibility(view, R.id.button_change, INVISIBLE);
        setVisibility(view, R.id.button_save, INVISIBLE);
        setVisibility(view, R.id.button_delete, INVISIBLE);
    }

    private class RangesCallback implements Callback<GeneralResponse<List<Range>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Range>>> call,
                               Response<GeneralResponse<List<Range>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            ranges = response.body().getData();
            List<String> rangesNames = ranges.stream()
                    .map(Range::toString).collect(Collectors.toList());
            updateSpinner(view, R.id.ranges_spinner, rangesNames);
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Range>>> call, Throwable t) {
            showError();
        }
    }
}
