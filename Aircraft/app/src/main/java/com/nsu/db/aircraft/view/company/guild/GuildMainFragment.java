package com.nsu.db.aircraft.view.company.guild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

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
        setStartFragmentButton(view, R.id.button_guild_show_all, new AllGuildsFragment());
        setStartFragmentButton(view, R.id.button_add_guild, new GuildDetailFragment());
        return view;
    }

    private void setStartFragmentButton(View view, int buttonId, Fragment fragment) {
        Button button = view.findViewById(buttonId);
        button.setOnClickListener(v -> startFragment(fragment));
    }

}
