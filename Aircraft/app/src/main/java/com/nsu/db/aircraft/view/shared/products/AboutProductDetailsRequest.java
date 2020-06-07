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
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.api.model.tests.Equipment;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.staff.staff.StaffDetailFragment;
import com.nsu.db.aircraft.view.tests.equipment.EquipmentDetailFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Setter
public class AboutProductDetailsRequest extends FragmentWithFragmentActivity {
    private static final int NULL_CALENDAR_MS = 0;
    private Product product;
    private List<Range> ranges;
    private boolean isEquipmentForm = false;
    private boolean isTestersForm = false;
    private GregorianCalendar beginDate, endDate;
    private List<Equipment> equipment;
    private List<Employee> employees;

    private View view;

    public AboutProductDetailsRequest() {
        // Required empty public constructor
    }

    public AboutProductDetailsRequest(Product product,
                                      boolean isEquipmentForm,
                                      boolean isTestersForm) {
        this.product = product;
        this.isTestersForm = isTestersForm;
        this.isEquipmentForm = isEquipmentForm;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater
                .inflate(R.layout.fragment_about_product_details_request,
                        container, false);
        sendRangesRequest();
        setButtons();
        setListView();
        setFragmentText();
        return view;
    }

    private void setListView() {
        ListView listView = view.findViewById(R.id.list_view);
        if (isEquipmentForm) {
            listView.setOnItemClickListener((parent, view, position, id) ->
                    startFragment(new EquipmentDetailFragment(equipment.get(position))));
        }
        if (isTestersForm) {
            listView.setOnItemClickListener((parent, view, position, id) ->
                    startFragment(new StaffDetailFragment(employees.get(position))));
        }
    }

    private void setFragmentText() {
        TextView fragmentText = view.findViewById(R.id.form_text);
        if (isEquipmentForm) {
            fragmentText.setText(R.string.equipment);
        }
        if (isTestersForm) {
            fragmentText.setText(R.string.testers);
        }
    }

    private void setButtons() {
        Button setBeginDateButton = view.findViewById(R.id.button_begin_date);
        setBeginDateButton.setOnClickListener(v -> startDateFragment(true));
        Button setEndDateButton = view.findViewById(R.id.button_end_date);
        setEndDateButton.setOnClickListener(v -> startDateFragment(false));
        Button showButton = view.findViewById(R.id.button_show);
        showButton.setOnClickListener(v -> {
            if (isEquipmentForm) {
                sendEquipmentRequest();
            }
            if (isTestersForm) {
                sendTestersRequest();
            }
        });
    }

    private void updateEquipmentList() {
        ListView equipmentListView = view.findViewById(R.id.list_view);
        List<String> equipmentNames = new ArrayList<>();
        equipment.forEach(equipmentItem -> equipmentNames.add(equipmentItem.getType()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, equipmentNames);
        equipmentListView.setAdapter(namesAdapter);
    }

    private void sendEquipmentRequest() {
        NetworkService.getInstance()
                .getEquipmentApi()
                .getByDateAndProductAndRange(
                        product.getId(),
                        getSelectedRangeId(),
                        getCalendarMs(beginDate),
                        getCalendarMs(endDate))
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

    private void updateEmployeesList() {
        ListView employeeListView = view.findViewById(R.id.list_view);
        List<String> employeeNames = new ArrayList<>();
        employees.forEach(employee -> employeeNames.add(employee.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, employeeNames);
        employeeListView.setAdapter(namesAdapter);
    }

    private void sendTestersRequest() {
        NetworkService.getInstance()
                .getTesterApi()
                .getByDateAndProductAndRange(
                        product.getId(),
                        getSelectedRangeId(),
                        getCalendarMs(beginDate),
                        getCalendarMs(endDate))
                .enqueue(new Callback<GeneralResponse<List<Employee>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                                           Response<GeneralResponse<List<Employee>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        employees = response.body().getData();
                        employees.forEach(employee -> employee
                                .setEmployeeCategory(Employee.TESTER));
                        updateEmployeesList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private int getSelectedRangeId() {
        Spinner spinner = view.findViewById(R.id.labs_spinner);
        return ranges.get(spinner.getSelectedItemPosition()).getId();
    }

    private long getCalendarMs(Calendar calendar) {
        if (calendar == null) {
            return NULL_CALENDAR_MS;
        }
        return calendar.getTimeInMillis();
    }

    private void startDateFragment(boolean isBeginDate) {
        AboutProductDetailsDates datesFragment = new AboutProductDetailsDates();
        datesFragment.setIsBeginDate(isBeginDate);
        datesFragment.setProduct(product);
        datesFragment.setRanges(ranges);
        datesFragment.setBeginDate(beginDate);
        datesFragment.setEndDate(endDate);
        datesFragment.setEquipmentForm(isEquipmentForm);
        datesFragment.setTestersForm(isTestersForm);
        startFragment(datesFragment);
    }

    private void sendRangesRequest() {
        NetworkService.getInstance()
                .getRangeApi()
                .getByProductId(product.getId())
                .enqueue(new Callback<GeneralResponse<List<Range>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Range>>> call,
                                           Response<GeneralResponse<List<Range>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        ranges = response.body().getData();
                        List<String> rangesNames = new ArrayList<>();
                        ranges.forEach(range -> rangesNames.add(range.getName()));
                        updateSpinner(view, R.id.labs_spinner, rangesNames);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Range>>> call, Throwable t) {
                        showError();
                    }
                });
    }
}
