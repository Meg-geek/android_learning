package com.nsu.db.aircraft.view.staff.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.nsu.db.aircraft.api.model.staff.Employee.ENGINEER;
import static com.nsu.db.aircraft.api.model.staff.Employee.ENGINEERING_STAFF;
import static com.nsu.db.aircraft.api.model.staff.Employee.LOCKSMITH;
import static com.nsu.db.aircraft.api.model.staff.Employee.MASTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.PICKER;
import static com.nsu.db.aircraft.api.model.staff.Employee.TECHNICIAN;
import static com.nsu.db.aircraft.api.model.staff.Employee.TESTER;
import static com.nsu.db.aircraft.api.model.staff.Employee.TURNER;
import static com.nsu.db.aircraft.api.model.staff.Employee.WELDER;
import static com.nsu.db.aircraft.api.model.staff.Employee.WORKER;
import static java.util.Arrays.asList;


public class StaffDetailFragment extends FragmentWithFragmentActivity {
    private final static int MIN_SIZE = 1;
    private final static String WRONG_STAFF_NAME = "Неправильно введено имя. " +
            INPUT_RULES;
    private final static String WRONG_STAFF_SURNAME = "Неправильно введена фамилия. "
            + INPUT_RULES;
    private Employee employee;
    private View view;
    private boolean isAddingFragment = true;
    private List<Brigade> brigades;
    private List<Range> ranges;
    private List<Site> sites;

    public StaffDetailFragment() {
        // Required empty public constructor
    }

