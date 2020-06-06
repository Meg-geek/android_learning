package com.nsu.db.aircraft.view.shared.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.staff.Brigade;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;
import com.nsu.db.aircraft.view.staff.brigade.BrigadeDetailFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BrigadeRequestFragment extends FragmentWithFragmentActivity {
    private Guild guild;
    private Site site;
    private Product product;

    private View view;
    private List<Brigade> brigades;

    public BrigadeRequestFragment() {
        // Required empty public constructor
    }

    public BrigadeRequestFragment(Guild guild) {
        this.guild = guild;
    }

    public BrigadeRequestFragment(Site site) {
        this.site = site;
    }

    public BrigadeRequestFragment(Product product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_brigade_request, container, false);
        setBrigadeListView();
        sendBrigadesRequest();
        return view;
    }

    private void setBrigadeListView() {
        ListView brigadesList = view.findViewById(R.id.brigades_list);
        brigadesList.setOnItemClickListener((parent, view, position, id) ->
                startFragment(new BrigadeDetailFragment(brigades.get(position))));
    }

    private void sendBrigadesRequest() {
        if (product != null) {
            sendProductBrigadesRequest();
            return;
        }
        if (guild != null) {
            sendGuildBrigadesRequest();
            return;
        }
        if (site != null) {
            sendSiteBrigadesRequest();
            return;
        }
    }

    private void updateBrigadesList() {
        ListView brigadesList = view.findViewById(R.id.brigades_list);
        List<String> brigadeNames = new ArrayList<>();
        brigades.forEach(brigade -> brigadeNames.add(brigade.toString()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, brigadeNames);
        brigadesList.setAdapter(namesAdapter);
    }

    private void sendGuildBrigadesRequest() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .getByGuildId(guild.getId())
                .enqueue(new BrigadesCallback());
    }

    private void sendSiteBrigadesRequest() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .getBySiteId(site.getId())
                .enqueue(new BrigadesCallback());
    }

    private void sendProductBrigadesRequest() {
        NetworkService.getInstance()
                .getBrigadeApi()
                .getByProductId(product.getId())
                .enqueue(new BrigadesCallback());
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
