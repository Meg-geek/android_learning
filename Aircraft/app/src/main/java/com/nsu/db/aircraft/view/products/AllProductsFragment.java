package com.nsu.db.aircraft.view.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllProductsFragment extends FragmentWithFragmentActivity {
    private List<Product> products;

    public AllProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_products, container, false);
        setProductsSpinner(view);
        setShowButton(view);
        setProductsListView(view);
        return view;
    }

    private void setProductsListView(View view) {
        ListView productListView = view.findViewById(R.id.product_list_view);
        productListView.setOnItemClickListener((parent, v, position, id) ->
                startFragment(new ProductDetailFragment(products.get(position))));
    }

    private void setProductsSpinner(View view) {
        Spinner productsSpinner = view.findViewById(R.id.products_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                Product.categoriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productsSpinner.setAdapter(adapter);
    }

    private void setShowButton(View view) {
        Button showButton = view.findViewById(R.id.show_products);
        showButton.setOnClickListener(v -> {
            showLoadText(view);
            String selectedCategory = getSpinnerValue(view);
            sendRequest(selectedCategory, false);
        });
    }

    private void showLoadText(View view) {
        TextView loadText = view.findViewById(R.id.load_text);
        loadText.setVisibility(View.VISIBLE);
    }

    private void sendRequest(String selectedCategory, boolean isUpdatable) {
        if (selectedCategory.equals(Product.ALL)) {
            sendAllProductsRequest();
            return;
        }
        Call<GeneralResponse<List<Product>>> productsCall;
        switch (selectedCategory) {
            case Product.ROCKET:
                productsCall = NetworkService.getInstance()
                        .getRocketApi()
                        .getAll();
                break;
            case Product.HANG_GLIDER:
                productsCall = NetworkService.getInstance()
                        .getHangGliderApi()
                        .getAll();
                break;
            case Product.HELICOPTER:
                productsCall = NetworkService.getInstance()
                        .getHelicopterApi()
                        .getAll();
                break;
            default:
                productsCall = NetworkService.getInstance()
                        .getPlaneApi()
                        .getAll();
                break;
        }
        productsCall.enqueue(new ProductsCallBack(selectedCategory, isUpdatable));
    }

    private void sendAllProductsRequest() {
        products = new ArrayList<>();
        sendRequest(Product.HANG_GLIDER, true);
        sendRequest(Product.HELICOPTER, true);
        sendRequest(Product.PLANE, true);
        sendRequest(Product.ROCKET, true);
    }


    private String getSpinnerValue(View view) {
        Spinner productsSpinner = view.findViewById(R.id.products_spinner);
        return (String) productsSpinner.getSelectedItem();
    }

    private void updateProductsList() {
        ListView productListView = getView().findViewById(R.id.product_list_view);
        List<String> productNames = new ArrayList<>();
        products.forEach(product -> productNames.add(product.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, productNames);
        productListView.setAdapter(namesAdapter);
        TextView textView = getView().findViewById(R.id.load_text);
        textView.setVisibility(View.INVISIBLE);
    }

    private class ProductsCallBack implements Callback<GeneralResponse<List<Product>>> {
        private String productCategory;
        private boolean isUpdatable;

        ProductsCallBack(String productCategory, boolean isUpdatable) {
            this.productCategory = productCategory;
            this.isUpdatable = isUpdatable;
        }


        @Override
        public void onResponse(Call<GeneralResponse<List<Product>>> call,
                               Response<GeneralResponse<List<Product>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Product> responseProducts = response.body().getData();
            responseProducts.forEach(rocket -> rocket.setProductCategory(productCategory));
            if (isUpdatable) {
                products.addAll(responseProducts);
            } else {
                products = responseProducts;
            }
            updateProductsList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Product>>> call, Throwable t) {
            showError();
        }
    }

}
