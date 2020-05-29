package com.nsu.db.aircraft.view.accounting.stage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class StageMainFragment extends FragmentWithFragmentActivity {

    public StageMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage_main, container, false);
        setStartFragmentButton(view, R.id.button_stage_show_all, new StageAllFragment());
        setStartFragmentButton(view, R.id.button_add_stage, new StageDetailFragment());
        setStartFragmentButton(view, R.id.button_show_stages_table, new StageTableFragment());
        return view;
    }
}
