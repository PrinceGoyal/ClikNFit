package com.cliknfit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterEvents;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.ModelEvents;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Events extends Fragment implements Adapterinterface, ApiResponse {

    private View view;
    private RecyclerView mRecycler;
    private ArrayList<ModelEvents> dataList;
    private AdapterEvents adapter;

    public Events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_events, container, false);
        initviews();
        return view;
    }

    private void initviews() {
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataList = new ArrayList<>();
        adapter = new AdapterEvents(this, getActivity(), dataList);
        mRecycler.setAdapter(adapter);

    }

    @Override
    public void getResponse(Object response, String service) {

    }

    @Override
    public void sendcallback(int position, String string) {

    }

    @Override
    public void sendcallbackforpreference(int position, String string, String date, String time) {

    }
}
