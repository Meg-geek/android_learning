package com.nsu.db.aircraft.view.company.site;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class SiteMainFragment extends FragmentWithFragmentActivity {

    public SiteMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site_main, container, false);
        setStartFragmentButton(view, R.id.button_site_show_all, new SiteAllFragment());
        setStartFragmentButton(view, R.id.button_add_site, new SiteDetailFragment());
        setStartFragmentButton(view, R.id.button_show_sites_table, new SiteTableFragment());
        return view;
    }
}
