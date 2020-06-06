package com.nsu.db.aircraft.view.shared.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class StaffRequestFragment extends FragmentWithFragmentActivity {
    private Company company;
    private Guild guild;
    private Site site;

    private View view;

    public StaffRequestFragment() {
        // Required empty public constructor
    }

    public StaffRequestFragment(Company company) {
        this.company = company;
    }

    public StaffRequestFragment(Guild guild) {
        this.guild = guild;
    }

    public StaffRequestFragment(Site site) {
        this.site = site;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_request, container, false);
        updateSpinner(view, R.id.spinner, Employee.categoriesAndMaster);
        return view;
    }
}
