package com.nsu.db.aircraft.view.company.guild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.model.company.Guild;

import static android.view.View.INVISIBLE;


public class GuildDetailFragment extends Fragment {
    private Guild guild;
    private boolean isAddFragment = true;

    public GuildDetailFragment() {
        // Required empty public constructor
    }

    GuildDetailFragment(Guild guild) {
        this.guild = guild;
        this.isAddFragment = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guild_detail, container, false);
        if (isAddFragment) {
            setVisibilityForAdd(view);
        } else {
            setVisibilityForDetailChange(view);
        }
        return view;
    }

    private void setVisibilityForDetailChange(View view) {
        view.findViewById(R.id.button_add_guild).setVisibility(INVISIBLE);
    }

    private void setVisibilityForAdd(View view) {
        view.findViewById(R.id.button_change_guild_name).setVisibility(INVISIBLE);
        view.findViewById(R.id.button_change_guild_company).setVisibility(INVISIBLE);
        view.findViewById(R.id.button_change_guild_manager).setVisibility(INVISIBLE);
        view.findViewById(R.id.button_delete_detail_guild).setVisibility(INVISIBLE);
    }
}
