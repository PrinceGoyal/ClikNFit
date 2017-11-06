package com.cliknfit.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cliknfit.R;

/**
 * Created by Prince on 3/30/2016.
 */
public class Alerts {


    public static void okAlert(final Context activity, String message, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        View dialog = LayoutInflater.from(activity).inflate(R.layout.ok_alert_layout, null, false);
        TextView tvtitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        tvtitle.setText(title);
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
        alertDialog2.show();
    }

    public static void okCancelAlert(final Activity activity, String message, String title, final boolean finish) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        View dialog = LayoutInflater.from(activity).inflate(R.layout.ok_alert_layout, null, false);
        TextView tvtitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        View canceldevider = dialog.findViewById(R.id.canceldevider);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        canceldevider.setVisibility(View.VISIBLE);
        tv_cancel.setVisibility(View.VISIBLE);

        tvtitle.setText(title);
        tvMessage.setText(message);
        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finish){
                    activity.finish();
                }
                alertDialog2.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();
    }




    public static void okAlert(final Activity activity, String message, String title, final boolean finish) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        View dialog = LayoutInflater.from(activity).inflate(R.layout.ok_alert_layout, null, false);
        TextView tvtitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);

        tvtitle.setText(title);
        tvMessage.setText(message);
        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finish){
                   activity.finish();
                }
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();
    }




}
