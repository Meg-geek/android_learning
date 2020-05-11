package com.nsu.db.aircraft.view.company.company;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nsu.db.aircraft.R;


public class CompanyFragment extends Fragment {

    private FragmentActivity fragmentActivity;

    public CompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        setShowAllButton(view);
        setAddCompanyButton(view);
        setShowTableButton(view);
        return view;
    }

    private void setAddCompanyButton(View view) {
        Button addCompanyButton = view.findViewById(R.id.button_add_company);
        addCompanyButton.setOnClickListener(v -> startFragment(new AddCompanyFragment()));
    }

    private void setShowAllButton(View view) {
        Button showAllButton = view.findViewById(R.id.button_company_show_all);
        showAllButton.setOnClickListener(view1 -> startFragment(new CompanyAllFragment()));
    }

    private void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setShowTableButton(View view) {
        Button showTableButton = view.findViewById(R.id.button_show_companies_table);
        showTableButton.setOnClickListener(v -> startFragment(new CompanyTableFragment()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }


}
