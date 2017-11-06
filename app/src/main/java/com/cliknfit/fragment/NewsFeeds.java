package com.cliknfit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterNewsFeed;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.DataModel;
import com.cliknfit.pojo.QNAModel;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeeds extends Fragment implements ApiResponse, Adapterinterface {

    private View view;
    private RecyclerView mRecycler;
    private ArrayList<QNAModel> dataList;
    private AdapterNewsFeed adapter;
    private TextView ad_title;
    private ImageView ad_image;

    public NewsFeeds() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news_feeds, container, false);
        initviews();
        return view;
    }

    private void initviews() {
        ad_image = (ImageView) view.findViewById(R.id.ad_image);
        ad_title = (TextView) view.findViewById(R.id.ad_title);

        mRecycler = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        dataList = new ArrayList<>();
        adapter = new AdapterNewsFeed(getActivity(), dataList);
        mRecycler.setAdapter(adapter);

        addList();
    }

    private void addList() {
        CommonAsyncTask ca = new CommonAsyncTask(getActivity(), this);

        ca.newsFeedtask("newsfeed");
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatus parentModel = (CommonStatus) response;
        if (!parentModel.isError()) {
            if (service.equals("newsfeed")) {
                DataModel dataModel = parentModel.getData();
                dataList.clear();
                for (QNAModel model : dataModel.getQna()) {
                    if (model.getIs_banner() == 1) {
                        ad_title.setText(model.getTitle());
                        String imageid = Constants.PICBASE_URL + "" + model.getImage();
                        Picasso.with(getActivity())
                                .load(imageid)
                                .placeholder(R.drawable.user_image_temp)
                                .error(R.drawable.user_image_temp)
                                .into(ad_image);
                    } else
                        dataList.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            Alerts.okAlert(getActivity(), parentModel.getmessage(), "");
        }

    }

    @Override
    public void sendcallback(int position, String string) {

    }

    @Override
    public void sendcallbackforpreference(int position, String string, String date, String time) {

    }

}
