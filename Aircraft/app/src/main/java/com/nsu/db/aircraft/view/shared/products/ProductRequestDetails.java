package com.nsu.db.aircraft.view.shared.products;

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
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.api.model.tests.Equipment;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.api.rest.products.HangGliderApi;
import com.nsu.db.aircraft.api.rest.products.HelicopterApi;
import com.nsu.db.aircraft.api.rest.products.PlaneApi;
import com.nsu.db.aircraft.api.rest.products.RocketApi;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.products.ProductDetailFragment;
import com.nsu.db.aircraft.view.staff.staff.StaffDetailFragment;
import com.nsu.db.aircraft.view.tests.equipment.EquipmentDetailFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static com.nsu.db.aircraft.api.model.product.Product.ALL;
import static com.nsu.db.aircraft.api.model.product.Product.HANG_GLIDER;
import static com.nsu.db.aircraft.api.model.product.Product.HELICOPTER;
import static com.nsu.db.aircraft.api.model.product.Product.PLANE;
import static com.nsu.db.aircraft.api.model.product.Product.ROCKET;
import static java.lang.String.valueOf;

@Setter
public class ProductRequestDetails extends FragmentWithFragmentActivity {
    private static final int NULL_CALENDAR_MS = 0;
    private Company company;
    private Site site;
    private Guild guild;
    private Range range;
    private View view;
    private boolean isEquipmentForm = false;
    private boolean isProductsForm = false;
    private boolean isTestersForm = false;
    private List<Equipment> equipment;
    private List<Employee> testers;
    private List<Product> products;
    private List<String> types;
    private GregorianCalendar beginDate, endDate;

    public ProductRequestDetails(Company company) {
        this.company = company;
    }

    public ProductRequestDetails(Guild guild) {
        this.guild = guild;
    }

    public ProductRequestDetails(Site site) {
        this.site = site;
    }

    public ProductRequestDetails(Range range) {
        this.range = range;
    }

