package com.nsu.db.aircraft.view.staff.brigade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrigadeMainFragment extends FragmentWithFragmentActivity {

    public BrigadeMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brigade_main, container, false);
        setStartFragmentButton(view, R.id.button_show_brigades_table, new BrigadeTableFragment());
        setStartFragmentButton(view, R.id.button_brigade_show_all, new BrigadeAllFragment());
        setCreateButton(view);
        return view;
    }

    private void setCreateButton(View view) {
        Button createButton = view.findViewById(R.id.button_add_brigade);
        createButton.setOnClickListener(v -> createBrigade());
    }

    private void createBrigade() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .addBrigade(new Brigade())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call,
                                           Response<GeneralResponse> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        showText(R.string.brigade_success_create);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        showError();
                    }
                });
    }
}
