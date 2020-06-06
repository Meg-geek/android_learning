package com.nsu.db.aircraft.view.shared.tests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Range;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.tests.range.RangeDetailFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RangesRequestFragment extends FragmentWithFragmentActivity {
    private Product product;
    private View view;
    private List<Range> ranges;

    public RangesRequestFragment() {
        // Required empty public constructor
    }

    public RangesRequestFragment(Product product) {
        this.product = product;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranges_request, container, false);
        setRangesList();
        sendRangesRequest();
        return view;
    }

    private void setRangesList() {
        ListView rangesList = view.findViewById(R.id.ranges_list);
        rangesList.setOnItemClickListener((parent, view, position, id) ->
                startFragment(new RangeDetailFragment(ranges.get(position))));
    }

    private void updateRangesList() {
        ListView rangesList = view.findViewById(R.id.ranges_list);
        List<String> rangeNames = new ArrayList<>();
        ranges.forEach(range -> rangeNames.add(range.getName()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, rangeNames);
        rangesList.setAdapter(namesAdapter);
    }

    private void sendRangesRequest() {
        NetworkService.getInstance()
                .getRangeApi()
                .getByProductId(product.getId())
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
}
