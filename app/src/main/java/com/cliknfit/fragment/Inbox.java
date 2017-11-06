package com.cliknfit.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliknfit.R;
import com.cliknfit.activity.ChatDetail;
import com.cliknfit.adapter.AdapterInbox;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;

import java.util.ArrayList;

public class Inbox extends Fragment implements ApiResponse, Adapterinterface {

    private View view;
    private RecyclerView mRecycler;
    private ArrayList<String> dataList;
    private AdapterInbox adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inbox, container, false);

        initviews();

        return view;
    }

    private void initviews() {
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataList = new ArrayList<>();
        adapter = new AdapterInbox(this, getActivity(), dataList);
        mRecycler.setAdapter(adapter);

    }

    @Override
    public void getResponse(Object response, String service) {

    }

    @Override
    public void sendcallback(int position, String string) {
        startActivity(new Intent(getActivity(), ChatDetail.class));
    }

    @Override
    public void sendcallbackforpreference(int position, String string, String date, String time) {

    }

}
