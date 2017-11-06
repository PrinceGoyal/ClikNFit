package com.cliknfit.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliknfit.R;
import com.cliknfit.activity.CommentsSession;
import com.cliknfit.adapter.AdapterMySession;
import com.cliknfit.adapter.AdapterWalletList;
import com.cliknfit.database.DataBaseHelper;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CardDBModel;

import java.util.ArrayList;

import static com.cliknfit.R.id.mRecycler;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWallet extends Fragment implements Adapterinterface, ApiResponse {


    private View view;
    private RecyclerView mRecycler;
    private ArrayList<CardDBModel> dataList;
    private AdapterWalletList adapter;

    public MyWallet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_wallet, container, false);
        initviews();
        return view;
    }

    private void initviews() {
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        dataList = new ArrayList<>();
        adapter = new AdapterWalletList(this, getActivity(), dataList);
        mRecycler.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        DataBaseHelper database = new DataBaseHelper(getActivity());
        ArrayList<CardDBModel> locallist = database.getAllCards();
        dataList.clear();
        for(CardDBModel model:locallist){
            dataList.add(model);
        }
        adapter.notifyDataSetChanged();

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