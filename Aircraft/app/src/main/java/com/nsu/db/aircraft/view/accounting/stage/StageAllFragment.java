package com.nsu.db.aircraft.view.accounting.stage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Stage;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StageAllFragment extends FragmentWithFragmentActivity {
    private List<Stage> stages;

    public StageAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage_all, container, false);
        sendStagesRequest(view);
        setStagesListView(view);
        return view;
    }

    private void setStagesListView(View view) {
        ListView stagesList = view.findViewById(R.id.stage_list_view);
        stagesList.setOnItemClickListener((parent, view1, position, id) -> {
            if (position >= 0 && position < stages.size()) {
                startFragment(new StageDetailFragment(stages.get(position)));
            }
        });
    }

    private void sendStagesRequest(View view) {
        NetworkService.getInstance()
                .getStageApi()
                .getAll().enqueue(new Callback<GeneralResponse<List<Stage>>>() {
            @Override
            public void onResponse(Call<GeneralResponse<List<Stage>>> call,
                                   Response<GeneralResponse<List<Stage>>> response) {
                if (!response.isSuccessful()) {
                    showError();
                }
                stages = response.body().getData();
                updateStagesList(view);
            }

            @Override
            public void onFailure(Call<GeneralResponse<List<Stage>>> call, Throwable t) {
                showError();
            }
        });
    }

    private void updateStagesList(View view) {
        ListView productListView = view.findViewById(R.id.stage_list_view);
        List<String> stageNames = new ArrayList<>();
        stages.forEach(stage -> stageNames.add(stage.getStageName()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, stageNames);
        productListView.setAdapter(namesAdapter);
        TextView textView = view.findViewById(R.id.load_text);
        textView.setVisibility(View.INVISIBLE);
    }
}
