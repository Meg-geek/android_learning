package com.nsu.db.aircraft.view.company;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;


public class AddCompanyFragment extends Fragment {

    public AddCompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_company, container, false);
        setAddCompanyButton(view);
        return view;
    }

    private void setAddCompanyButton(View view) {

    }
}
