package com.nsu.db.aircraft.view.tests.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class EquipmentMainFragment extends FragmentWithFragmentActivity {

    public EquipmentMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_equipment_main, container, false);
        setStartFragmentButton(view, R.id.button_equipment_show_all, new EquipmentAllFragment());
        setStartFragmentButton(view, R.id.button_add_equipment, new EquipmentDetailFragment());
        setStartFragmentButton(view, R.id.button_show_table, new EquipmentTableFragment());
        return view;
    }
}
