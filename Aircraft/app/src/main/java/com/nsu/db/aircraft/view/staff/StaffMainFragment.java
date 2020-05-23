package com.nsu.db.aircraft.view.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class StaffMainFragment extends FragmentWithFragmentActivity {

    public StaffMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_main, container, false);
        setStartFragmentButton(view, R.id.button_staff_show_all, new StaffAllFragment());
        return view;
    }
}
