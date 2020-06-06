package com.nsu.db.aircraft.view.company.guild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

public class GuildMainFragment extends FragmentWithFragmentActivity {

    public GuildMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guild_main, container, false);
        setStartFragmentButton(view, R.id.button_brigade_show_all, new AllGuildsFragment());
        setStartFragmentButton(view, R.id.button_add_brigade, new GuildDetailFragment());
        setStartFragmentButton(view, R.id.button_show_brigades_table, new GuildTableFragment());
        return view;
    }
}
