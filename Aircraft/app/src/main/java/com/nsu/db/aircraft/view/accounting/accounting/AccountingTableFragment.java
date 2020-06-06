package com.nsu.db.aircraft.view.accounting.accounting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountingTableFragment extends Fragment {

    public AccountingTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounting_table, container, false);
    }
}
