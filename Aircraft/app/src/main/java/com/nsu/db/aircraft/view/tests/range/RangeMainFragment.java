package com.nsu.db.aircraft.view.tests.range;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class RangeMainFragment extends FragmentWithFragmentActivity {

    public RangeMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_range_main, container, false);
        setStartFragmentButton(view, R.id.button_add_range, new RangeDetailFragment());
        setStartFragmentButton(view, R.id.button_show_ranges_table, new RangeTableFragment());
        setStartFragmentButton(view, R.id.button_range_show_all, new RangeAllFragment());
        return view;
    }
}
