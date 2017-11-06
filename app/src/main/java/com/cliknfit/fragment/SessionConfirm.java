package com.cliknfit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliknfit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionConfirm extends Fragment {

    public SessionConfirm() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_confirm, container, false);
    }

}
