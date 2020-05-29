package com.nsu.db.aircraft.view.company.guild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.Status;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllGuildsFragment extends FragmentWithFragmentActivity {
    private List<Guild> guilds;

    public AllGuildsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_guilds, container, false);
        setGuildsListView(view);
        sendGuildsRequest();
        return view;
    }

    private void setGuildsListView(View view) {
        ListView guildsList = view.findViewById(R.id.all_guilds_list_view);
        guildsList.setOnItemClickListener((parent, view1, position, id) -> {
            if (position >= 0 && position < guilds.size()) {
                startFragment(new GuildDetailFragment(guilds.get(position)));
            }
        });
    }

    private void sendGuildsRequest() {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getAllGuilds()
                .enqueue(new Callback<GeneralResponse<List<Guild>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Guild>>> call,
                                           Response<GeneralResponse<List<Guild>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        GeneralResponse<List<Guild>> generalResponse = response.body();
                        if (generalResponse.getStatus() != Status.OK) {
                            showError();
                            return;
                        }
                        guilds = generalResponse.getData();
                        updateGuildsList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Guild>>> call,
                                          Throwable t) {
                        showError();
                    }
                });
    }


    private void updateGuildsList() {
        ListView guildsList = getView().findViewById(R.id.all_guilds_list_view);
        List<String> guildsNames = new ArrayList<>();
        guilds.forEach(guild -> guildsNames.add(guild.getGuildName()));
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, guildsNames);
        guildsList.setAdapter(namesAdapter);
        TextView textView = getView().findViewById(R.id.load_text);
        textView.setVisibility(View.INVISIBLE);
    }

}
