package com.nsu.db.aircraft.view.accounting.accounting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nsu.db.aircraft.R;
import com.nsu.db.aircraft.api.GeneralResponse;
import com.nsu.db.aircraft.api.model.accounting.Accounting;
import com.nsu.db.aircraft.network.NetworkService;
import com.nsu.db.aircraft.view.FragmentWithFragmentActivity;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountingAllFragment extends FragmentWithFragmentActivity {
    private View view;
    private List<Accounting> accountingList;

    public AccountingAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accounting_all, container, false);
        setAccountingList();
        sendAccountingRequest();
        return view;
    }

    private void setAccountingList() {
        ListView accountingListView = view.findViewById(R.id.list_view);
        accountingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startFragment(new AccountingDetailFragment(accountingList.get(position)));
            }
        });
    }

    private void updateAccountingList() {
        ListView accountingListView = view.findViewById(R.id.list_view);
        List<String> accountingViews = accountingList.stream()
                .map(Accounting::toString).collect(Collectors.toList());
        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, accountingViews);
        accountingListView.setAdapter(namesAdapter);
    }

    private void sendAccountingRequest() {
        NetworkService.getInstance()
                .getAccountingApi()
                .getAll()
                .enqueue(new Callback<GeneralResponse<List<Accounting>>>() {
                    @Override
                    public void onResponse(Call<GeneralResponse<List<Accounting>>> call,
                                           Response<GeneralResponse<List<Accounting>>> response) {
                        if (!response.isSuccessful()) {
                            showError();
                            return;
                        }
                        accountingList = response.body().getData();
                        updateAccountingList();
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse<List<Accounting>>> call, Throwable t) {
                        showError();
                    }
                });
    }
}
