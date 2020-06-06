package com.nsu.db.aircraft.view.staff.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nsu.db.aircraft.api.model.staff.Employee.ALL;
import static com.nsu.db.aircraft.api.model.staff.Employee.ENGINEER;
import static com.nsu.db.aircraft.api.model.staff.Employee.GUILD_MANAGER;
import static com.nsu.db.aircraft.api.model.staff.Employee.LOCKSMITH;
import static com.nsu.db.aircraft.api.model.staff.Employee.MASTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.PICKER;
import static com.nsu.db.aircraft.api.model.staff.Employee.SITE_MANAGER;
import static com.nsu.db.aircraft.api.model.staff.Employee.TECHNICIAN;
import static com.nsu.db.aircraft.api.model.staff.Employee.TESTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.TURNER;
import static com.nsu.db.aircraft.api.model.staff.Employee.WELDER;

public class StaffAllFragment extends FragmentWithFragmentActivity {
    private View view;
    private List<Employee> employees;

    public StaffAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_all, container, false);
        setSpinner();
        setEmployeeList();
        setShowButton();
        return view;
    }

    private void setEmployeeList() {
        ListView employeesList = view.findViewById(R.id.staff_list_view);
        employeesList.setOnItemClickListener((parent, view1, position, id) -> {
            if (position >= 0 && position < employees.size()) {
                startFragment(new StaffDetailFragment(employees.get(position)));
            }
        });
    }

    private void setSpinner() {
        Spinner staffCategories = view.findViewById(R.id.staff_category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                Employee.employeeCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staffCategories.setAdapter(adapter);
    }

    private String getSelectedCategory() {
        Spinner staffCategories = view.findViewById(R.id.staff_category_spinner);
        return (String) staffCategories.getSelectedItem();
    }

    private void setShowButton() {
        Button showButton = view.findViewById(R.id.show_staff);
        showButton.setOnClickListener(v -> showStaff(getSelectedCategory(), true));
    }

    private void showStaff(String category, boolean isSingleRequest) {
        switch (category) {
            case TESTER:
                sendTestersRequest(isSingleRequest);
                break;
            case ENGINEER:
                sendEngineerRequest(isSingleRequest);
                break;
            case LOCKSMITH:
                sendLocksmithRequest(isSingleRequest);
                break;
            case ALL:
                sendAllRequest();
                break;
            case MASTER:
                sendMasterRequest(isSingleRequest);
                break;
            case PICKER:
                sendPickerRequest(isSingleRequest);
                break;
            case TECHNICIAN:
                sendTechnicianRequest(isSingleRequest);
                break;
            case TURNER:
                sendTurnerRequest(isSingleRequest);
                break;
            case WELDER:
                sendWelderRequest(isSingleRequest);
                break;
            case GUILD_MANAGER:
                sendGuildManagerRequest(isSingleRequest);
                break;
            case SITE_MANAGER:
                sendSiteManagerRequest(isSingleRequest);
                break;
        }
    }

    private void sendGuildManagerRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getGuildManagers()
                .enqueue(new EmployeeCallBack(GUILD_MANAGER, isSingleRequest));
    }

    private void sendSiteManagerRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getSiteApi()
                .getSiteManagers()
                .enqueue(new EmployeeCallBack(SITE_MANAGER, isSingleRequest));
    }

    private void sendWelderRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getWelderApi()
                .getAll()
                .enqueue(new EmployeeCallBack(WELDER, isSingleRequest));
    }

    private void sendTurnerRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getTurnerApi()
                .getAll()
                .enqueue(new EmployeeCallBack(TURNER, isSingleRequest));
    }

    private void sendTechnicianRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getTechnicianApi()
                .getAll()
                .enqueue(new EmployeeCallBack(TECHNICIAN, isSingleRequest));
    }

    private void sendPickerRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getPickerApi()
                .getAll()
                .enqueue(new EmployeeCallBack(PICKER, isSingleRequest));
    }

    private void sendMasterRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getMasterApi()
                .getAll()
                .enqueue(new EmployeeCallBack(MASTER, isSingleRequest));
    }

    private void sendAllRequest() {
        employees = new ArrayList<>();
        for (String category : Employee.employeeTables) {
            if (category.equals(ALL)) {
                continue;
            }
            showStaff(category, false);
        }
    }

    private void sendLocksmithRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getLocksmithApi()
                .getAll()
                .enqueue(new EmployeeCallBack(LOCKSMITH, isSingleRequest));
    }

    private void sendEngineerRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getEngineerApi()
                .getAll()
                .enqueue(new EmployeeCallBack(ENGINEER, isSingleRequest));
    }

    private void sendTestersRequest(boolean isSingleRequest) {
        NetworkService.getInstance()
                .getTesterApi()
                .getAll()
                .enqueue(new EmployeeCallBack(TESTER, isSingleRequest));
    }

    private void showEmployees() {
        ListView employeesList = view.findViewById(R.id.staff_list_view);
        List<String> employeeNames = new ArrayList<>();
        employees.forEach(employee -> employeeNames.add(employee.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, employeeNames);
        employeesList.setAdapter(namesAdapter);
    }

    @AllArgsConstructor
    private class EmployeeCallBack implements Callback<GeneralResponse<List<Employee>>> {
        private String employeeCategory;
        private boolean isSingleRequest;

        @Override
        public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                               Response<GeneralResponse<List<Employee>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            List<Employee> employeesResponse = response.body().getData();
            employeesResponse.forEach(employee -> employee.setEmployeeCategory(employeeCategory));
            if (isSingleRequest) {
                employees = employeesResponse;
            } else {
                employees.addAll(employeesResponse);
            }
            showEmployees();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
            showError();
        }
    }
}
