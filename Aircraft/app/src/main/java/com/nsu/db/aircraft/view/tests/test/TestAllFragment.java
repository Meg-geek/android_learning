package com.nsu.db.aircraft.view.tests.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.tests.Test;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestAllFragment extends FragmentWithFragmentActivity {
    private View view;
    private List<Test> tests;

    public TestAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test_all, container, false);
        setTestsList();
        sendTestsRequest();
        return view;
    }

    private void setTestsList() {
        ListView testsList = view.findViewById(R.id.list_view);
        testsList.setOnItemClickListener((parent, view, position, id) ->
                startFragment(new TestDetailFragment(tests.get(position))));
    }

    private void sendTestsRequest() {
        NetworkService.getInstance()
                .getTestApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Test>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Test>>> call,
                                           Response<GeneralResponse<List<Test>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        tests = response.body().getData();
                        updateTestsList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Test>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateTestsList() {
        ListView testsList = view.findViewById(R.id.list_view);
        List<String> testNames = tests.stream()
                .map(Test::toString).collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, testNames);
        testsList.setAdapter(namesAdapter);
    }
}
