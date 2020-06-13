package com.nsu.db.aircraft.view.tests.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class TestMainFragment extends FragmentWithFragmentActivity {

    public TestMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_main, container, false);
        setStartFragmentButton(view, R.id.button_show_all, new TestAllFragment());
        setStartFragmentButton(view, R.id.button_add, new TestDetailFragment());
        setStartFragmentButton(view, R.id.button_show_table, new TestTableFragment());
        return view;
    }
}
