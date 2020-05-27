package com.nsu.db.aircraft.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nsu.db.aircraft.R;

public class FragmentWithFragmentActivity extends Fragment {
    protected FragmentActivity fragmentActivity;

    public FragmentWithFragmentActivity() {

    }

    protected void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    protected void setStartFragmentButton(View view, int buttonId, Fragment fragment) {
        Button startFragmentButton = view.findViewById(buttonId);
        startFragmentButton.setOnClickListener(v -> startFragment(fragment));
    }
}
