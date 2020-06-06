package com.nsu.db.aircraft.view.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.shared.sites.WorkTypesRequestFragment;


public class ProductDetailFragment extends FragmentWithFragmentActivity {
    private boolean isAddFragment = true;
    private Product product;
    private View view;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    public ProductDetailFragment(Product product) {
        this.product = product;
        isAddFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        if (isAddFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }
        return view;
    }

    private void setAddFragment() {

    }

    private void setDetailFragment() {
        setStartFragmentButton(view, R.id.button_works, new WorkTypesRequestFragment(product));
    }
}
