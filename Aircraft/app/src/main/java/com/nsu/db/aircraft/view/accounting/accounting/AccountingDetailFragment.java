package com.nsu.db.aircraft.view.accounting.accounting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Accounting;
import com.nsu.db.aircraft.api.model.accounting.Stage;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Test;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.AdapterView.INVALID_POSITION;
import static java.lang.String.valueOf;

@Setter
public class AccountingDetailFragment extends FragmentWithFragmentActivity {
    private boolean isAddFragment = true;
    private Accounting accounting;
    private View view;
    private List<Product> products = new ArrayList<>();
    private List<Stage> stages = new ArrayList<>();
    private List<Site> sites = new ArrayList<>();
    private List<Test> tests = new ArrayList<>();
    private GregorianCalendar beginDate, endDate;

    public AccountingDetailFragment() {
        // Required empty public constructor
    }

    public AccountingDetailFragment(Accounting accounting) {
        this.accounting = accounting;
        this.isAddFragment = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accounting_detail, container, false);
        setProductsSpinner();
        if (isAddFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }
        setSetDatesButtons();
        setDatesIfPresent();
        return view;
    }

    private void setDetailFragment() {
        setVisibilityForDetail();
        setEnabledForDetail(false);
        setChangeButton();
        setAccountingData();
        setAccountingInfo();
        updateSpinners();
        setSaveButton();
        setDeleteButton();
    }

    private void setAccountingData() {
        beginDate = new GregorianCalendar();
        beginDate.setTimeInMillis(accounting.getBeginTime());
        endDate = new GregorianCalendar();
        endDate.setTimeInMillis(accounting.getEndTime());
        if (accounting.getProduct() != null) {
            products.add(accounting.getProduct());
        }
        if (accounting.getSite() != null) {
            sites.add(accounting.getSite());
        }
        if (accounting.getStage() != null) {
            stages.add(accounting.getStage());
        }
        if (accounting.getTest() != null) {
            tests.add(accounting.getTest());
        }
    }


    private void setAddFragment() {
        setVisibilityForAdd();
        setAddButton();
        if (accounting != null) {
            updateSpinners();
            setAccountingInfo();
            return;
        }
        sendProductsRequest();
        sendStagesRequest();
    }

    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            saveAccountingSelectedInfo();
            sendUpdateRequest();
        });
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkService.getInstance()
                        .getAccountingApi()
                        .deleteById(accounting.getId())
                        .enqueue(new GeneralResponseCallback());
            }
        });
    }

    private void sendUpdateRequest() {
        NetworkService.getInstance()
                .getAccountingApi()
                .update(accounting)
                .enqueue(new Callback<GeneralResponse<Accounting>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<Accounting>> call,
                                           Response<GeneralResponse<Accounting>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.update_success);
                        startFragment(new ProductAccountingMainFragment());
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<Accounting>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void setVisibilityForDetail() {
        setVisibility(view, R.id.button_add, INVISIBLE);
    }

    private void setEnabledForDetail(boolean enabled) {
        setEnabled(view.findViewById(R.id.product_spinner), enabled);
        setEnabled(view.findViewById(R.id.stages_spinner), enabled);
        setEnabled(view.findViewById(R.id.sites_spinner), enabled);
        setEnabled(view.findViewById(R.id.tests_spinner), enabled);

        setEnabled(view.findViewById(R.id.button_begin), enabled);
        setEnabled(view.findViewById(R.id.button_end), enabled);
    }

    private void setChangeButton() {
        Button changeButton = view.findViewById(R.id.button_change);
        changeButton.setOnClickListener(v -> {
            setEnabledForDetail(true);
            setVisibility(view, R.id.button_save, VISIBLE);
            sendProductsRequest();
            sendStagesRequest();
        });
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            saveAccountingSelectedInfo();
            NetworkService.getInstance()
                    .getAccountingApi()
                    .add(accounting)
                    .enqueue(new GeneralResponseCallback());
        });
    }

    private void updateSpinners() {
        updateProductsSpinner();
        updateSitesSpinner();
        updateStagesSpinner();
        updateTestsSpinner();
    }

    private void setAccountingInfo() {
        if (accounting == null) {
            return;
        }
        setProduct();
        setStage();
        setSite();
        setTest();
    }

    private void setProduct() {
        Spinner productsSpinner = view.findViewById(R.id.product_spinner);
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).equals(accounting.getProduct())) {
                productsSpinner.setSelection(i);
            }
        }
    }

    private void setSite() {
        Spinner siteSpinner = view.findViewById(R.id.sites_spinner);
        for (int i = 0; i < sites.size(); i++) {
            if (sites.get(i).equals(accounting.getSite())) {
                siteSpinner.setSelection(i);
            }
        }
    }

    private void setTest() {
        Spinner testsSpinner = view.findViewById(R.id.tests_spinner);
        for (int i = 0; i < tests.size(); i++) {
            if (tests.get(i).equals(accounting.getTest())) {
                testsSpinner.setSelection(i);
            }
        }
    }

    private void setStage() {
        Spinner stagesSpinner = view.findViewById(R.id.stages_spinner);
        for (int i = 0; i < stages.size(); i++) {
            if (stages.get(i).equals(accounting.getStage())) {
                stagesSpinner.setSelection(i);
            }
        }
    }

    private void setSetDatesButtons() {
        Button beginDateButton = view.findViewById(R.id.button_begin);
        beginDateButton.setOnClickListener(v -> {
            createAndStartSetDateFragment(true);
        });
        Button endDateButton = view.findViewById(R.id.button_end);
        endDateButton.setOnClickListener(v -> {
            createAndStartSetDateFragment(false);
        });
    }

    private void saveAccountingSelectedInfo() {
        accounting = new Accounting();
        accounting.setProduct(getSelectedProduct());
        accounting.setSite(getSelectedSite());
        accounting.setStage(getSelectedStage());
        accounting.setTest(getSelectedTest());
        accounting.setBeginTime(getTimeMillis(beginDate));
        accounting.setEndTime(getTimeMillis(endDate));
    }

    private long getTimeMillis(Calendar calendar) {
        if (calendar == null) {
            return 0;
        }
        return calendar.getTimeInMillis();
    }

    private Test getSelectedTest() {
        Spinner testsSpinner = view.findViewById(R.id.tests_spinner);
        int position = testsSpinner.getSelectedItemPosition();
        if (position == INVALID_POSITION) {
            return null;
        }
        return tests.get(position);
    }

    private Stage getSelectedStage() {
        Spinner stageSpinner = view.findViewById(R.id.stages_spinner);
        int position = stageSpinner.getSelectedItemPosition();
        if (position == INVALID_POSITION) {
            return null;
        }
        return stages.get(position);
    }

    private Site getSelectedSite() {
        Spinner sitesSpinner = view.findViewById(R.id.sites_spinner);
        int position = sitesSpinner.getSelectedItemPosition();
        if (position == INVALID_POSITION) {
            return null;
        }
        return sites.get(position);
    }

    private void createAndStartSetDateFragment(boolean isBeginDate) {
        saveAccountingSelectedInfo();
        AccountingSetDateFragment setDateFragment = new AccountingSetDateFragment();
        setDateFragment.setAccounting(accounting);
        setDateFragment.setAddFragment(isAddFragment);
        setDateFragment.setBeginDate(beginDate);
        setDateFragment.setEndDate(endDate);
        setDateFragment.setProducts(products);
        setDateFragment.setSites(sites);
        setDateFragment.setStages(stages);
        setDateFragment.setTests(tests);
        setDateFragment.setIsBeginDate(isBeginDate);
        startFragment(setDateFragment);
    }

    private void setDatesIfPresent() {
        if (beginDate != null) {
            String date = String.join(".", valueOf(beginDate.get(Calendar.DAY_OF_MONTH)),
                    valueOf(beginDate.get(Calendar.MONTH) + 1),
                    valueOf(beginDate.get(Calendar.YEAR)));
            TextView beginDate = view.findViewById(R.id.begin_date);
            beginDate.setText(date);
        }
        if (endDate != null) {
            String date = String.join(".", valueOf(endDate.get(Calendar.DAY_OF_MONTH)),
                    valueOf(endDate.get(Calendar.MONTH) + 1),
                    valueOf(endDate.get(Calendar.YEAR)));
            TextView beginDate = view.findViewById(R.id.end_date);
            beginDate.setText(date);
        }
    }

    private void sendTestsRequest() {
        NetworkService.getInstance()
                .getTestApi()
                .getByGuild(getSelectedProduct().getGuild())
                .enqueue(new TestsCallback());
    }

    private Product getSelectedProduct() {
        Spinner productsSpinner = view.findViewById(R.id.product_spinner);
        return products.get(productsSpinner.getSelectedItemPosition());
    }

    private void setProductsSpinner() {
        Spinner productSpinner = view.findViewById(R.id.product_spinner);
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendSitesRequest(products.get(position).getGuild());
                sendTestsRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendSitesRequest(Guild guild) {
        NetworkService.getInstance()
                .getSiteApi()
                .getByGuild(guild)
                .enqueue(new SitesCallBack());
    }

    private void sendStagesRequest() {
        NetworkService.getInstance()
                .getStageApi()
                .getAll()
                .enqueue(new StagesCallBack());
    }

    private void sendProductsRequest() {
        NetworkService.getInstance()
                .getRocketApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.ROCKET));
        NetworkService.getInstance()
                .getHangGliderApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.HANG_GLIDER));
        NetworkService.getInstance()
                .getHelicopterApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.HELICOPTER));
        NetworkService.getInstance()
                .getPlaneApi()
                .getAll()
                .enqueue(new ProductsCallBack(Product.PLANE));
    }

    private void setVisibilityForAdd() {
        setVisibility(view, R.id.button_change, INVISIBLE);
        setVisibility(view, R.id.button_delete, INVISIBLE);
        setVisibility(view, R.id.button_save, INVISIBLE);
    }

    private void updateProductsSpinner() {
        List<String> names = products.stream()
                .map(product -> product.toString())
                .collect(Collectors.toList());
        updateSpinner(view, R.id.product_spinner, names);
        if (accounting != null) {
            setProduct();
        }
    }

    private void updateSitesSpinner() {
        List<String> siteNames = sites.stream()
                .map(site -> site.toString()).collect(Collectors.toList());
        updateSpinner(view, R.id.sites_spinner, siteNames);
        if (accounting != null) {
            setSite();
        }
    }

    private void updateStagesSpinner() {
        List<String> stagesNames = stages.stream()
                .map(stage -> stage.getStageName()).collect(Collectors.toList());
        updateSpinner(view, R.id.stages_spinner, stagesNames);
        if (accounting != null) {
            setStage();
        }
    }

    private void updateTestsSpinner() {
        List<String> testNames = tests.stream()
                .map(Test::toString).collect(Collectors.toList());
        updateSpinner(view, R.id.tests_spinner, testNames);
        if (accounting != null) {
            setTest();
        }
    }

    @AllArgsConstructor
    private class ProductsCallBack implements Callback<GeneralResponse<List<Product>>> {
        private String productCategory;

        @Override
        public void onResponse(Call<GeneralResponse<List<Product>>> call,
                               Response<GeneralResponse<List<Product>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Product> responseProducts = response.body().getData();
            responseProducts.forEach(rocket -> rocket.setProductCategory(productCategory));
            products.addAll(responseProducts);
            updateProductsSpinner();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Product>>> call, Throwable t) {
            showError();
        }
    }

    private class SitesCallBack implements Callback<GeneralResponse<List<Site>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Site>>> call, Response<GeneralResponse<List<Site>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            sites = response.body().getData();
            updateSitesSpinner();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Site>>> call, Throwable t) {
            showError();
        }
    }

    private class StagesCallBack implements Callback<GeneralResponse<List<Stage>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Stage>>> call,
                               Response<GeneralResponse<List<Stage>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            stages = response.body().getData();
            updateStagesSpinner();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Stage>>> call, Throwable t) {
            showError();
        }
    }

    private class TestsCallback implements Callback<GeneralResponse<List<Test>>> {
        @Override
        public void onResponse(Call<GeneralResponse<List<Test>>> call,
                               Response<GeneralResponse<List<Test>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            tests = response.body().getData();
            updateTestsSpinner();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Test>>> call, Throwable t) {
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
            startFragment(new ProductAccountingMainFragment());
        }

        @Override
        public void onFailure(Call<GeneralResponse> call, Throwable t) {
            showError();
        }
    }
}
