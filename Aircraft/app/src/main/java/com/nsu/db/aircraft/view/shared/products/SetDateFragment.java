package com.nsu.db.aircraft.view.shared.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import lombok.Setter;

@Setter
public class SetDateFragment extends FragmentWithFragmentActivity {
    private View view;
    private Company company;
    private Site site;
    private Guild guild;
    private GregorianCalendar beginDate, endDate;
    private boolean isBeginDate = true;

    public SetDateFragment() {
        // Required empty public constructor
    }

    public void setIsBeginDate(boolean isBeginDate) {
        this.isBeginDate = isBeginDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set_date, container, false);
        setDateIfPresent();
        Button setButton = view.findViewById(R.id.button_set);
        setButton.setOnClickListener(v -> createAndStartProductRequestDetails());
        return view;
    }

    private void createAndStartProductRequestDetails() {
        getDate();
        ProductRequestDetails productRequestDetails = new ProductRequestDetails();
        productRequestDetails.setBeginDate(beginDate);
        productRequestDetails.setEndDate(endDate);
        productRequestDetails.setCompany(company);
        productRequestDetails.setGuild(guild);
        productRequestDetails.setSite(site);
        startFragment(productRequestDetails);
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

    private boolean areCorrectDates() {
        if (beginDate == null || endDate == null) {
            return true;
        }
        return beginDate.before(endDate);
    }
}
