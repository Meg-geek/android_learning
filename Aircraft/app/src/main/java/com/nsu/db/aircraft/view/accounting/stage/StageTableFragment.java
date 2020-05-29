package com.nsu.db.aircraft.view.accounting.stage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Stage;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.TableFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;


public class StageTableFragment extends TableFragment {
    private List<Stage> stages;

    public StageTableFragment() {
        super(asList("id", "stage_name"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage_table, container, false);
        getStages(view);
        return view;
    }

    private void getStages(View view) {
        NetworkService.getInstance()
                .getStageApi()
                .getAll().enqueue(new Callback<GeneralResponse<List<Stage>>>() {
            @Override
            public void onResponse(Call<GeneralResponse<List<Stage>>> call,
                                   Response<GeneralResponse<List<Stage>>> response) {
                if (!response.isSuccessful()) {
                    showError();
                    return;
                }
                stages = response.body().getData();
                setTable(view);
            }

            @Override
            public void onFailure(Call<GeneralResponse<List<Stage>>> call, Throwable t) {
                showError();
            }
        });
    }


    private void setTable(View view) {
        TableLayout tableLayout = view.findViewById(R.id.stages_table);
        addColumnNames(tableLayout);
        for (Stage stage : stages) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(getTextViewForTable(valueOf(stage.getId())));
            tableRow.addView(getTextViewForTable(stage.getStageName()));
            tableLayout.addView(tableRow);
        }
    }
}