    public StaffDetailFragment(Employee employee) {
        this.employee = employee;
        isAddingFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_detail, container, false);
        setSpecialitiesSpinner();
        if (isAddingFragment) {
            setAddingFragment();
        } else {
            setDetailFragment();
        }
        return view;
    }

    private void setAddingFragment() {
        setVisibilityForAdding();
        setAddButton();
    }

    private void setDetailFragment() {
        setVisibilityForDetailFragment();
        setEnabledForDetailFragment();
        setEmployeeData();
        setDeleteButton();
        setChangeButton();
        setSaveButton();
    }

    private void setChangeButton() {
        Button changeButton = view.findViewById(R.id.button_change);
        changeButton.setOnClickListener(v -> {
            setVisibility(R.id.button_save, VISIBLE);
            setEnabled(view.findViewById(R.id.staff_details_spinner), true);
            setEnabled(view.findViewById(R.id.name_edit_text), true);
            setEnabled(view.findViewById(R.id.surname_edit_text), true);
            Spinner specialitiesSpinner = view.findViewById(R.id.speciality_spinner);
            updateSpecialSpinner((String) specialitiesSpinner.getSelectedItem(), false);
        });
    }


    private void setSaveButton() {
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            hideKeyboard();
            if (isNameFieldsWrong()) {
                showInputErrors();
                return;
            }
            hideInputError(R.id.name_input_layout);
            hideInputError(R.id.surname_layout);
            sendUpdateRequest();
        });
    }

    private void sendUpdateRequest() {
        employee.setName(getEnteredName(R.id.name_edit_text));
        employee.setSurname(getEnteredName(R.id.surname_edit_text));
        setUpdateDetail();
        NetworkService networkService = NetworkService.getInstance();
        Call<GeneralResponse<Employee>> call = null;
        Spinner categorySpinner = view.findViewById(R.id.staff_category_spinner);
        String category = (String) categorySpinner.getSelectedItem();
        switch (category) {
            case TESTER:
                call = networkService
                        .getTesterApi().update(employee);
                break;
            case ENGINEER:
                call = networkService
                        .getEngineerApi().update(employee);
                break;
            case LOCKSMITH:
                call = networkService
                        .getLocksmithApi().update(employee);
                break;
            case MASTER:
                call = networkService
                        .getMasterApi().update(employee);
                break;
            case PICKER:
                call = networkService
                        .getPickerApi().update(employee);
                break;
            case TECHNICIAN:
                call = networkService
                        .getTechnicianApi().update(employee);
                break;
            case TURNER:
                call = networkService
                        .getTurnerApi().update(employee);
                break;
            case WELDER:
                call = networkService
                        .getWelderApi().update(employee);
                break;
        }
        if (call != null) {
            call.enqueue(new EmployeeCallback());
        }
    }

    private void setUpdateDetail() {
        Spinner detailsSpinner = view.findViewById(R.id.staff_details_spinner);
        int selectedPosition = detailsSpinner.getSelectedItemPosition();
        if (employee.getSite() != null) {
            employee.setSite(sites.get(selectedPosition));
        }
        if (employee.getRange() != null) {
            employee.setRange(ranges.get(selectedPosition));
        }
        if (employee.getBrigade() != null) {
            employee.setBrigade(brigades.get(selectedPosition));
        }
    }

    private void setDeleteButton() {
        Button deleteButton = view.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> sendDeleteRequest());
    }

    private void sendDeleteRequest() {
        NetworkService networkService = NetworkService.getInstance();
        Call<GeneralResponse> call = null;
        Spinner categorySpinner = view.findViewById(R.id.staff_category_spinner);
        switch ((String) categorySpinner.getSelectedItem()) {
            case TESTER:
                call = networkService
                        .getTesterApi().deleteById(employee.getId());
                break;
            case ENGINEER:
                call = networkService
                        .getEngineerApi().deleteById(employee.getId());
                break;
            case LOCKSMITH:
                call = networkService
                        .getLocksmithApi().deleteById(employee.getId());
                break;
            case MASTER:
                call = networkService
                        .getMasterApi().deleteById(employee.getId());
                break;
            case PICKER:
                call = networkService
                        .getPickerApi().deleteById(employee.getId());
                break;
            case TECHNICIAN:
                call = networkService
                        .getTechnicianApi().deleteById(employee.getId());
                break;
            case TURNER:
                call = networkService
                        .getTurnerApi().deleteById(employee.getId());
                break;
            case WELDER:
                call = networkService
                        .getWelderApi().deleteById(employee.getId());
                break;
        }
        if (call != null) {
            call.enqueue(new GeneralResponseCallback());
        }
    }

    private void setEmployeeData() {
        setNameAndSurname();
        setStaffDetails();
    }

    private void setStaffDetails() {
        TextView specialityFeature = view.findViewById(R.id.speciality_feature);
        if (employee.getEmployeeCategory() != null) {
            updateSpinner(view, R.id.staff_category_spinner, asList(employee.getEmployeeCategory()));
        }
        if (employee.getSite() != null) {
            updateSpinner(view, R.id.speciality_spinner, asList(ENGINEERING_STAFF));
            specialityFeature.setText(R.string.sites);
            sites = asList(employee.getSite());
            updateSitesSpinner();
        }
        if (employee.getRange() != null) {
            updateSpinner(view, R.id.speciality_spinner, asList(TESTER));
            specialityFeature.setText(R.string.test_labs_title);
            ranges = asList(employee.getRange());
            updateRangesSpinner();
        }
        if (employee.getBrigade() != null) {
            updateSpinner(view, R.id.speciality_spinner, asList(WORKER));
            specialityFeature.setText(R.string.brigades);
            brigades = asList(employee.getBrigade());
            updateBrigadesSpinner();
        }
    }

    private void setNameAndSurname() {
        TextInputEditText name = view.findViewById(R.id.name_edit_text);
        name.setText(employee.getName());
        TextInputEditText surname = view.findViewById(R.id.surname_edit_text);
        surname.setText(employee.getSurname());
    }

    private void setEnabledForDetailFragment() {
        setEnabled(view.findViewById(R.id.speciality_spinner), false);
        setEnabled(view.findViewById(R.id.staff_details_spinner), false);
        setEnabled(view.findViewById(R.id.staff_category_spinner), false);
        setEnabled(view.findViewById(R.id.name_edit_text), false);
        setEnabled(view.findViewById(R.id.surname_edit_text), false);
    }

    private void setVisibilityForDetailFragment() {
        setVisibility(R.id.button_add, INVISIBLE);
        setVisibility(R.id.button_save, INVISIBLE);
    }

    private void setAddButton() {
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            hideKeyboard();
            if (isNameFieldsWrong()) {
                showInputErrors();
                return;
            }
            hideInputError(R.id.name_input_layout);
            hideInputError(R.id.surname_layout);
            sendAddRequest();
        });
    }

    private boolean isNameFieldsWrong() {
        return isNameFieldWrong(R.id.surname_edit_text)
                || isNameFieldWrong(R.id.name_edit_text);
    }

    private void showInputErrors() {
        if (isNameFieldWrong(R.id.surname_edit_text)) {
            showInputError(WRONG_STAFF_SURNAME, R.id.surname_layout);
        }
        if (isNameFieldWrong(R.id.name_edit_text)) {
            showInputError(WRONG_STAFF_NAME, R.id.name_input_layout);
        }
    }

    private void sendAddRequest() {
        Spinner specialitiesSpinner = view.findViewById(R.id.speciality_spinner);
        String speciality = (String) specialitiesSpinner.getSelectedItem();
        employee = new Employee();
        employee.setName(getEnteredName(R.id.name_edit_text));
        employee.setSurname(getEnteredName(R.id.surname_edit_text));
        Spinner staffDetails = view.findViewById(R.id.staff_details_spinner);
        int selectedPosition = staffDetails.getSelectedItemPosition();
        switch (speciality) {
            case WORKER:
                employee.setBrigade(brigades.get(selectedPosition));
                sendAddWorkerRequest();
                break;
            case ENGINEERING_STAFF:
                employee.setSite(sites.get(selectedPosition));
                sendAddEngineeringStaffRequest();
                break;
            case TESTER:
                employee.setRange(ranges.get(selectedPosition));
                sendAddTesterRequest();
                break;
        }
    }

    private void sendAddEngineeringStaffRequest() {
        NetworkService networkService = NetworkService.getInstance();
        Call<GeneralResponse> call = null;
        switch (getSelectedCategory()) {
            case ENGINEER:
                call = networkService
                        .getEngineerApi().add(employee);
                break;
            case TECHNICIAN:
                call = networkService
                        .getTechnicianApi().add(employee);
                break;
        }
        if (call != null) {
            call.enqueue(new GeneralResponseCallback());
        }
    }

    private void sendAddWorkerRequest() {
        NetworkService networkService = NetworkService.getInstance();
        Call<GeneralResponse> call = null;
        switch (getSelectedCategory()) {
            case LOCKSMITH:
                call = networkService
                        .getLocksmithApi().add(employee);
                break;
            case WELDER:
                call = networkService
                        .getWelderApi().add(employee);
                break;
            case TURNER:
                call = networkService
                        .getTurnerApi().add(employee);
                break;
            case PICKER:
                call = networkService
                        .getPickerApi().add(employee);
                break;
        }
        if (call != null) {
            call.enqueue(new GeneralResponseCallback());
        }
    }

    private String getSelectedCategory() {
        Spinner staffCategory = view.findViewById(R.id.staff_category_spinner);
        return (String) staffCategory.getSelectedItem();
    }

    private void sendAddTesterRequest() {
        NetworkService.getInstance()
                .getTesterApi()
                .add(employee)
                .enqueue(new GeneralResponseCallback());
    }

    private void setSpecialitiesSpinner() {
        updateSpinner(view, R.id.speciality_spinner, Employee.specialities);
        Spinner specialitiesSpinner = view.findViewById(R.id.speciality_spinner);
        specialitiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
                if (isAddingFragment) {
                    updateSpecialSpinner(Employee.specialities.get(position), true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (isAddingFragment) {
            updateSpecialSpinner(WORKER, true);
        }
    }

    private void updateSpecialSpinner(String speciality, boolean needUpdateCategory) {
        TextView specialityFeature = view.findViewById(R.id.speciality_feature);
        switch (speciality) {
            case WORKER:
                sendBrigadesRequest();
                specialityFeature.setText(R.string.brigades);
                if (needUpdateCategory) {
                    updateSpinner(view, R.id.staff_category_spinner, Employee.workers);
                }
                break;
            case ENGINEERING_STAFF:
                sendSitesRequest();
                specialityFeature.setText(R.string.sites);
                if (needUpdateCategory) {
                    updateSpinner(view, R.id.staff_category_spinner, Employee.engineeringStaff);
                }
                break;
            case TESTER:
                sendRangesRequest();
                specialityFeature.setText(R.string.test_labs_title);
                if (needUpdateCategory) {
                    updateSpinner(view, R.id.staff_category_spinner, asList(TESTER));
                }
                break;
        }
    }

    private void updateRangesSpinner() {
        List<String> rangeNames = ranges.stream()
                .map(Range::toString).collect(Collectors.toList());
        updateSpinner(view, R.id.staff_details_spinner, rangeNames);
    }

    private void sendRangesRequest() {
        if (ranges != null && ranges.size() > MIN_SIZE) {
            updateRangesSpinner();
            return;
        }
        NetworkService.getInstance()
                .getRangeApi()
                .getAll()
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
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Range>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateSitesSpinner() {
        List<String> siteNames = sites.stream()
                .map(Site::toString).collect(Collectors.toList());
        updateSpinner(view, R.id.staff_details_spinner, siteNames);
    }

    private void sendSitesRequest() {
        if (sites != null && sites.size() > MIN_SIZE) {
            updateSitesSpinner();
            return;
        }
        NetworkService.getInstance()
                .getSiteApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Site>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Site>>> call,
                                           Response<GeneralResponse<List<Site>>> response) {
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
                });
    }

    private void updateBrigadesSpinner() {
        List<String> brigadeNames = brigades.stream()
                .map(Brigade::toString).collect(Collectors.toList());
        updateSpinner(view, R.id.staff_details_spinner, brigadeNames);
    }

    private void sendBrigadesRequest() {
        if (brigades != null && brigades.size() > MIN_SIZE) {
            updateBrigadesSpinner();
            return;
        }
        NetworkService.getInstance()
                .getBrigadeApi()
                .getAllBrigades()
                .enqueue(new Callback<GeneralResponse<List<Brigade>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Brigade>>> call,
                                           Response<GeneralResponse<List<Brigade>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        brigades = response.body().getData();
                        updateBrigadesSpinner();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Brigade>>> call, Throwable t) {
                        showError();
                    }
                });
    }


    private void setVisibility(int resourceId, int visibility) {
        setVisibility(view, resourceId, visibility);
    }

    private void setVisibilityForAdding() {
        Button button = view.findViewById(R.id.button_add);
        setVisibility(R.id.button_add, VISIBLE);
        button = view.findViewById(R.id.button_change);
        setVisibility(R.id.button_change, INVISIBLE);
        button = view.findViewById(R.id.button_save);
        setVisibility(R.id.button_save, INVISIBLE);
        button = view.findViewById(R.id.button_delete);
        setVisibility(R.id.button_delete, INVISIBLE);
    }

    private class GeneralResponseCallback implements Callback<GeneralResponse> {
        @Override
        public void onResponse(Call<GeneralResponse> call,
                               Response<GeneralResponse> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            if (isAddingFragment) {
                showText(R.string.load_success);
            } else {
                showText(R.string.success_delete);
            }
            startFragment(new StaffMainFragment());
        }

        @Override
        public void onFailure(Call<GeneralResponse> call, Throwable t) {
            showError();
        }
    }

    private class EmployeeCallback implements Callback<GeneralResponse<Employee>> {

        @Override
        public void onResponse(Call<GeneralResponse<Employee>> call,
                               Response<GeneralResponse<Employee>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            showText(R.string.update_success);
            startFragment(new StaffMainFragment());
        }

        @Override
        public void onFailure(Call<GeneralResponse<Employee>> call, Throwable t) {
            showError();
        }
    }
}
