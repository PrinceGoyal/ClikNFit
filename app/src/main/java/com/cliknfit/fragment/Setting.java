package com.cliknfit.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cliknfit.R;
import com.cliknfit.activity.AboutChangeContact;
import com.cliknfit.activity.MyAccount;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Setting extends Fragment {

    private View view;
    private LinearLayout llAboutUs, llAccount, llChange_password, llContactUs;
    private RelativeLayout llNotification;
    private SwitchCompat switchNotificatuon;
    private View viewchangePassword;

    public Setting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);


        initViews();
        return view;
    }

    private void initViews() {
        llAboutUs = (LinearLayout) view.findViewById(R.id.ll_about);
        llAccount = (LinearLayout) view.findViewById(R.id.ll_account);
        llChange_password = (LinearLayout) view.findViewById(R.id.ll_change_password);
        viewchangePassword = (View) view.findViewById(R.id.viewchangePassword);
        llNotification = (RelativeLayout) view.findViewById(R.id.ll_notification);
        llContactUs = (LinearLayout) view.findViewById(R.id.ll_contactUs);
        switchNotificatuon = (SwitchCompat) view.findViewById(R.id.notification_switch);

        if(AppPreference.getPreference(getActivity(), Constants.LOGINTYPE).equals("phone")){
            llChange_password.setVisibility(View.VISIBLE);
            viewchangePassword.setVisibility(View.VISIBLE);
        }
        click();
    }

    private void click() {
        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutChangeContact.class)
                        .putExtra("title","ABOUT US")
                        .putExtra("pageno",1)
                );
            }
        });
        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyAccount.class)
                );
            }
        });
        llChange_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutChangeContact.class)
                        .putExtra("title","CHANGE PASSWORD")
                        .putExtra("pageno",3)
                );

            }
        });

        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AboutChangeContact.class)
                        .putExtra("title","CONTACT US")
                        .putExtra("pageno",5)
                );
            }
        });


        if (AppPreference.getPreference(getActivity(), Constants.MUTE).equals("1")) {
            switchNotificatuon.setChecked(true);
        }else
            switchNotificatuon.setChecked(false);

        switchNotificatuon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppPreference.setPreference(getActivity(), Constants.MUTE,"1");
                } else {
                    AppPreference.setPreference(getActivity(), Constants.MUTE,"");
                }
            }
        });


    }


}
