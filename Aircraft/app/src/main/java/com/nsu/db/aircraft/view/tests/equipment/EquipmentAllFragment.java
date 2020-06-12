package com.nsu.db.aircraft.view.tests.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.tests.Equipment;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EquipmentAllFragment extends FragmentWithFragmentActivity {
    private View view;
    private List<Equipment> equipment;


    public EquipmentAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equipment_all, container, false);
        setEquipmentList();
        sendEquipmentRequest();
        return view;
    }

    private void setEquipmentList() {
        ListView equipmentList = view.findViewById(R.id.list_view);
        equipmentList.setOnItemClickListener((parent, view, position, id) ->
                startFragment(new EquipmentDetailFragment(equipment.get(position))));
    }

    private void sendEquipmentRequest() {
        NetworkService.getInstance()
                .getEquipmentApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Equipment>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Equipment>>> call,
                                           Response<GeneralResponse<List<Equipment>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        equipment = response.body().getData();
                        updateEquipmentList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Equipment>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateEquipmentList() {
        ListView equipmentList = view.findViewById(R.id.list_view);
        List<String> equipmentTypes = equipment.stream()
                .map(Equipment::getType).collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, equipmentTypes);
        equipmentList.setAdapter(namesAdapter);
    }
}
