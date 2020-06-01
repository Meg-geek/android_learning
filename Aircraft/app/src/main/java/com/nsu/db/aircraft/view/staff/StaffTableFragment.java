package com.nsu.db.aircraft.view.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.TableFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nsu.db.aircraft.api.model.staff.Employee.ALL;
import static com.nsu.db.aircraft.api.model.staff.Employee.ENGINEER;
import static com.nsu.db.aircraft.api.model.staff.Employee.LOCKSMITH;
import static com.nsu.db.aircraft.api.model.staff.Employee.MASTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.PICKER;
import static com.nsu.db.aircraft.api.model.staff.Employee.TECHNICIAN;
import static com.nsu.db.aircraft.api.model.staff.Employee.TESTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.TURNER;
import static com.nsu.db.aircraft.api.model.staff.Employee.WELDER;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;


public class StaffTableFragment extends TableFragment {
    private View view;
    private List<Employee> employees;

    private List<String> allStaffColumns = asList("id", "name", "surname");
    private List<String> workerColumns = new ArrayList<>(allStaffColumns);
    private List<String> engineeringStaffColumns = new ArrayList<>(allStaffColumns);
    private List<String> testerColumns = new ArrayList<>(allStaffColumns);

    public StaffTableFragment() {
        workerColumns.add("brigade_id");
        engineeringStaffColumns.add("site_id");
        testerColumns.add("range_id");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_table, container, false);
        setSpinner();
        setShowButton();
        return view;
    }

    private void setShowButton() {
        Button showButton = view.findViewById(R.id.button_show_staff);
        showButton.setOnClickListener(v -> sendRequest(getSelectedCategory()));
    }

    private void sendRequest(String category) {
        switch (category) {
            case TESTER:
                sendTestersRequest();
                break;
            case ENGINEER:
                sendEngineerRequest();
                break;
            case LOCKSMITH:
                sendLocksmithRequest();
                break;
            case ALL:
                sendAllRequest();
                break;
            case MASTER:
                sendMasterRequest();
                break;
            case PICKER:
                sendPickerRequest();
                break;
            case TECHNICIAN:
                sendTechnicianRequest();
                break;
            case TURNER:
                sendTurnerRequest();
                break;
            case WELDER:
                sendWelderRequest();
                break;
        }
    }

    private void sendWelderRequest() {
        NetworkService.getInstance()
                .getWelderApi()
                .getAll()
                .enqueue(new WorkerCallback());
    }

    private void sendTurnerRequest() {
        NetworkService.getInstance()
                .getTurnerApi()
                .getAll()
                .enqueue(new WorkerCallback());
    }

    private void sendTechnicianRequest() {
        NetworkService.getInstance()
                .getTechnicianApi()
                .getAll()
                .enqueue(new EngineeringStaffCallback());
    }

    private void sendPickerRequest() {
        NetworkService.getInstance()
                .getPickerApi()
                .getAll()
                .enqueue(new WorkerCallback());
    }

    private void sendMasterRequest() {
        NetworkService.getInstance()
                .getMasterApi()
                .getAll()
                .enqueue(new EngineeringStaffCallback());
    }

    private void sendAllRequest() {
        NetworkService.getInstance()
                .getEmployeeApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Employee>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                                           Response<GeneralResponse<List<Employee>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        employees = response.body().getData();
                        showAllTable();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void sendLocksmithRequest() {
        NetworkService.getInstance()
                .getLocksmithApi()
                .getAll()
                .enqueue(new WorkerCallback());
    }

    private void sendEngineerRequest() {
        NetworkService.getInstance()
                .getEngineerApi()
                .getAll()
                .enqueue(new EngineeringStaffCallback());
    }

    private void sendTestersRequest() {
        NetworkService.getInstance()
                .getTesterApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Employee>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                                           Response<GeneralResponse<List<Employee>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        employees = response.body().getData();
                        setTesterTable();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void showAllTable() {
        TableLayout tableLayout = view.findViewById(R.id.staff_table);
        tableLayout.removeAllViews();
        setColumnNames(allStaffColumns);
        addColumnNames(tableLayout);
        for (Employee employee : employees) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(getTextViewForTable(valueOf(employee.getId())));
            tableRow.addView(getTextViewForTable(employee.getName()));
            tableRow.addView(getTextViewForTable(employee.getSurname()));
            tableLayout.addView(tableRow);
        }
    }

    private void showEngineeringStaffTable() {
        TableLayout tableLayout = view.findViewById(R.id.staff_table);
        tableLayout.removeAllViews();
        setColumnNames(engineeringStaffColumns);
        addColumnNames(tableLayout);
        for (Employee employee : employees) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(getTextViewForTable(valueOf(employee.getId())));
            tableRow.addView(getTextViewForTable(employee.getName()));
            tableRow.addView(getTextViewForTable(employee.getSurname()));
            tableRow.addView(getTextViewForTable(getSiteId(employee.getSite())));
            tableLayout.addView(tableRow);
        }
    }

    private void setWorkerTable() {
        TableLayout tableLayout = view.findViewById(R.id.staff_table);
        tableLayout.removeAllViews();
        setColumnNames(workerColumns);
        addColumnNames(tableLayout);
        for (Employee employee : employees) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(getTextViewForTable(valueOf(employee.getId())));
            tableRow.addView(getTextViewForTable(employee.getName()));
            tableRow.addView(getTextViewForTable(employee.getSurname()));
            tableRow.addView(getTextViewForTable(getBrigadeId(employee.getBrigade())));
            tableLayout.addView(tableRow);
        }
    }

    private String getBrigadeId(Brigade brigade) {
        if (brigade == null) {
            return NULL_VALUE;
        }
        return valueOf(brigade.getId());
    }

    private String getSiteId(Site site) {
        if (site == null) {
            return NULL_VALUE;
        }
        return valueOf(site.getId());
    }

    private void setTesterTable() {
        TableLayout tableLayout = view.findViewById(R.id.staff_table);
        tableLayout.removeAllViews();
        setColumnNames(testerColumns);
        addColumnNames(tableLayout);
        for (Employee employee : employees) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(getTextViewForTable(valueOf(employee.getId())));
            tableRow.addView(getTextViewForTable(employee.getName()));
            tableRow.addView(getTextViewForTable(employee.getSurname()));
            tableRow.addView(getTextViewForTable(getRangeId(employee.getRange())));
            tableLayout.addView(tableRow);
        }
    }

    private String getRangeId(Range range) {
        if (range == null) {
            return NULL_VALUE;
        }
        return valueOf(range.getId());
    }

    private String getSelectedCategory() {
        Spinner staffCategories = view.findViewById(R.id.staff_types_spinner);
        return (String) staffCategories.getSelectedItem();
    }

    private void setSpinner() {
        Spinner staffCategories = view.findViewById(R.id.staff_types_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                Employee.employeeTables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staffCategories.setAdapter(adapter);
    }

    private class EngineeringStaffCallback implements Callback<GeneralResponse<List<Employee>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                               Response<GeneralResponse<List<Employee>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            employees = response.body().getData();
            showEngineeringStaffTable();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
            showError();
        }
    }

    private class WorkerCallback implements Callback<GeneralResponse<List<Employee>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                               Response<GeneralResponse<List<Employee>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            employees = response.body().getData();
            setWorkerTable();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
            showError();
        }
    }
}
