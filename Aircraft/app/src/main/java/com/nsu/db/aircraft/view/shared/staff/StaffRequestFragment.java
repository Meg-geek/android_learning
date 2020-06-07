package com.nsu.db.aircraft.view.shared.staff;

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
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.staff.staff.StaffDetailFragment;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nsu.db.aircraft.api.model.staff.Employee.ALL;
import static com.nsu.db.aircraft.api.model.staff.Employee.ENGINEER;
import static com.nsu.db.aircraft.api.model.staff.Employee.LOCKSMITH;
import static com.nsu.db.aircraft.api.model.staff.Employee.MASTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.PICKER;
import static com.nsu.db.aircraft.api.model.staff.Employee.TECHNICIAN;
import static com.nsu.db.aircraft.api.model.staff.Employee.TURNER;
import static com.nsu.db.aircraft.api.model.staff.Employee.WELDER;
import static java.util.Arrays.asList;


public class StaffRequestFragment extends FragmentWithFragmentActivity {
    private Company company;
    private Guild guild;
    private Site site;
    private List<Employee> employees;

    private boolean isCompanyFragment, isGuildFragment, isSiteFragment;

    private View view;

    public StaffRequestFragment() {
        // Required empty public constructor
    }

    public StaffRequestFragment(Company company) {
        this.company = company;
    }

    public StaffRequestFragment(Guild guild) {
        this.guild = guild;
    }

    public StaffRequestFragment(Site site) {
        this.site = site;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_request, container, false);
        setFragment();
        if (isSiteFragment) {
            updateSpinner(view, R.id.spinner, asList(MASTER));
        } else {
            updateSpinner(view, R.id.spinner, Employee.categoriesAndMaster);
        }
        setShowButton();
        setEmployeesList();
        return view;
    }

    private void setEmployeesList() {
        ListView employeesList = view.findViewById(R.id.empoyee_list);
        employeesList.setOnItemClickListener((parent, v, position, id) ->
                startFragment(new StaffDetailFragment(employees.get(position))));
    }

    private void setShowButton() {
        Button showButton = view.findViewById(R.id.button_show_by_dates);
        showButton.setOnClickListener(v ->
                sendStaffRequest(getSpinnerValue(), true));
    }

    private void sendStaffRequest(String category, boolean isSingleRequest) {
        switch (category) {
            case MASTER:
                sendMasterRequest(isSingleRequest);
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
        }
    }

    private void sendEngineerRequest(boolean isSingleRequest) {
        if (isCompanyFragment) {
            NetworkService.getInstance()
                    .getEngineerApi()
                    .getByCompany(company.getId())
                    .enqueue(new EmployeeCallBack(ENGINEER, isSingleRequest));
        }
        if (isGuildFragment) {
            NetworkService.getInstance()
                    .getEngineerApi()
                    .getByGuild(guild.getId())
                    .enqueue(new EmployeeCallBack(ENGINEER, isSingleRequest));
        }
    }

    private void sendMasterRequest(boolean isSingleRequest) {
        if (isCompanyFragment) {
            NetworkService.getInstance()
                    .getMasterApi()
                    .getByCompany(company.getId())
                    .enqueue(new EmployeeCallBack(MASTER, isSingleRequest));
        }
        if (isGuildFragment) {
            NetworkService.getInstance()
                    .getMasterApi()
                    .getByGuild(guild.getId())
                    .enqueue(new EmployeeCallBack(MASTER, isSingleRequest));
        }
        if (isSiteFragment) {
            NetworkService.getInstance()
                    .getMasterApi()
                    .getBySite(site)
                    .enqueue(new EmployeeCallBack(MASTER, isSingleRequest));
        }
    }

    private void sendLocksmithRequest(boolean isSingleRequest) {
        if (isCompanyFragment) {
            NetworkService.getInstance()
                    .getLocksmithApi()
                    .getByCompany(company.getId())
                    .enqueue(new EmployeeCallBack(LOCKSMITH, isSingleRequest));
        }
        if (isGuildFragment) {
            NetworkService.getInstance()
                    .getLocksmithApi()
                    .getByGuild(guild.getId())
                    .enqueue(new EmployeeCallBack(LOCKSMITH, isSingleRequest));
        }
    }

    private void sendPickerRequest(boolean isSingleRequest) {
        if (isCompanyFragment) {
            NetworkService.getInstance()
                    .getPickerApi()
                    .getByCompany(company.getId())
                    .enqueue(new EmployeeCallBack(PICKER, isSingleRequest));
        }
        if (isGuildFragment) {
            NetworkService.getInstance()
                    .getPickerApi()
                    .getByGuild(guild.getId())
                    .enqueue(new EmployeeCallBack(PICKER, isSingleRequest));
        }
    }

    private void sendTechnicianRequest(boolean isSingleRequest) {
        if (isCompanyFragment) {
            NetworkService.getInstance()
                    .getTechnicianApi()
                    .getByCompany(company.getId())
                    .enqueue(new EmployeeCallBack(TECHNICIAN, isSingleRequest));
        }
        if (isGuildFragment) {
            NetworkService.getInstance()
                    .getTechnicianApi()
                    .getByGuild(guild.getId())
                    .enqueue(new EmployeeCallBack(TECHNICIAN, isSingleRequest));
        }
    }

    private void sendTurnerRequest(boolean isSingleRequest) {
        if (isCompanyFragment) {
            NetworkService.getInstance()
                    .getTurnerApi()
                    .getByCompany(company.getId())
                    .enqueue(new EmployeeCallBack(TURNER, isSingleRequest));
        }
        if (isGuildFragment) {
            NetworkService.getInstance()
                    .getTurnerApi()
                    .getByGuild(guild.getId())
                    .enqueue(new EmployeeCallBack(TURNER, isSingleRequest));
        }
    }

    private void sendWelderRequest(boolean isSingleRequest) {
        if (isCompanyFragment) {
            NetworkService.getInstance()
                    .getWelderApi()
                    .getByCompany(company.getId())
                    .enqueue(new EmployeeCallBack(WELDER, isSingleRequest));
        }
        if (isGuildFragment) {
            NetworkService.getInstance()
                    .getWelderApi()
                    .getByGuild(guild.getId())
                    .enqueue(new EmployeeCallBack(WELDER, isSingleRequest));
        }
    }

    private void sendAllRequest() {
        employees = new ArrayList<>();
        for (String category : Employee.categoriesAndMaster) {
            if (category.equals(ALL)) {
                continue;
            }
            sendStaffRequest(category, false);
        }
    }

    private String getSpinnerValue() {
        Spinner spinner = view.findViewById(R.id.spinner);
        return (String) spinner.getSelectedItem();
    }

    private void setFragment() {
        isCompanyFragment = company != null;
        isGuildFragment = guild != null;
        isSiteFragment = site != null;
    }

    private void showEmployees() {
        ListView employeesList = view.findViewById(R.id.empoyee_list);
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
