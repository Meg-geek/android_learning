package com.nsu.db.aircraft.view.tests.test;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.api.model.tests.Equipment;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.api.model.tests.Test;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.nsu.db.aircraft.api.model.product.Product.HANG_GLIDER;
import static com.nsu.db.aircraft.api.model.product.Product.HELICOPTER;
import static com.nsu.db.aircraft.api.model.product.Product.PLANE;
import static com.nsu.db.aircraft.api.model.product.Product.ROCKET;
import static java.util.Arrays.asList;


public class TestDetailFragment extends FragmentWithFragmentActivity {
    private Test test = new Test();
    private View view;
    private List<Product> products = new ArrayList<>();
    private Guild guild;
    private List<Range> ranges;
    private List<Employee> testers;
    private List<Equipment> equipment;
    private List<Equipment> selectedEquipment;
    private boolean isAddFragment = true;

    public TestDetailFragment() {
        // Required empty public constructor
    }

    public TestDetailFragment(Test test) {
        this.test = test;
        this.isAddFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test_detail, container, false);
        if (isAddFragment) {
            setAddFragment();
        } else {
            setDetailFragment();
        }
        return view;
    }

    private void setDetailFragment() {
        products = asList(test.getProduct());
        updateProductsSpinner();
        guild = test.getGuild();
        updateGuildsSpinner();
        ranges = asList(test.getRange());
        updateRangesSpinner();
        testers = asList(test.getTester());
        updateTestersSpinner();
        equipment = test.getEquipment();
        updateEquipmentList();
        setEnabled(view.findViewById(R.id.products_spinner), false);
        setEnabled(view.findViewById(R.id.guilds_spinner), false);
        setEnabled(view.findViewById(R.id.ranges_spinner), false);
        setEnabled(view.findViewById(R.id.testers_spinner), false);
        setVisibility(view, R.id.button_add, INVISIBLE);
        setVisibility(view, R.id.button_delete, VISIBLE);
        setDeleteButton();
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> {
            NetworkService.getInstance()
                    .getTestApi()
                    .deleteById(test.getId())
                    .enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            if (!response.isSuccessful()) {
                                showError();
                                return;
                            }
                            showText(R.string.success_delete);
                            startFragment(new TestMainFragment());
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            showError();
                        }
                    });
        });
    }

    private void setAddFragment() {
        sendProductsRequest();
        setProductsSpinner();
        setRangesSpinner();
        setTestersSpinner();
        setAddButton();
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            NetworkService.getInstance()
                    .getTestApi()
                    .add(test)
                    .enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            showText(R.string.load_success);
                            startFragment(new TestMainFragment());
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            showError();
                        }
                    });
        });
    }

    private void setRangesSpinner() {
        Spinner rangesSpinner = view.findViewById(R.id.ranges_spinner);
        rangesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                test.setRange(ranges.get(position));
                sendTestersRequest();
                sendEquipmentRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTestersSpinner() {
        Spinner testersSpinner = view.findViewById(R.id.testers_spinner);
        testersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                test.setTester(testers.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setProductsSpinner() {
        Spinner productsSpinner = view.findViewById(R.id.products_spinner);
        productsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                test.setProduct(products.get(position));
                guild = products.get(position).getGuild();
                test.setGuild(guild);
                updateGuildsSpinner();
                sendRangesRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendTestersRequest() {
        NetworkService.getInstance()
                .getTesterApi()
                .getByRange(test.getRange())
                .enqueue(new Callback<GeneralResponse<List<Employee>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                                           Response<GeneralResponse<List<Employee>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        testers = response.body().getData();
                        testers.forEach(tester -> tester.setEmployeeCategory(Employee.TESTER));
                        updateTestersSpinner();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendEquipmentRequest() {
        NetworkService.getInstance()
                .getEquipmentApi()
                .getByRange(test.getRange())
                .enqueue(new Callback<GeneralResponse<List<Equipment>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Equipment>>> call,
                                           Response<GeneralResponse<List<Equipment>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        equipment = response.body().getData();
                        updateEquipmentList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Equipment>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateEquipmentList() {
        ListView equipmentList = view.findViewById(R.id.list_view);
        equipmentList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    SparseBooleanArray sparseBooleanArray = equipmentList
                            .getCheckedItemPositions();
                    selectedEquipment = new ArrayList<>();
                    for (int i = 0; i < sparseBooleanArray.size(); i++) {
                        int key = sparseBooleanArray.keyAt(i);
                        if (sparseBooleanArray.get(key)) {
                            selectedEquipment.add(equipment.get(key));
                        }
                    }
                    test.setEquipment(selectedEquipment);
                }
        );
    }

    private void sendRangesRequest() {
        NetworkService.getInstance()
                .getRangeApi()
                .getByGuildId(guild.getId())
                .enqueue(new Callback<GeneralResponse<List<Range>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Range>>> call,
                                           Response<GeneralResponse<List<Range>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        ranges = response.body().getData();
                        updateRangesSpinner();
                        sendEquipmentRequest();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Range>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendProductsRequest() {
        NetworkService networkService = NetworkService.getInstance();
        networkService.getRocketApi().getAll().enqueue(new ProductsCallback(ROCKET));
        networkService.getHangGliderApi().getAll().enqueue(new ProductsCallback(HANG_GLIDER));
        networkService.getHelicopterApi().getAll().enqueue(new ProductsCallback(HELICOPTER));
        networkService.getPlaneApi().getAll().enqueue(new ProductsCallback(PLANE));
    }


    private void updateProductsSpinner() {
        List<String> productNames = products.stream()
                .map(Product::toString).collect(Collectors.toList());
        updateSpinner(view, R.id.products_spinner, productNames);
    }

    private void updateGuildsSpinner() {
        List<String> guildNames = asList(guild.getGuildName());
        updateSpinner(view, R.id.guilds_spinner, guildNames);
    }

    private void updateRangesSpinner() {
        List<String> rangesNames = ranges.stream()
                .map(range -> range.getName())
                .collect(Collectors.toList());
        updateSpinner(view, R.id.ranges_spinner, rangesNames);
    }

    private void updateTestersSpinner() {
        List<String> testersNames = testers.stream()
                .map(tester -> tester.toString())
                .collect(Collectors.toList());
        updateSpinner(view, R.id.testers_spinner, testersNames);
    }

    @AllArgsConstructor
    private class ProductsCallback implements Callback<GeneralResponse<List<Product>>> {
        private String category;

        @Override
        public void onResponse(Call<GeneralResponse<List<Product>>> call,
                               Response<GeneralResponse<List<Product>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Product> productsResponse = response.body().getData();
            productsResponse.forEach(product -> product.setProductCategory(category));
            products.addAll(productsResponse);
            updateProductsSpinner();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Product>>> call, Throwable t) {

        }
    }
}
