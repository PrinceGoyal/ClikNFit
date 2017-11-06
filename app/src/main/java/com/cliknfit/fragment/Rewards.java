package com.cliknfit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterReward;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.ModelReward;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Rewards extends Fragment implements ApiResponse,Adapterinterface{

    private View view;

    public Rewards() {
        // Required empty public constructor
    }
    private RecyclerView mRecycler;
    private ArrayList<ModelReward> dataList;
    private AdapterReward adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_rewards, container, false);
        initviews();
        return view;
    }

    private void initviews() {
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        dataList = new ArrayList<>();
        adapter = new AdapterReward(this, getActivity(), dataList);
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
