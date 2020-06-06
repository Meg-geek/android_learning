package com.nsu.db.aircraft.view.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;


public class ProductsMainFragment extends FragmentWithFragmentActivity {

    public ProductsMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_main, container, false);
        setStartFragmentButton(view, R.id.button_products_show_all, new AllProductsFragment());
        setStartFragmentButton(view, R.id.button_add_product, new ProductDetailFragment());
        setStartFragmentButton(view, R.id.button_show_products_table, new ProductTableFragment());
        return view;
    }
}
