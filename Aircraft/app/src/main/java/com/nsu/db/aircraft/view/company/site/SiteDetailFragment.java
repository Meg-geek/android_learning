package com.nsu.db.aircraft.view.company.site;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.company.Site;


public class SiteDetailFragment extends Fragment {
    private View view;

    public SiteDetailFragment() {
        // Required empty public constructor
    }

    SiteDetailFragment(Site site) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_site_detail, container, false);
        return view;
    }
}
