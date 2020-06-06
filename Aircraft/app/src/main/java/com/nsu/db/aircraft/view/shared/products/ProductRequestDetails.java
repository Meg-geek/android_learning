package com.nsu.db.aircraft.view.shared.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import lombok.Setter;

import static java.lang.String.valueOf;

@Setter
public class ProductRequestDetails extends FragmentWithFragmentActivity {
    private Company company;
    private Site site;
    private Guild guild;
    private Range range;
    private View view;

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
        setDatesIfPresent();
        setButtons();
        return view;
    }

    private void setButtons() {
        setDateButtons();
        //TODO кнопочки четырех запросов
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
        startFragment(setDateFragment);
    }

    private void setDatesIfPresent() {
        if (beginDate != null) {
            String date = String.join(".", valueOf(beginDate.get(Calendar.DAY_OF_MONTH)),
                    valueOf(beginDate.get(Calendar.MONTH)),
                    valueOf(beginDate.get(Calendar.YEAR)));
            TextView beginDate = view.findViewById(R.id.begin_date);
            beginDate.setText(date);
        }
        if (endDate != null) {
            String date = String.join(".", valueOf(endDate.get(Calendar.DAY_OF_MONTH)),
                    valueOf(endDate.get(Calendar.MONTH)),
                    valueOf(endDate.get(Calendar.YEAR)));
            TextView beginDate = view.findViewById(R.id.end_date);
            beginDate.setText(date);
        }
    }
}
