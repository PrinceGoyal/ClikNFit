package com.cliknfit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterTrainer;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopTrainner extends Fragment implements ApiResponse,Adapterinterface {

    private View view;
    private RecyclerView mRecycler;
    private ArrayList<String> dataList;
    private AdapterTrainer adapter;
    public TopTrainner() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_top_trainner, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        dataList = new ArrayList<>();
        adapter = new AdapterTrainer(this, getActivity(), dataList);
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
