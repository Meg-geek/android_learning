package com.nsu.db.aircraft.view.staff.brigade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BrigadeDetailFragment extends FragmentWithFragmentActivity {
    private View view;
    private Brigade brigade;
    private List<Employee> brigadeWorkers;

    public BrigadeDetailFragment(Brigade brigade) {
        this.brigade = brigade;
    }

    public BrigadeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_brigade_detail, container, false);
        setBrigadeName();
        sendWorkersRequest();
        return view;
    }

    private void sendWorkersRequest() {
        NetworkService.getInstance()
                .getWorkerApi()
                .getByBrigade(brigade)
                .enqueue(new BrigadeCallback());
    }

    private void setBrigadeName() {
        TextView brigadeName = view.findViewById(R.id.brigade_name);
        brigadeName.setText(brigade.toString());
    }

    private void updateWorkersList() {
        ListView workersList = view.findViewById(R.id.workers_list);
        List<String> workersNames = new ArrayList<>();
        brigadeWorkers.forEach(worker -> workersNames.add(worker.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, workersNames);
        workersList.setAdapter(namesAdapter);
    }

    private class BrigadeCallback implements Callback<GeneralResponse<List<Employee>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                               Response<GeneralResponse<List<Employee>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            brigadeWorkers = response.body().getData();
            updateWorkersList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
            showError();
        }
    }
}
