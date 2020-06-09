package com.nsu.db.aircraft.view.staff.brigade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

public class BrigadeMainFragment extends FragmentWithFragmentActivity {

    public BrigadeMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brigade_main, container, false);
        setStartFragmentButton(view, R.id.button_show_brigades_table, new BrigadeTableFragment());
        setStartFragmentButton(view, R.id.button_brigade_show_all, new BrigadeAllFragment());
        setStartFragmentButton(view, R.id.button_add_brigade, new BrigadeDetailFragment());
        return view;
    }
}
