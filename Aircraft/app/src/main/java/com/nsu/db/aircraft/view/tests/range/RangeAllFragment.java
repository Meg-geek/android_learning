package com.nsu.db.aircraft.view.tests.range;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RangeAllFragment extends FragmentWithFragmentActivity {
    private View view;
    private List<Range> ranges;

    public RangeAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_range_all, container, false);
        sendRangesRequest();
        setRangesListView();
        return view;
    }

    private void setRangesListView() {
        ListView rangesList = view.findViewById(R.id.all_ranges_list_view);
        rangesList.setOnItemClickListener((parent, view, position, id) ->
                startFragment(new RangeDetailFragment(ranges.get(position))));
    }

    private void sendRangesRequest() {
        NetworkService.getInstance()
                .getRangeApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Range>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Range>>> call,
                                           Response<GeneralResponse<List<Range>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        ranges = response.body().getData();
                        updateRangesList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Range>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateRangesList() {
        ListView rangesList = view.findViewById(R.id.all_ranges_list_view);
        List<String> rangesNames = new ArrayList<>();
        ranges.forEach(range -> rangesNames.add(range.getName()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, rangesNames);
        rangesList.setAdapter(namesAdapter);
    }
}
