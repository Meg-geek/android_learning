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
    private View view;

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
        view = inflater.inflate(R.layout.fragment_range_detail, container, false);
        setProductsButton();
        setEquipmentButton();
        setTestersButton();
        return view;
    }

    private void setProductsButton() {
        ProductRequestDetails productRequestDetails = new ProductRequestDetails(range);
        productRequestDetails.setProductsForm(true);
        setStartFragmentButton(view, R.id.button_products, productRequestDetails);
    }

    private void setEquipmentButton() {
        ProductRequestDetails productRequestDetails = new ProductRequestDetails(range);
        productRequestDetails.setEquipmentForm(true);
        setStartFragmentButton(view, R.id.button_equipment, productRequestDetails);
    }

    private void setTestersButton() {
        ProductRequestDetails productRequestDetails = new ProductRequestDetails(range);
        productRequestDetails.setTestersForm(true);
        setStartFragmentButton(view, R.id.button_testers, productRequestDetails);
    }
}
