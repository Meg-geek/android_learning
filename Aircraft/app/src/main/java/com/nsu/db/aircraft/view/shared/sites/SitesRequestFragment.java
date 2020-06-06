package com.nsu.db.aircraft.view.shared.sites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Company;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.company.site.SiteDetailFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SitesRequestFragment extends FragmentWithFragmentActivity {
    private Company company;
    private Guild guild;
    private View view;
    private List<Site> sites;

    public SitesRequestFragment() {
        // Required empty public constructor
    }

    public SitesRequestFragment(Company company) {
        this.company = company;
    }

    public SitesRequestFragment(Guild guild) {
        this.guild = guild;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sites_request, container, false);
        setSitesListView();
        sendSitesRequest();
        return view;
    }

    private void setSitesListView() {
        ListView sitesList = view.findViewById(R.id.sites_list);
        sitesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startFragment(new SiteDetailFragment(sites.get(position)));
            }
        });
    }

    private void sendSitesRequest() {
        if (company != null) {
            sendCompanySitesRequest();
            return;
        }
        if (guild != null) {
            sendGuildSitesRequest();
            return;
        }
    }

    private void sendCompanySitesRequest() {
        NetworkService.getInstance()
                .getSiteApi()
                .getByCompanyId(company.getId())
                .enqueue(new SitesCallback());
    }

    private void sendGuildSitesRequest() {
        NetworkService.getInstance()
                .getSiteApi()
                .getByGuild(guild)
                .enqueue(new SitesCallback());
    }

    private void updateSitesList() {
        ListView sitesListView = view.findViewById(R.id.sites_list);
        List<String> siteNames = new ArrayList<>();
        sites.forEach(site -> siteNames.add(site.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, siteNames);
        sitesListView.setAdapter(namesAdapter);
    }

    private class SitesCallback implements Callback<GeneralResponse<List<Site>>> {

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
    }
}
