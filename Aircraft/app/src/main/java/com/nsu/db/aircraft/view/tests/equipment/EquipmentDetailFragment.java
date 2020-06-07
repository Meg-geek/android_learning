package com.nsu.db.aircraft.view.tests.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.tests.Equipment;


public class EquipmentDetailFragment extends Fragment {
    private Equipment equipment;

    public EquipmentDetailFragment() {
        // Required empty public constructor
    }

    public EquipmentDetailFragment(Equipment equipment) {
        this.equipment = equipment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_equipment_detail, container, false);
    }
}
