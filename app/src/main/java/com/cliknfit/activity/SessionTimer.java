package com.cliknfit.activity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.pojo.TimerScreenModel;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SessionTimer extends AppCompatActivity implements ApiResponse, View.OnClickListener {


    private TextView emergency_stop;
    private RelativeLayout center_circleLayout, warmUpLayout, breakLayout;
    private DonutProgress centerProgress,breakProgress,coolProgress,warmUpProgress;
    private TextView txt_timecenter;
    private boolean flagCenterTimer=false;
    private RelativeLayout coolLayout;
    private TextView txt_wakeUptime,txt_cooltimer,txt_breaktime;
    private boolean flagCoolTimer=false;
    private ModelMySessionResults model;
    private BroadcastReceiver broadcastReceiver;
    private boolean back = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_session_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void registerFcmReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cliknfit");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateOnPushTimerTask();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }
    private void updateOnPushTimerTask() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.updateOnPushtask("" + model.getId(),"update"
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (back) {
                AppPreference.setBooleanPreference(SessionTimer.this, Constants.PROFILEUPDATED, true);
                finish();
                super.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (back) {
            AppPreference.setBooleanPreference(SessionTimer.this, Constants.PROFILEUPDATED, true);
            finish();
            super.onBackPressed();
        }
    }

    private void initViews() {
              if (getIntent().hasExtra("model"))
            model = (ModelMySessionResults) getIntent().getSerializableExtra("model");
        if(model.getHas_additional_hours()==1)
            model=model.getAdditional_hours();

        emergency_stop = (TextView) findViewById(R.id.emergency_stop);
        emergency_stop.setOnClickListener(this);
        center_circleLayout = (RelativeLayout) findViewById(R.id.center_circle);
        center_circleLayout.setOnClickListener(this);
        warmUpLayout = (RelativeLayout) findViewById(R.id.warmUpLayout);
        warmUpLayout.setOnClickListener(this);
        breakLayout = (RelativeLayout) findViewById(R.id.breakLayout);
        breakLayout.setOnClickListener(this);
        coolLayout = (RelativeLayout) findViewById(R.id.coolLayout);
        coolLayout.setOnClickListener(this);


        breakProgress = (DonutProgress) findViewById(R.id.donut_progress_break);
        coolProgress = (DonutProgress) findViewById(R.id.donut_progress_cool);
        warmUpProgress = (DonutProgress) findViewById(R.id.donut_progress_warmUp);
        centerProgress = (DonutProgress) findViewById(R.id.donut_progresscenter);
        txt_timecenter = (TextView) findViewById(R.id.timecenter);
        txt_wakeUptime = (TextView) findViewById(R.id.wakeUptimeleft);
        txt_cooltimer = (TextView) findViewById(R.id.cooltimeright);
        txt_breaktime = (TextView) findViewById(R.id.breaktime);

        checkforcenterstart();
        checkforcoolstart();
        checkforbreakstart();
        checkforwarmupstart();
    }


    private void checkforwarmupstart() {
        if (model.getWarm_up_start_time().equals("") && model.getWarm_up_end_time().equals("")) {
        } else if (!model.getWarm_up_start_time().equals("")) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            try {
                Date current = new Date();
                Calendar mcurrentTime = Calendar.getInstance();
                int d = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                int y = mcurrentTime.get(Calendar.YEAR);
                int m = mcurrentTime.get(Calendar.MONTH) + 1;

                Date startd = sdf.parse(""+ model.getWarm_up_start_time());
                long c = current.getTime();
                long p = startd.getTime();

                int diff_in_ms = (int) (c - p);

                final int millisToGo = model.getWarm_up_min() * 1000 * 60;
                if(diff_in_ms<=millisToGo) {
                    startWarmupUpTimer(millisToGo, (millisToGo - diff_in_ms));
                }

            } catch (ParseException ex) {
                Log.e("", ex.toString());
            }
        }
    }

    private void startWarmupUpTimer(final int totaltime, int starttime) {

        new CountDownTimer(starttime, 1000) {

            @Override
            public void onTick(long millis) {
                int seconds = (int) (millis / 1000) % 60;
                int minutes = (int) ((millis / (1000 * 60)) % 60);
                int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                String text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                txt_wakeUptime.setText(text);
                txt_wakeUptime.setVisibility(View.VISIBLE);

                int progressvalue = (int) (millis * 100) / totaltime;
                warmUpProgress.setProgress(progressvalue);
            }

            @Override
            public void onFinish() {
                txt_wakeUptime.setText("Finish");
                txt_wakeUptime.setVisibility(View.GONE);
                warmUpProgress.setProgress(0);
            }
        }.start();
    }

    private void checkforbreakstart() {
        if (model.getBreak_start_time().equals("") && model.getBreak_end_time().equals("")) {
        } else if (!model.getBreak_start_time().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            try {
                Date current = new Date();
                Calendar mcurrentTime = Calendar.getInstance();
                int d = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                int y = mcurrentTime.get(Calendar.YEAR);
                int m = mcurrentTime.get(Calendar.MONTH) + 1;

                Date startd = sdf.parse(""+ model.getBreak_start_time());
                long c = current.getTime();
                long p = startd.getTime();

                int diff_in_ms = (int) (c - p);
                final int millisToGo = model.getBreak_min() * 1000 * 60;
                if(diff_in_ms<=millisToGo) {
                    startBreakTimer(millisToGo, (millisToGo - diff_in_ms));
                }

            } catch (ParseException ex) {
                Log.e("", ex.toString());
            }
        }
    }
    private void checkforcoolstart() {
        if (model.getCool_start_time().equals("") && model.getCool_end_time().equals("")) {
        } else if (!model.getCool_start_time().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            try {
                Date current = new Date();
                Calendar mcurrentTime = Calendar.getInstance();
                int d = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                int y = mcurrentTime.get(Calendar.YEAR);
                int m = mcurrentTime.get(Calendar.MONTH) + 1;

                Date startd = sdf.parse("" + model.getCool_start_time());
                long c = current.getTime();
                long p = startd.getTime();

                int diff_in_ms = (int) (c - p);
                final int millisToGo = model.getCool_min() * 1000 * 60;
                if(diff_in_ms<=millisToGo) {
                    startCoolTimer(millisToGo, (millisToGo - diff_in_ms));
                }

            } catch (ParseException ex) {
                Log.e("", ex.toString());
            }

        }
    }
    private void checkforcenterstart() {
        if (model.getActual_start_time().equals("") && model.getActual_end_time().equals("")) {
        } else if (!model.getActual_start_time().equals("")) {
            if (model.getActual_end_time().equals("")) {
                center_circleLayout.setEnabled(false);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                try {
                    Date current = new Date();
                    Calendar mcurrentTime = Calendar.getInstance();
                    int d = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                    int y = mcurrentTime.get(Calendar.YEAR);
                    int m = mcurrentTime.get(Calendar.MONTH) + 1;

                    Date startd = sdf.parse("" +model.getActual_start_time());
                    long c = current.getTime();
                    long p = startd.getTime();

                    int diff_in_ms = (int) (c - p);
                    final int millisToGo = (int) (model.getHour() * 1000 * 60 * 60);
                    startCenterTimer(millisToGo, (millisToGo - diff_in_ms));

                } catch (ParseException ex) {
                    Log.e("", ex.toString());
                }

            }
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emergency_stop:
                abortAlert("Are you sure you want to abort this session");
                break;
        }
    }

    private void startCoolTimer(final int totaltime, int starttime) {

        new CountDownTimer(starttime, 1000) {

            @Override
            public void onTick(long millis) {
                int seconds = (int) (millis / 1000) % 60;
                int minutes = (int) ((millis / (1000 * 60)) % 60);
                int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                String text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                txt_cooltimer.setText(text);
                int progressvalue = (int) (millis * 100) / totaltime;
                coolProgress.setProgress(progressvalue);
            }

            @Override
            public void onFinish() {
                txt_cooltimer.setText("Finish");
                coolProgress.setProgress(0);
            }
        }.start();
    }

    private void startBreakTimer(final int totaltime, int starttime) {

        new CountDownTimer(starttime, 1000) {

            @Override
            public void onTick(long millis) {
                int seconds = (int) (millis / 1000) % 60;
                int minutes = (int) ((millis / (1000 * 60)) % 60);
                int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                String text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                txt_breaktime.setText(text);
                int progressvalue = (int) (millis * 100) / totaltime;
                breakProgress.setProgress(progressvalue);
            }

            @Override
            public void onFinish() {
                txt_breaktime.setText("Finish");
                breakProgress.setProgress(0);
            }
        }.start();
    }


    private void startCenterTimer(final int totaltime, int starttime) {

        new CountDownTimer(starttime, 1000) {

            @Override
            public void onTick(long millis) {
                int seconds = (int) (millis / 1000) % 60;
                int minutes = (int) ((millis / (1000 * 60)) % 60);
                int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                String text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                txt_timecenter.setText(text);
                int progressvalue = (int) (millis * 100) / totaltime;
                centerProgress.setProgress(progressvalue);
                emergency_stop.setVisibility(View.VISIBLE);
                back=false;
            }

            @Override
            public void onFinish() {
                txt_timecenter.setText("Finish");
                centerProgress.setProgress(0);
                emergency_stop.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",model);
                startActivity(new Intent(SessionTimer.this,EndSession.class)
                        .putExtras(bundle)
                );

            }
        }.start();
    }

    private void abortAlert(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View dialog = LayoutInflater.from(SessionTimer.this).inflate(R.layout.ok_alert_layout, null, false);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",model);

                startActivity(new Intent(SessionTimer.this,EndSession.class)
                        .putExtra("emergency",1)
                        .putExtras(bundle));
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();
    }




    private void openLanedialog(final ArrayList<String> arrayList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Why the session has been aborted");
        builder.setCancelable(true);

        final String[] flowers = arrayList.toArray(new String[0]);
        builder.setSingleChoiceItems(
                flowers, // Items list
                -1, // Index of checked item (-1 = no selection)
                new DialogInterface.OnClickListener() // Item click listener
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the alert dialog selected item's text
                        // String selectedItem = Arrays.asList(flowers).get(i);

                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void getResponse(Object response, String service) {
        TimerScreenModel parent = (TimerScreenModel) response;
        if (!parent.isError()) {
            TimerScreenModel.TimerScreenData result = parent.getData();
            model=null;
            model = result.getResults();
            if(model.getIs_trainer_complete()==1){
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",model);
                startActivity(new Intent(SessionTimer.this,EndSession.class).putExtras(bundle));
            }else {
                checkforcenterstart();
                checkforcoolstart();
                checkforbreakstart();
                checkforwarmupstart();
            }
        } else {
            Alerts.okAlert(SessionTimer.this, parent.getMessage(), "");
        }
    }


}
