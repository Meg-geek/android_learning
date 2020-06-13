package com.nsu.db.aircraft.view.accounting.accounting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.accounting.Accounting;
import com.nsu.db.aircraft.api.model.accounting.Stage;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Test;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Setter;

@Setter
public class AccountingSetDateFragment extends FragmentWithFragmentActivity {
    private boolean isBeginDate;
    private boolean isAddFragment = true;
    private Accounting accounting;
    private View view;
    private List<Product> products = new ArrayList<>();
    private List<Stage> stages;
    private List<Site> sites;
    private List<Test> tests;
    private GregorianCalendar beginDate, endDate;

    public AccountingSetDateFragment() {
        // Required empty public constructor
    }

    public void setIsBeginDate(boolean isBeginDate) {
        this.isBeginDate = isBeginDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater
                .inflate(R.layout.fragment_accounting_set_date, container, false);
        setDateIfPresent();
        Button setButton = view.findViewById(R.id.button_set);
        setButton.setOnClickListener(v -> createAndStartAccountingDetails());
        return view;
    }

    private void createAndStartAccountingDetails() {
        getDate();
        AccountingDetailFragment accountingDetailFragment = new AccountingDetailFragment();
        accountingDetailFragment.setAccounting(accounting);
        accountingDetailFragment.setAddFragment(isAddFragment);
        accountingDetailFragment.setBeginDate(beginDate);
        accountingDetailFragment.setEndDate(endDate);
        accountingDetailFragment.setProducts(products);
        accountingDetailFragment.setSites(sites);
        accountingDetailFragment.setStages(stages);
        accountingDetailFragment.setTests(tests);
        startFragment(accountingDetailFragment);
    }

    private void setDateIfPresent() {
        if (isBeginDate && beginDate != null) {
            setBeginDate();
        }
        if (!isBeginDate && endDate != null) {
            setEndDate();
        }
    }

    private void setBeginDate() {
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.init(beginDate.get(Calendar.YEAR),
                beginDate.get(Calendar.MONTH),
                beginDate.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) -> {
                });
    }

    private void setEndDate() {
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.init(endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) -> {
                });
    }

    private void getDate() {
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(
                datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        if (isBeginDate) {
            beginDate = gregorianCalendar;
        } else {
            endDate = gregorianCalendar;
        }
    }
}
