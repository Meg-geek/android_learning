package com.nsu.db.aircraft.view.accounting.accounting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class ProductAccountingMainFragment extends FragmentWithFragmentActivity {

    public ProductAccountingMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_product_accounting_main, container, false);
        setStartFragmentButton(view, R.id.button_accounting_show_all, new AccountingAllFragment());
        setStartFragmentButton(view, R.id.button_add_accounting, new AccountingDetailFragment());
        setStartFragmentButton(view, R.id.button_show_accounting_table, new AccountingTableFragment());
        return view;
    }
}
