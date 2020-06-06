package com.nsu.db.aircraft.view.tests.range;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.shared.products.ProductRequestDetails;


public class RangeDetailFragment extends FragmentWithFragmentActivity {
    private Range range;
    private boolean isAddFragment = true;

    public RangeDetailFragment() {
        // Required empty public constructor
    }

    public RangeDetailFragment(Range range) {
        this.range = range;
        this.isAddFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_range_detail, container, false);
        setStartFragmentButton(view, R.id.button_products, new ProductRequestDetails(range));
        return view;
    }
}
