package com.cliknfit.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.activity.CommentsSession;
import com.cliknfit.adapter.AdapterBookingHistory;
import com.cliknfit.adapter.AdapterMySession;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.ModelBookingHistory;
import com.cliknfit.pojo.ModelBookingResult;
import com.cliknfit.pojo.ModelMySession;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingHistory extends Fragment implements Adapterinterface, ApiResponse {

    private TextView norecord;

    public BookingHistory() {
        // Required empty public constructor
    }
    private SwipeRefreshLayout swiperefresh;
    private BroadcastReceiver broadcastReceiver;


    private View view;
    private RecyclerView mRecycler;
    private ArrayList<ModelBookingResult> dataList;
    private AdapterBookingHistory adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_booking_history, container, false);
        initviews();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        registerFcmReciever();
        boolean update = AppPreference.getBooleanPreference(getActivity(), Constants.PROFILEUPDATED);
        if (update) {
            addList();
            AppPreference.setBooleanPreference(getActivity(), Constants.PROFILEUPDATED, false);
        }
    }

    private void registerFcmReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cliknfit");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                addList();
            }
        };
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initviews() {
        norecord = (TextView) view.findViewById(R.id.norecord);
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataList = new ArrayList<>();
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        adapter = new AdapterBookingHistory(this, getActivity(), dataList);
        mRecycler.setAdapter(adapter);

        addList();
        swipe();

    }


    private void swipe() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addList();
            }
        });
    }


    private void addList() {
        CommonAsyncTask ca = new CommonAsyncTask(getActivity(),this);

        ca.getBookingHistorytask(AppPreference.getPreference(getActivity(),Constants.USERID), "sessionList");
    }

    @Override
    public void getResponse(Object response, String service) {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
            if (dataList.size() > 0)
                dataList.clear();
        }

        if (dataList.size() > 0)
            dataList.clear();

        ModelBookingHistory parent = (ModelBookingHistory) response;
        if (!parent.isError()) {
            for (ModelBookingResult model : parent.getData().getResults()) {
                dataList.add(model);
            }
            adapter.notifyDataSetChanged();
            if(dataList.size()>0)
                norecord.setVisibility(View.GONE);
            else
                norecord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sendcallback(int position, String string) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", dataList.get(position));
        startActivity(new Intent(getActivity(), CommentsSession.class)
                .putExtras(bundle)
        );
    }

    @Override
    public void sendcallbackforpreference(int position, String string, String date, String time) {

    }
}