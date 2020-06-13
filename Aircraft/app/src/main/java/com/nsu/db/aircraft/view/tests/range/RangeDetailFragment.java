package com.nsu.db.aircraft.view.tests.range;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.shared.products.ProductRequestDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class RangeDetailFragment extends FragmentWithFragmentActivity {
    private Range range;
    private boolean isAddFragment = true;
    private View view;
    private List<Guild> guilds;
    private List<Guild> selectedGuilds;

    public RangeDetailFragment() {
        // Required empty public constructor
    }

    public RangeDetailFragment(Range range) {
        this.range = range;
        this.isAddFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_range_detail, container, false);
        setProductsButton();
        setEquipmentButton();
        setTestersButton();
        setGuildsList();
        view.findViewById(R.id.range_detail).setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });
        if (isAddFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }
        return view;
    }

    private void setAddFragment() {
        setVisibilityForAdd();
        sendGuildsRequest();
        setAddButton();
    }

    private void setDetailFragment() {
        setVisibilityForDetail();
        setEnabledForDetail(false);
        setChangeButton();
        setDetailData();
        setSaveButton();
        setDeleteButton();
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> NetworkService.getInstance()
                .getRangeApi()
                .deleteById(range.getId())
                .enqueue(new GeneralResponseCallback()));
    }

    private void setDetailData() {
        TextInputEditText name = view.findViewById(R.id.name_edit_text);
        name.setText(range.getName());
        guilds = selectedGuilds = range.getGuilds();
        updateGuildsList();
    }

    private void setChangeButton() {
        Button changeButton = view.findViewById(R.id.button_change);
        changeButton.setOnClickListener(v -> {
            setEnabledForDetail(true);
            setVisibility(view, R.id.button_save, VISIBLE);
            sendGuildsRequest();
        });
    }

    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            if (isNameFieldWrong(R.id.name_edit_text)) {
                showInputError(INPUT_RULES, R.id.textInputLayout2);
                return;
            }
            hideInputError(R.id.textInputLayout2);
            sendUpdateRequest();
        });
    }

    private void sendUpdateRequest() {
        range.setName(getEnteredName(R.id.name_edit_text));
        range.setGuilds(selectedGuilds);
        NetworkService.getInstance()
                .getRangeApi()
                .update(range)
                .enqueue(new RangeCallback());
    }

    private void setVisibilityForDetail() {
        setVisibility(view, R.id.button_add, INVISIBLE);
        setVisibility(view, R.id.button_change, VISIBLE);
        setVisibility(view, R.id.button_delete, VISIBLE);
        setVisibility(view, R.id.button_products, VISIBLE);
        setVisibility(view, R.id.button_equipment, VISIBLE);
        setVisibility(view, R.id.button_testers, VISIBLE);
    }

    private void setEnabledForDetail(boolean enabled) {
        setEnabled(view.findViewById(R.id.name_edit_text), enabled);
        setEnabled(view.findViewById(R.id.guilds_list), enabled);
    }

    private void setGuildsList() {
        ListView guildsList = view.findViewById(R.id.guilds_list);
        guildsList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    SparseBooleanArray sparseBooleanArray = guildsList
                            .getCheckedItemPositions();
                    selectedGuilds = new ArrayList<>();
                    for (int i = 0; i < sparseBooleanArray.size(); i++) {
                        int key = sparseBooleanArray.keyAt(i);
                        if (sparseBooleanArray.get(key)) {
                            selectedGuilds.add(guilds.get(key));
                        }
                    }
                }
        );
    }

    private void setVisibilityForAdd() {
        setVisibility(view, R.id.button_change, INVISIBLE);
        setVisibility(view, R.id.button_delete, INVISIBLE);
        setVisibility(view, R.id.button_products, INVISIBLE);
        setVisibility(view, R.id.button_equipment, INVISIBLE);
        setVisibility(view, R.id.button_testers, INVISIBLE);
    }

    private void sendGuildsRequest() {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getAllGuilds()
                .enqueue(new GuildsCallback());
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameFieldWrong(R.id.name_edit_text)) {
                    showInputError(INPUT_RULES, R.id.textInputLayout2);
                    return;
                }
                hideInputError(R.id.textInputLayout2);
                hideKeyboard();
                sendAddRequest();
            }
        });
    }

    private void sendAddRequest() {
        range = new Range();
        range.setName(getEnteredName(R.id.name_edit_text));
        range.setGuilds(selectedGuilds);
        NetworkService.getInstance()
                .getRangeApi()
                .add(range)
                .enqueue(new RangeCallback());
    }

    private void setProductsButton() {
        ProductRequestDetails productRequestDetails = new ProductRequestDetails(range);
        productRequestDetails.setProductsForm(true);
        setStartFragmentButton(view, R.id.button_products, productRequestDetails);
    }

    private void setEquipmentButton() {
        ProductRequestDetails productRequestDetails = new ProductRequestDetails(range);
        productRequestDetails.setEquipmentForm(true);
        setStartFragmentButton(view, R.id.button_equipment, productRequestDetails);
    }

    private void setTestersButton() {
        ProductRequestDetails productRequestDetails = new ProductRequestDetails(range);
        productRequestDetails.setTestersForm(true);
        setStartFragmentButton(view, R.id.button_testers, productRequestDetails);
    }

    private void updateGuildsList() {
        ListView guildsList = view.findViewById(R.id.guilds_list);
        List<String> guildNames = guilds.stream()
                .map(Guild::getGuildName).collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, guildNames);
        guildsList.setAdapter(namesAdapter);
        if (selectedGuilds != null) {
            setSelectionForGuilds();
        }
        namesAdapter.notifyDataSetChanged();
    }

    private void setSelectionForGuilds() {
        ListView guildsList = view.findViewById(R.id.guilds_list);
        for (int i = 0; i < guilds.size(); i++) {
            if (selectedGuilds.contains(guilds.get(i))) {
                guildsList.setSelection(i);
            }
        }
    }

    private class GuildsCallback implements Callback<GeneralResponse<List<Guild>>> {
        private boolean isChangeSelected = false;

        @Override
        public void onResponse(Call<GeneralResponse<List<Guild>>> call,
                               Response<GeneralResponse<List<Guild>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            guilds = response.body().getData();
            updateGuildsList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Guild>>> call, Throwable t) {
            showError();
        }
    }


    private class RangeCallback implements Callback<GeneralResponse<Range>> {
        @Override
        public void onResponse(Call<GeneralResponse<Range>> call, Response<GeneralResponse<Range>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            if (isAddFragment) {
                showText(R.string.load_success);
            } else {
                showText(R.string.update_success);
            }
            startFragment(new RangeMainFragment());
        }

        @Override
        public void onFailure(Call<GeneralResponse<Range>> call, Throwable t) {
            showError();
        }
    }

    private class GeneralResponseCallback implements Callback<GeneralResponse> {

        @Override
        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            if (isAddFragment) {
                showText(R.string.load_success);
            } else {
                showText(R.string.success_delete);
            }
            startFragment(new RangeMainFragment());
        }

        @Override
        public void onFailure(Call<GeneralResponse> call, Throwable t) {
            showError();

        }
    }
}
