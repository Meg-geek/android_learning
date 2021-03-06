package com.nsu.db.aircraft.view.staff.brigade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BrigadeAllFragment extends FragmentWithFragmentActivity {
    private View view;
    private List<Brigade> brigades;

    public BrigadeAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_brigade_all, container, false);
        setBrigadeListView();
        sendBrigadesRequest();
        return view;
    }

    private void setBrigadeListView() {
        ListView brigadesListView = view.findViewById(R.id.brigades_list);
        brigadesListView.setOnItemClickListener((parent, view, position, id) ->
                startFragment(new BrigadeDetailFragment(brigades.get(position))));
    }

    private void sendBrigadesRequest() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .getAllBrigades()
                .enqueue(new BrigadesCallback());
    }

    private void updateBrigadesList() {
        ListView brigadesList = view.findViewById(R.id.brigades_list);
        List<String> brigadeNames = new ArrayList<>();
        brigades.forEach(brigade -> brigadeNames.add(brigade.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, brigadeNames);
        brigadesList.setAdapter(namesAdapter);
    }

    private class BrigadesCallback implements Callback<GeneralResponse<List<Brigade>>> {

        @Override
        public void onResponse(Call<GeneralResponse<List<Brigade>>> call,
                               Response<GeneralResponse<List<Brigade>>> response) {
            if (!response.isSuccessful()) {
                showError();
                return;
            }
            brigades = response.body().getData();
            updateBrigadesList();
        }

        @Override
        public void onFailure(Call<GeneralResponse<List<Brigade>>> call, Throwable t) {
            showError();
        }
    }
}
