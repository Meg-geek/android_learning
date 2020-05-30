package com.nsu.db.aircraft.view.company.guild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.company.Guild;
import com.nsu.db.aircraft.api.model.staff.Employee;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.TableFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

public class GuildTableFragment extends TableFragment {
    private View view;
    private List<Guild> guilds;
    private Map<Integer, Integer> guildManagerId = new HashMap<>();
    private boolean areGuildsReady = false;
    private boolean areManagersReady = false;

    public GuildTableFragment() {
        super(asList("id", "company_id", "manager_id", "guild_name"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guild_table, container, false);
        getGuilds();
        getManagers();
        return view;
    }

    private void getManagers() {
        NetworkService.getInstance()
                .getGuildJsonApi()
                .getGuildManagers()
                .enqueue(new Callback<GeneralResponse<List<Employee>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Employee>>> call,
                                           Response<GeneralResponse<List<Employee>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                        }
                        List<Employee> managers = response.body().getData();
                        for (Employee manager : managers) {
                            guildManagerId.put(manager.getSite().getGuild().getId(),
                                    manager.getId());
                        }
                        areManagersReady = true;
                        if (areGuildsReady && areManagersReady) {
                            updateGuildsTable();
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Employee>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void getGuilds() {
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
                        guilds = response.body().getData();
                        areGuildsReady = true;
                        if (areGuildsReady && areManagersReady) {
                            updateGuildsTable();
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Guild>>> call, Throwable t) {
                        showError();
                    }
                });
    }

    private void updateGuildsTable() {
        TableLayout tableLayout = view.findViewById(R.id.guilds_table);
        addColumnNames(tableLayout);
        for (Guild guild : guilds) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(getTextViewForTable(valueOf(guild.getId())));
            tableRow.addView(getTextViewForTable(valueOf(guild.getCompany().getId())));
            tableRow.addView(getTextViewForTable(getManagerName(guild)));
            tableRow.addView(getTextViewForTable(guild.getGuildName()));
            tableLayout.addView(tableRow);
        }
    }

    private String getManagerName(Guild guild) {
        if (guildManagerId.containsKey(guild.getId())) {
            return guildManagerId.get(guild.getId()).toString();
        }
        return NULL_VALUE;
    }
}
