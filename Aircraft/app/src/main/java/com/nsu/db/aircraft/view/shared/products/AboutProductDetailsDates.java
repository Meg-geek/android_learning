package com.nsu.db.aircraft.view.shared.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Setter;

@Setter
public class AboutProductDetailsDates extends FragmentWithFragmentActivity {
    private Product product;
    private List<Range> ranges;
    private boolean isEquipmentForm = false;
    private boolean isTestersForm = false;
    private boolean isBeginDate = false;
    private GregorianCalendar beginDate, endDate;
    private View view;

    public AboutProductDetailsDates() {
        // Required empty public constructor
    }

    public void setIsBeginDate(boolean isBeginDate) {
        this.isBeginDate = isBeginDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_product_details_dates,
                container, false);
        setDateIfPresent();
        Button setButton = view.findViewById(R.id.button_set);
        setButton.setOnClickListener(v -> createAndStartProductRequestDetails());
        return view;
    }

    private void createAndStartProductRequestDetails() {
        getDate();
        AboutProductDetailsRequest productRequestDetails = new AboutProductDetailsRequest();
        productRequestDetails.setBeginDate(beginDate);
        productRequestDetails.setEndDate(endDate);
        productRequestDetails.setEquipmentForm(isEquipmentForm);
        productRequestDetails.setProduct(product);
        productRequestDetails.setRanges(ranges);
        productRequestDetails.setTestersForm(isTestersForm);
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
}
