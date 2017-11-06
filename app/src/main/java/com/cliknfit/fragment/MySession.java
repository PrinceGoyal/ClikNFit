package com.cliknfit.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.activity.CommentsSession;
import com.cliknfit.activity.Dashboard;
import com.cliknfit.activity.EndSession;
import com.cliknfit.activity.SessionDetails;
import com.cliknfit.activity.SessionTimer;
import com.cliknfit.adapter.AdapterMySession;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.ModelMySession;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.add;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySession extends Fragment implements Adapterinterface, ApiResponse {


    private SwipeRefreshLayout swiperefresh;
    private BroadcastReceiver broadcastReceiver;
    private static final int PERMISSION_ALL = 176;
    private String mobileno;
    private static final String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    private TextView norecord;

    public MySession() {
        // Required empty public constructor
    }

    private View view;
    private RecyclerView mRecycler;
    private ArrayList<ModelMySessionResults> dataList;
    private AdapterMySession adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_session, container, false);
        initviews();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        registerFcmReciever();
        int update = AppPreference.getIntegerPreference(getActivity(), Constants.PROFILEUPDATED);
        if (update == 1) {
            addList();
            AppPreference.setIntegerPreference(getActivity(), Constants.PROFILEUPDATED, 0);
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

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void initviews() {
        norecord = (TextView) view.findViewById(R.id.norecord);
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataList = new ArrayList<>();
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        adapter = new AdapterMySession(this, getActivity(), dataList);
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
        CommonAsyncTask ca = new CommonAsyncTask(getActivity(), this);

        ca.getSessiontask(AppPreference.getPreference(getActivity(), Constants.USERID), "sessionList");
    }

    @Override
    public void getResponse(Object response, String service) {
        if (service.equals("starttimer")) {
            CommonStatusResultObj parent = (CommonStatusResultObj) response;
            if (!parent.isError()) {
                addList();
                Alerts.okAlert(getActivity(), parent.getMessage(), "");
            } else
                Alerts.okAlert(getActivity(), parent.getMessage(), "");

        } else {
            if (swiperefresh.isRefreshing()) {
                swiperefresh.setRefreshing(false);
                if (dataList.size() > 0)
                    dataList.clear();
            }

            if (dataList.size() > 0)
                dataList.clear();

            ModelMySession parent = (ModelMySession) response;
            if (!parent.isError()) {
                for (ModelMySessionResults model : parent.getData().getResults()) {
                    dataList.add(model);
                }
                adapter.notifyDataSetChanged();
                if (dataList.size() > 0)
                    norecord.setVisibility(View.GONE);
                else
                    norecord.setVisibility(View.VISIBLE);

                if (!parent.isError() && dataList.size() > 0) {
                    ModelMySessionResults model;
                    if (parent.getData().getResults().get(0).getHas_additional_hours() == 1)
                        model = parent.getData().getResults().get(0).getAdditional_hours();
                    else
                        model = parent.getData().getResults().get(0);

                    if (model.getIs_emergency_stop() == 1 && model.getIs_client_complete() == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", dataList.get(0));
                        startActivity(new Intent(getActivity(), EndSession.class)
                                .putExtras(bundle)
                        );
                    } else if (model.getStatus() == 3 && model.getIs_trainer_started() == 1 && model.getIs_client_started() == 1 &&
                            model.getIs_trainer_complete() == 1 && model.getIs_client_complete() == 0
                            ) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", dataList.get(0));
                        startActivity(new Intent(getActivity(), EndSession.class)
                                .putExtras(bundle)
                        );
                    } else if (model.getStatus() == 3 && model.getIs_trainer_started() == 1 && model.getIs_client_started() == 1 &&
                            model.getIs_trainer_complete() == 0 && model.getIs_client_complete() == 0
                            ) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", dataList.get(0));
                        startActivity(new Intent(getActivity(), SessionTimer.class)
                                .putExtras(bundle)
                        );
                    }
                }


            } else
                norecord.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void sendcallback(int position, String string) {

        callOnPhone(string);
    }

    public void callOnPhone(String phone) {
        mobileno = phone;
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobileno));
                startActivity(callIntent);
                break;
        }
    }

    @Override
    public void sendcallbackforpreference(int position, String id, String date, String time) {
        if(time.equals("cancel")){
            CommonAsyncTask ca = new CommonAsyncTask(getActivity(), this);
            abortAlert("Are you sure want to cancel the session?",id,ca);
        }else {
            CommonAsyncTask ca = new CommonAsyncTask(getActivity(), this);

            ca.startTimerTask("" + id, "starttimer"
            );
        }
    }


    private void abortAlert(String message,final String id,final CommonAsyncTask ca) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.ok_alert_layout, null, false);
        TextView cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        View canceldevider = (View) dialog.findViewById(R.id.canceldevider);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);

        tvOk.setText("NO");
        cancel.setText("YES");
        cancel.setVisibility(View.VISIBLE);
        canceldevider.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog2.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.cancelTask("" + id, "starttimer");
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();
    }

}