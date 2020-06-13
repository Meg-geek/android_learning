package com.nsu.db.aircraft.view.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.rest.products.HangGliderApi;
import com.nsu.db.aircraft.api.rest.products.HelicopterApi;
import com.nsu.db.aircraft.api.rest.products.PlaneApi;
import com.nsu.db.aircraft.api.rest.products.RocketApi;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.shared.products.AboutProductDetailsRequest;
import com.nsu.db.aircraft.view.shared.sites.WorkTypesRequestFragment;
import com.nsu.db.aircraft.view.shared.staff.BrigadeRequestFragment;
import com.nsu.db.aircraft.view.shared.tests.RangesRequestFragment;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.nsu.db.aircraft.api.model.product.Product.HANG_GLIDER;
import static com.nsu.db.aircraft.api.model.product.Product.HELICOPTER;
import static com.nsu.db.aircraft.api.model.product.Product.PLANE;
import static com.nsu.db.aircraft.api.model.product.Product.ROCKET;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;


public class ProductDetailFragment extends FragmentWithFragmentActivity {
    private boolean isAddFragment = true;
    private Product product;
    private View view;
    private List<Guild> guilds;

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
        setCategorySpinner();
        if (isAddFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }
        return view;
    }

    private void setCategorySpinner() {
        updateSpinner(view, R.id.category_spinner, Product.onlyCategories);
        Spinner categorySpinner = view.findViewById(R.id.category_spinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeFormForCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getSelectedCategory() {
        Spinner categorySpinner = view.findViewById(R.id.category_spinner);
        return (String) categorySpinner.getSelectedItem();
    }

    private void changeFormForCategory() {
        TextView categoryFeature = view.findViewById(R.id.textView30);
        String category = getSelectedCategory();
        if (!category.equals(HANG_GLIDER)) {
            setVisibility(view, R.id.textView30, VISIBLE);
            setVisibility(view, R.id.editText, VISIBLE);
        }
        switch (category) {
            case PLANE:
                categoryFeature.setText(R.string.engine_amount);
                break;
            case ROCKET:
                categoryFeature.setText(R.string.charge_power);
                break;
            case HELICOPTER:
                categoryFeature.setText(R.string.weight);
                break;
            case HANG_GLIDER:
                setVisibility(view, R.id.textView30, INVISIBLE);
                setVisibility(view, R.id.editText, INVISIBLE);
                break;
        }
    }

    private void setAddFragment() {
        setVisibilityForAdd();
        setAddButton();
        sendGuildsRequest();
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            hideKeyboard();
            if (!getSelectedCategory().equals(HANG_GLIDER)
                    && isProductFeatureWrong()) {
                showText(R.string.product_parameter_rule);
                return;
            }
            sendAddProductRequest();
        });
    }

    private boolean isProductFeatureWrong() {
        EditText featureText = view.findViewById(R.id.editText);
        if (featureText.getText().toString().isEmpty()) {
            return false;
        }
        return parseInt(featureText.getText().toString()) < 0;
    }

    private Integer getFeatureValue() {
        EditText featureText = view.findViewById(R.id.editText);
        String feature = featureText.getText().toString();
        if (feature.isEmpty()) {
            return null;
        }
        return parseInt(feature);
    }

    private void sendUpdateProductRequest() {
        product.setType(getEnteredName(R.id.type_edit_text));
        product.setGuild(getSelectedGuild());
        switch (getSelectedCategory()) {
            case PLANE:
                sendPlaneRequest();
                break;
            case ROCKET:
                sendRocketRequest();
                break;
            case HELICOPTER:
                sendHelicopterRequest();
                break;
            case HANG_GLIDER:
                sendHangGliderRequest();
                break;
        }
    }

    private void sendAddProductRequest() {
        product = new Product();
        product.setType(getEnteredName(R.id.type_edit_text));
        product.setGuild(getSelectedGuild());
        switch (getSelectedCategory()) {
            case PLANE:
                sendPlaneRequest();
                break;
            case ROCKET:
                sendRocketRequest();
                break;
            case HELICOPTER:
                sendHelicopterRequest();
                break;
            case HANG_GLIDER:
                sendHangGliderRequest();
                break;
        }
    }

    private void sendHelicopterRequest() {
        if (getFeatureValue() != null) {
            product.setWeight(getFeatureValue());
        }
        HelicopterApi helicopterApi = NetworkService.getInstance().getHelicopterApi();
        if (isAddFragment) {
            helicopterApi.add(product)
                    .enqueue(new GeneralResponseCallback());
        } else {
            helicopterApi.update(product)
                    .enqueue(new ProductCallback());
        }
    }

    private void sendHangGliderRequest() {
        HangGliderApi hangGliderApi = NetworkService.getInstance().getHangGliderApi();
        if (isAddFragment) {
            hangGliderApi.add(product)
                    .enqueue(new GeneralResponseCallback());
        } else {
            hangGliderApi.update(product)
                    .enqueue(new ProductCallback());
        }
    }

    private void sendPlaneRequest() {
        if (getFeatureValue() != null) {
            product.setEngineAmount(getFeatureValue());
        }
        PlaneApi planeApi = NetworkService.getInstance().getPlaneApi();
        if (isAddFragment) {
            planeApi.add(product)
                    .enqueue(new GeneralResponseCallback());
        } else {
            planeApi.update(product)
                    .enqueue(new ProductCallback());
        }
    }

    private void sendRocketRequest() {
        if (getFeatureValue() != null) {
            product.setChargePower(getFeatureValue());
        }
        RocketApi rocketApi = NetworkService.getInstance().getRocketApi();
        if (isAddFragment) {
            rocketApi.add(product)
                    .enqueue(new GeneralResponseCallback());
        } else {
            rocketApi.update(product)
                    .enqueue(new ProductCallback());
        }
    }

    private Guild getSelectedGuild() {
        Spinner guildsSpinner = view.findViewById(R.id.guild_spinner);
        return guilds.get(guildsSpinner.getSelectedItemPosition());
    }


    private void sendGuildsRequest() {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getAllGuilds()
                .enqueue(new GuildsCallback());
    }


    private void setDetailFragment() {
        setDetailButtons();
        setDetailVisibility();
        setDetailEnabled(false);
        setProductData();
        setChangeButton();
        setSaveButton();
        setDeleteButton();
    }

    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            hideKeyboard();
            if (!getSelectedCategory().equals(HANG_GLIDER)
                    && isProductFeatureWrong()) {
                showText(R.string.product_parameter_rule);
                return;
            }
            sendUpdateProductRequest();
        });
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> NetworkService.getInstance()
                .getProductJsonApi()
                .deleteByProductId(product.getId())
                .enqueue(new GeneralResponseCallback()));
    }

    private void setChangeButton() {
        Button changeButton = view.findViewById(R.id.button_change);
        changeButton.setOnClickListener(v -> {
            setDetailEnabled(true);
            setVisibility(view, R.id.button_save, VISIBLE);
            sendGuildsRequest();
        });
    }

    private void setProductData() {
        updateSpinner(view, R.id.category_spinner, asList(product.getProductCategory()));
        changeFormForCategory();
        guilds = asList(product.getGuild());
        updateGuildsSpinner();
        TextInputEditText type = view.findViewById(R.id.type_edit_text);
        type.setText(product.getType());
        EditText featureText = view.findViewById(R.id.editText);
        switch (product.getProductCategory()) {
            case PLANE:
                featureText.setText(valueOf(product.getEngineAmount()));
                break;
            case ROCKET:
                featureText.setText(valueOf(product.getChargePower()));
                break;
            case HELICOPTER:
                featureText.setText(valueOf(product.getWeight()));
                break;
        }
    }

    private void setDetailVisibility() {
        setVisibility(view, R.id.button_add, INVISIBLE);
    }

    private void setDetailEnabled(boolean enabled) {
        setEnabled(view.findViewById(R.id.category_spinner), false);
        setEnabled(view.findViewById(R.id.guild_spinner), enabled);
        setEnabled(view.findViewById(R.id.type_edit_text), enabled);
        setEnabled(view.findViewById(R.id.editText), enabled);
    }

    private void setVisibilityForAdd() {
        setVisibility(view, R.id.button_change, INVISIBLE);
        setVisibility(view, R.id.button_delete, INVISIBLE);
        setVisibility(view, R.id.button_works, INVISIBLE);
        setVisibility(view, R.id.button_brigades, INVISIBLE);
        setVisibility(view, R.id.button_labs, INVISIBLE);
        setVisibility(view, R.id.button_equipment, INVISIBLE);
        setVisibility(view, R.id.button_testers, INVISIBLE);
    }

    private void setDetailButtons() {
        setStartFragmentButton(view, R.id.button_works, new WorkTypesRequestFragment(product));
        setStartFragmentButton(view, R.id.button_brigades, new BrigadeRequestFragment(product));
        setStartFragmentButton(view, R.id.button_labs, new RangesRequestFragment(product));
        setStartFragmentButton(view, R.id.button_equipment,
                new AboutProductDetailsRequest(product, true, false));
        setStartFragmentButton(view, R.id.button_testers,
                new AboutProductDetailsRequest(product, false, true));

    }

    private void updateGuildsSpinner() {
        List<String> guildNames = guilds.stream()
                .map(Guild::getGuildName).collect(Collectors.toList());
        updateSpinner(view, R.id.guild_spinner, guildNames);
        if (isAddFragment) {
            return;
        }
        Spinner guildSpinner = view.findViewById(R.id.guild_spinner);
        for (int i = 0; i < guilds.size(); i++) {
            if (guilds.get(i).equals(product.getGuild())) {
                guildSpinner.setSelection(i);
            }
        }
    }

    private class GuildsCallback implements Callback<GeneralResponse<List<Guild>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Guild>>> call,
                               Response<GeneralResponse<List<Guild>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            guilds = response.body().getData();
            updateGuildsSpinner();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Guild>>> call, Throwable t) {
            showError();
        }
    }

    private class GeneralResponseCallback implements Callback<GeneralResponse> {

        @Override
        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            if (isAddFragment) {
                showText(R.string.load_success);
            } else {
                showText(R.string.success_delete);
            }
            startFragment(new ProductsMainFragment());
        }

        @Override
        public void onFailure(Call<GeneralResponse> call, Throwable t) {
            showError();
        }
    }

    private class ProductCallback implements Callback<GeneralResponse<Product>> {

        @Override
        public void onResponse(Call<GeneralResponse<Product>> call,
                               Response<GeneralResponse<Product>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            showText(R.string.update_success);
            startFragment(new ProductsMainFragment());
        }

        @Override
        public void onFailure(Call<GeneralResponse<Product>> call, Throwable t) {
            showError();
        }
    }
}
