package com.nsu.db.aircraft.view.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.staff.Employee;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaffDetailFragment extends Fragment {

    public StaffDetailFragment() {
        // Required empty public constructor
    }

    StaffDetailFragment(Employee employee) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_detail, container, false);
    }
}
