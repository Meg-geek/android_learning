package com.nsu.db.aircraft.view.company.site;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SiteAllFragment extends FragmentWithFragmentActivity {
    private View view;
    private List<Site> sites;

    public SiteAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_site_all, container, false);
        setSitesListView();
        sendSitesRequest();
        return view;
    }

    private void sendSitesRequest() {
        NetworkService.getInstance()
                .getSiteApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Site>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Site>>> call,
                                           Response<GeneralResponse<List<Site>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        sites = response.body().getData();
                        updateSitesList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Site>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateSitesList() {
        ListView sitesListView = view.findViewById(R.id.site_list_view);
        List<String> siteNames = new ArrayList<>();
        sites.forEach(site -> siteNames.add(site.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, siteNames);
        sitesListView.setAdapter(namesAdapter);
        TextView textView = view.findViewById(R.id.load_text);
        textView.setVisibility(View.INVISIBLE);
    }

    private void setSitesListView() {
        ListView sitesList = view.findViewById(R.id.site_list_view);
        sitesList.setOnItemClickListener((parent, view1, position, id) -> {
            if (position >= 0 && position < sites.size()) {
                startFragment(new SiteDetailFragment(sites.get(position)));
            }
        });
    }
}