    public ProductRequestDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_request_details, container, false);
        updateSpinner(view, R.id.spinner, Product.categoriesList);
        setListView();
        setDatesIfPresent();
        setVisibility();
        setButtons();
        return view;
    }

    private void setListView() {
        if (Stream.of(company, guild, site).anyMatch(Objects::nonNull)) {
            isProductsForm = true;
        }
        ListView listView = view.findViewById(R.id.list_view);
        TextView textView = view.findViewById(R.id.textView31);
        if (isProductsForm) {
            listView.setOnItemClickListener((parent, v, position, id) ->
                    startFragment(new ProductDetailFragment(products.get(position))));
            textView.setText(R.string.products);
        }
        if (isTestersForm) {
            listView.setOnItemClickListener((parent, v, position, id) ->
                    startFragment(new StaffDetailFragment(testers.get(position))));
            textView.setText(R.string.testers);
        }
        if (isEquipmentForm) {
            listView.setOnItemClickListener((parent, v, position, id) ->
                    startFragment(new EquipmentDetailFragment(equipment.get(position))));
            textView.setText(R.string.equipment);
        }
    }

    private void setVisibility() {
        if (range != null) {
            setVisibility(view, R.id.button_show_types, INVISIBLE);
            setVisibility(view, R.id.button_build_now, INVISIBLE);
        }
        if (site != null) {
            setVisibility(view, R.id.button_show_types, INVISIBLE);
        }
    }

    private void setButtons() {
        setDateButtons();
        setButtonShowTypes();
        setButtonBuildNow();
        setShowByDatesButton();
    }

    private void setShowByDatesButton() {
        Button showByDatesButton = view.findViewById(R.id.button_show_by_dates);
        showByDatesButton.setOnClickListener(v ->
                sendShowByDatesRequest(getSelectedCategory(), true)
        );
    }

    private void setButtonBuildNow() {
        Button buttonBuildByNow = view.findViewById(R.id.button_build_now);
        buttonBuildByNow.setOnClickListener(v -> {
            sendBuildByNowRequest(getSelectedCategory(), true);
        });
    }

    private void sendShowByDatesRequest(String category, boolean isSingleRequest) {
        switch (category) {
            case PLANE:
                sendPlaneShowByDatesRequest(isSingleRequest);
                break;
            case HELICOPTER:
                sendHelicopterShowByDatesRequest(isSingleRequest);
                break;
            case HANG_GLIDER:
                sendHangGliderShowByDatesRequest(isSingleRequest);
                break;
            case ROCKET:
                sendRocketShowByDatesRequest(isSingleRequest);
                break;
            case ALL:
                sendAllShowByDatesRequest();
                break;
        }
    }

    private void sendBuildByNowRequest(String category, boolean isSingleRequest) {
        switch (category) {
            case PLANE:
                sendPlaneNowBuildRequest(isSingleRequest);
                break;
            case HELICOPTER:
                sendHelicopterNowBuildRequest(isSingleRequest);
                break;
            case HANG_GLIDER:
                sendHangGliderNowBuildRequest(isSingleRequest);
                break;
            case ROCKET:
                sendRocketNowBuildRequest(isSingleRequest);
                break;
            case ALL:
                sendAllNowBuildRequest();
                break;
        }
    }

    private long getTimeInMillis(Calendar calendar) {
        if (calendar == null) {
            return NULL_CALENDAR_MS;
        }
        return calendar.getTimeInMillis();
    }

    private void sendPlaneShowByDatesRequest(boolean isSingleRequest) {
        PlaneApi planeApi = NetworkService.getInstance().getPlaneApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = planeApi
                    .getByDateIntervalAndCompany(company.getId(), beginDateMs, endDateMs);
        }
        if (guild != null) {
            call = planeApi
                    .getByDateIntervalAndGuild(guild.getId(), beginDateMs, endDateMs);
        }
        if (site != null) {
            call = planeApi
                    .getByDateIntervalAndSite(site.getId(), beginDateMs, endDateMs);
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(PLANE, isSingleRequest));
        }
        if (range != null) {
            sendPlaneRangeShowByDatesRequest(isSingleRequest);
        }

    }

    private void sendPlaneRangeShowByDatesRequest(boolean isSingleRequest) {
        PlaneApi planeApi = NetworkService.getInstance().getPlaneApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        if (isProductsForm) {
            planeApi.getByDateIntervalAndRange(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new ProductsCallback(PLANE, isSingleRequest));
        }
        if (isEquipmentForm) {
            NetworkService.getInstance()
                    .getEquipmentApi()
                    .getByDateAndRangeAndPlanes(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new EquipmentCallback(isSingleRequest));
        }
        if (isTestersForm) {
            NetworkService.getInstance()
                    .getTesterApi()
                    .getByDateAndRangeAndPlanes(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new TesterCallback(isSingleRequest));
        }
    }

    private void sendHelicopterShowByDatesRequest(boolean isSingleRequest) {
        HelicopterApi helicopterApi = NetworkService.getInstance().getHelicopterApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = helicopterApi
                    .getByDateIntervalAndCompany(company.getId(), beginDateMs, endDateMs);
        }
        if (guild != null) {
            call = helicopterApi
                    .getByDateIntervalAndGuild(guild.getId(), beginDateMs, endDateMs);
        }
        if (site != null) {
            call = helicopterApi
                    .getByDateIntervalAndSite(site.getId(), beginDateMs, endDateMs);
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(HELICOPTER, isSingleRequest));
        }
        if (range != null) {
            sendHelicopterRangeShowByDatesRequest(isSingleRequest);
        }
    }

    private void sendHelicopterRangeShowByDatesRequest(boolean isSingleRequest) {
        HelicopterApi helicopterApi = NetworkService.getInstance().getHelicopterApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        if (isProductsForm) {
            helicopterApi.getByDateIntervalAndRange(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new ProductsCallback(HELICOPTER, isSingleRequest));
        }
        if (isEquipmentForm) {
            NetworkService.getInstance()
                    .getEquipmentApi()
                    .getByDateAndRangeAndHelicopters(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new EquipmentCallback(isSingleRequest));
        }
        if (isTestersForm) {
            NetworkService.getInstance()
                    .getTesterApi()
                    .getByDateAndRangeAndHelicopters(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new TesterCallback(isSingleRequest));
        }
    }

    private void sendHangGliderShowByDatesRequest(boolean isSingleRequest) {
        HangGliderApi hangGliderApi = NetworkService.getInstance().getHangGliderApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = hangGliderApi
                    .getByDateIntervalAndCompany(company.getId(), beginDateMs, endDateMs);
        }
        if (guild != null) {
            call = hangGliderApi
                    .getByDateIntervalAndGuild(guild.getId(), beginDateMs, endDateMs);
        }
        if (site != null) {
            call = hangGliderApi
                    .getByDateIntervalAndSite(site.getId(), beginDateMs, endDateMs);
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(HANG_GLIDER, isSingleRequest));
        }
        if (range != null) {
            sendHangGliderRangeShowByDatesRequest(isSingleRequest);
        }
    }

    private void sendHangGliderRangeShowByDatesRequest(boolean isSingleRequest) {
        HangGliderApi hangGliderApi = NetworkService.getInstance().getHangGliderApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        if (isProductsForm) {
            hangGliderApi.getByDateIntervalAndRange(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new ProductsCallback(HANG_GLIDER, isSingleRequest));
        }
        if (isEquipmentForm) {
            NetworkService.getInstance()
                    .getEquipmentApi()
                    .getByDateAndRangeAndHangGliders(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new EquipmentCallback(isSingleRequest));
        }
        if (isTestersForm) {
            NetworkService.getInstance()
                    .getTesterApi()
                    .getByDateAndRangeAndHangGliders(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new TesterCallback(isSingleRequest));
        }
    }

    private void sendRocketShowByDatesRequest(boolean isSingleRequest) {
        RocketApi rocketApi = NetworkService.getInstance().getRocketApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = rocketApi
                    .getByDateIntervalAndCompany(company.getId(), beginDateMs, endDateMs);
        }
        if (guild != null) {
            call = rocketApi
                    .getByDateIntervalAndGuild(guild.getId(), beginDateMs, endDateMs);
        }
        if (site != null) {
            call = rocketApi
                    .getByDateIntervalAndSite(site.getId(), beginDateMs, endDateMs);
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(ROCKET, isSingleRequest));
        }
        if (range != null) {
            sendRocketRangeShowByDatesRequest(isSingleRequest);
        }
    }

    private void sendRocketRangeShowByDatesRequest(boolean isSingleRequest) {
        RocketApi rocketApi = NetworkService.getInstance().getRocketApi();
        long beginDateMs = getTimeInMillis(beginDate), endDateMs = getTimeInMillis(endDate);
        if (isProductsForm) {
            rocketApi.getByDateIntervalAndRange(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new ProductsCallback(ROCKET, isSingleRequest));
        }
        if (isEquipmentForm) {
            NetworkService.getInstance()
                    .getEquipmentApi()
                    .getByDateAndRangeAndRockets(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new EquipmentCallback(isSingleRequest));
        }
        if (isTestersForm) {
            NetworkService.getInstance()
                    .getTesterApi()
                    .getByDateAndRangeAndRockets(range.getId(), beginDateMs, endDateMs)
                    .enqueue(new TesterCallback(isSingleRequest));
        }
    }

    private void sendAllShowByDatesRequest() {
        products = new ArrayList<>();
        testers = new ArrayList<>();
        equipment = new ArrayList<>();
        for (String category : Product.categoriesList) {
            if (category.equals(ALL)) {
                continue;
            }
            sendShowByDatesRequest(category, false);
        }
    }

    private void sendPlaneNowBuildRequest(boolean isSingleRequest) {
        PlaneApi planeApi = NetworkService.getInstance().getPlaneApi();
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = planeApi.getNowBuildingByCompany(company.getId());
        }
        if (guild != null) {
            call = planeApi.getNowBuildingByGuild(guild.getId());
        }
        if (site != null) {
            call = planeApi.getNowBuildingBySite(site.getId());
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(PLANE, isSingleRequest));
        }
    }

    private void sendHelicopterNowBuildRequest(boolean isSingleRequest) {
        HelicopterApi helicopterApi = NetworkService.getInstance().getHelicopterApi();
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = helicopterApi.getNowBuildingByCompany(company.getId());
        }
        if (guild != null) {
            call = helicopterApi.getNowBuildingByGuild(guild.getId());
        }
        if (site != null) {
            call = helicopterApi.getNowBuildingBySite(site.getId());
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(HELICOPTER, isSingleRequest));
        }
    }

    private void sendHangGliderNowBuildRequest(boolean isSingleRequest) {
        HangGliderApi hangGliderApi = NetworkService.getInstance().getHangGliderApi();
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = hangGliderApi.getNowBuildingByCompany(company.getId());
        }
        if (guild != null) {
            call = hangGliderApi.getNowBuildingByGuild(guild.getId());
        }
        if (site != null) {
            call = hangGliderApi.getNowBuildingBySite(site.getId());
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(HANG_GLIDER, isSingleRequest));
        }
    }

    private void sendRocketNowBuildRequest(boolean isSingleRequest) {
        RocketApi rocketApi = NetworkService.getInstance().getRocketApi();
        Call<GeneralResponse<List<Product>>> call = null;
        if (company != null) {
            call = rocketApi.getNowBuildingByCompany(company.getId());
        }
        if (guild != null) {
            call = rocketApi.getNowBuildingByGuild(guild.getId());
        }
        if (site != null) {
            call = rocketApi.getNowBuildingBySite(site.getId());
        }
        if (call != null) {
            call.enqueue(new ProductsCallback(ROCKET, isSingleRequest));
        }
    }

    private void sendAllNowBuildRequest() {
        products = new ArrayList<>();
        for (String category : Product.categoriesList) {
            if (category.equals(ALL)) {
                continue;
            }
            sendBuildByNowRequest(category, false);
        }
    }

    private void setButtonShowTypes() {
        Button showTypesButton = view.findViewById(R.id.button_show_types);
        showTypesButton.setOnClickListener(v ->
                sendTypesRequest(getSelectedCategory(), true));
    }

    private String getSelectedCategory() {
        Spinner spinner = view.findViewById(R.id.spinner);
        return (String) spinner.getSelectedItem();
    }

    private void sendTypesRequest(String category, boolean isSingleRequest) {
        switch (category) {
            case PLANE:
                sendPlaneTypesRequest(isSingleRequest);
                break;
            case HELICOPTER:
                sendHelicopterTypesRequest(isSingleRequest);
                break;
            case HANG_GLIDER:
                sendHangGliderTypesRequest(isSingleRequest);
                break;
            case ROCKET:
                sendRocketTypesRequest(isSingleRequest);
                break;
            case ALL:
                sendAllTypesRequest();
                break;
        }
    }

    private void sendAllTypesRequest() {
        types = new ArrayList<>();
        for (String category : Product.categoriesList) {
            if (category.equals(ALL)) {
                continue;
            }
            sendTypesRequest(category, false);
        }
    }

    private void sendPlaneTypesRequest(boolean isSingleRequest) {
        PlaneApi planeApi = NetworkService.getInstance().getPlaneApi();
        if (company != null) {
            planeApi.getTypesByCompanyId(company.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
        if (guild != null) {
            planeApi.getTypesByGuildId(guild.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
    }

    private void sendHelicopterTypesRequest(boolean isSingleRequest) {
        HelicopterApi helicopterApi = NetworkService.getInstance().getHelicopterApi();
        if (company != null) {
            helicopterApi.getTypesByCompanyId(company.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
        if (guild != null) {
            helicopterApi.getTypesByGuildId(guild.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
    }

    private void sendHangGliderTypesRequest(boolean isSingleRequest) {
        HangGliderApi hangGliderApi = NetworkService.getInstance().getHangGliderApi();
        if (company != null) {
            hangGliderApi.getTypesByCompanyId(company.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
        if (guild != null) {
            hangGliderApi.getTypesByGuildId(guild.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
    }

    private void sendRocketTypesRequest(boolean isSingleRequest) {
        RocketApi rocketApi = NetworkService.getInstance().getRocketApi();
        if (company != null) {
            rocketApi.getTypesByCompanyId(company.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
        if (guild != null) {
            rocketApi.getTypesByGuildId(guild.getId())
                    .enqueue(new TypesCallback(isSingleRequest));
        }
    }

    private void setDateButtons() {
        Button beginDateButton = view.findViewById(R.id.button_set_begin);
        beginDateButton.setOnClickListener(v -> {
            createAndStartSetDateFragment(true);
        });
        Button endDateButton = view.findViewById(R.id.button_set_end);
        endDateButton.setOnClickListener(v -> {
            createAndStartSetDateFragment(false);
        });
    }

    private void createAndStartSetDateFragment(boolean isBeginDate) {
        SetDateFragment setDateFragment = new SetDateFragment();
        setDateFragment.setIsBeginDate(isBeginDate);
        setDateFragment.setBeginDate(beginDate);
        setDateFragment.setCompany(company);
        setDateFragment.setEndDate(endDate);
        setDateFragment.setGuild(guild);
        setDateFragment.setSite(site);
        setDateFragment.setRange(range);
        setDateFragment.setEquipmentForm(isEquipmentForm);
        setDateFragment.setProductsForm(isProductsForm);
        setDateFragment.setTestersForm(isTestersForm);
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

    private void updateTypesList() {
        ListView typesListView = view.findViewById(R.id.list_view);
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, types);
        typesListView.setAdapter(namesAdapter);
    }

    private void updateProductsList() {
        ListView productsListView = view.findViewById(R.id.list_view);
        List<String> productNames = new ArrayList<>();
        products.forEach(product -> productNames.add(product.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, productNames);
        productsListView.setAdapter(namesAdapter);
    }

    private void updateEquipmentList() {
        ListView equipmentListView = view.findViewById(R.id.list_view);
        List<String> equipmentTypes = new ArrayList<>();
        equipment.forEach(equipmentItem -> equipmentTypes.add(equipmentItem.getType()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, equipmentTypes);
        equipmentListView.setAdapter(namesAdapter);
    }

    private void updateTestersList() {
        ListView testersListView = view.findViewById(R.id.list_view);
        List<String> testerNames = new ArrayList<>();
        testers.forEach(tester -> testerNames.add(tester.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, testerNames);
        testersListView.setAdapter(namesAdapter);
    }

    @AllArgsConstructor
    private class TypesCallback implements Callback<GeneralResponse<List<String>>> {
        private boolean isSingleRequest;

        @Override
        public void onResponse(Call<GeneralResponse<List<String>>> call,
                               Response<GeneralResponse<List<String>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            if (isSingleRequest) {
                types = response.body().getData();
            } else {
                types.addAll(response.body().getData());
            }
            updateTypesList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<String>>> call, Throwable t) {
            showError();
        }
    }

    @AllArgsConstructor
    private class ProductsCallback implements Callback<GeneralResponse<List<Product>>> {
        private String category;
        private boolean isSingleRequest;

        @Override
        public void onResponse(Call<GeneralResponse<List<Product>>> call,
                               Response<GeneralResponse<List<Product>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Product> responseProducts = response.body().getData();
            responseProducts.forEach(product -> product.setProductCategory(category));
            if (isSingleRequest) {
                products = responseProducts;
            } else {
                products.addAll(responseProducts);
            }
            updateProductsList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Product>>> call, Throwable t) {
            showError();
        }
    }

    @AllArgsConstructor
    private class EquipmentCallback implements Callback<GeneralResponse<List<Equipment>>> {
        private boolean isSingleRequest;

        @Override
        public void onResponse(Call<GeneralResponse<List<Equipment>>> call,
                               Response<GeneralResponse<List<Equipment>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Equipment> responseEquipment = response.body().getData();
            if (isSingleRequest) {
                equipment = responseEquipment;
            } else {
                for (Equipment equipmentItem : responseEquipment) {
                    if (!equipment.contains(equipmentItem)) {
                        equipment.add(equipmentItem);
                    }
                }
            }
            updateEquipmentList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Equipment>>> call, Throwable t) {
            showError();
        }
    }

    @AllArgsConstructor
    private class TesterCallback implements Callback<GeneralResponse<List<Employee>>> {
        private boolean isSingleRequest;

        @Override
        public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                               Response<GeneralResponse<List<Employee>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Employee> responseEmployees = response.body().getData();
            responseEmployees.forEach(employee -> employee.setEmployeeCategory(Employee.TESTER));
            if (isSingleRequest) {
                testers = responseEmployees;
            } else {
                for (Employee employee : responseEmployees) {
                    if (!testers.contains(employee)) {
                        testers.add(employee);
                    }
                }
            }
            updateTestersList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
            showError();
        }
    }
}
