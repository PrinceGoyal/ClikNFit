package com.cliknfit.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Validations;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import static com.cliknfit.R.id.no_of_people;
import static com.cliknfit.R.id.otp1;
import static com.cliknfit.R.id.otp2;
import static com.cliknfit.R.id.otp3;
import static com.cliknfit.R.id.otp4;
import static com.cliknfit.R.id.tv_hour;
import static com.cliknfit.R.id.two;

public class EndSession extends AppCompatActivity implements ApiResponse {

    private RatingBar rb;
    private ModelMySessionResults model;
    private EditText txt_selecthour;
    private EditText tv_notes, tv_review;
    private TextView btn_requestmosthour;
    private LinearLayout ll_addmorehour;
    private int rategiven = 0;
    private double addhour = 0;
    private ImageView onestar, twostar, threestar, fourstar, fivestar;
    private Context context;
    private TextView btn_complete;
    private TextView tv_price, tv_name, tv_hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_session_end);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        context = this;
        if (getIntent().hasExtra("model"))
            model = (ModelMySessionResults) getIntent().getSerializableExtra("model");
        if(model.getTrainer()!=null) {
            if (model.getHas_additional_hours() == 1)
                model = model.getAdditional_hours();
        }

        onestar = (ImageView) findViewById(R.id.onestar);
        twostar = (ImageView) findViewById(R.id.twostar);
        threestar = (ImageView) findViewById(R.id.threestar);
        fourstar = (ImageView) findViewById(R.id.fourstar);
        fivestar = (ImageView) findViewById(R.id.fivestar);

        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_name = (TextView) findViewById(R.id.tv_name);


        if(model.getTrainer()!=null) {
            tv_name.setText("" + model.getTrainer().getName());
            tv_price.setText("$" + model.getTotal_price());
            tv_hour.setText("" + model.getHour());
        }else {
            tv_name.setText("" + model.getTrainer_name());
            tv_price.setText("$" + model.getReq_client_total_price());
            tv_hour.setText("" + model.getReq_total_hours());
        }

        ll_addmorehour = (LinearLayout) findViewById(R.id.ll_addmorehour);
        txt_selecthour = (EditText) findViewById(R.id.selecthour);
        btn_complete = (TextView) findViewById(R.id.complete);
        tv_review = (EditText) findViewById(R.id.tv_review);
        tv_notes = (EditText) findViewById(R.id.tv_notes);
        btn_requestmosthour = (TextView) findViewById(R.id.requestmosthour);
        if (model.getHour() == 2) {
            ll_addmorehour.setVisibility(View.GONE);
        } else
            txt_selecthour.setText("0.5 Hour");

        if(getIntent().hasExtra("emergency")){
            ll_addmorehour.setVisibility(View.GONE);
        }

        if(model.getIs_emergency_stop()==1){
            ll_addmorehour.setVisibility(View.GONE);
        }


        click();
    }

    private boolean isValid() {
      if (rategiven == 0) {
            Alerts.okAlert(EndSession.this, "First rate then submit.", "Please rate.");
            return false;
        } else {
            return true;
        }
    }


    private void submitRatingMethod() {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.submitRatetask(tv_review.getText().toString(),
                tv_notes.getText().toString(),
                "" + rategiven,
                "" + model.getId(),
                "ratesubmit"
        );
    }

    private void click() {
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid())
                    submitRatingMethod();
            }
        });


        txt_selecthour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHourDialog();
            }
        });

        onestar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                rategiven = 1;
                return false;
            }
        });
        twostar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                rategiven = 2;
                return false;
            }
        });

        threestar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                rategiven = 3;
                return false;
            }
        });
        fourstar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_star));
                rategiven = 4;

                return false;
            }
        });
        fivestar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.white_fill_star));
                rategiven = 5;
                return false;
            }
        });

        btn_requestmosthour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(Validations.isFieldNotEmpty(txt_selecthour))
                requestMoreHourMethod();
            }
        });
    }

    private void requestMoreHourMethod() {
        Calendar cCur = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentdate = df.format(cCur.getTime());

        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.requestMoreHourTask(currentdate,"" + model.getId(), "" + addhour, "addhour");
    }

    private void selectHourDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(EndSession.this);
        View dialog = LayoutInflater.from(EndSession.this).inflate(R.layout.alertno_of_people, null, false);
        final TextView one = (TextView) dialog.findViewById(R.id.one);
        final TextView two = (TextView) dialog.findViewById(R.id.two);
        final TextView three = (TextView) dialog.findViewById(R.id.three);
        final TextView four = (TextView) dialog.findViewById(R.id.four);
        final TextView five = (TextView) dialog.findViewById(R.id.five);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_done = (TextView) dialog.findViewById(R.id.tv_done);


        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();
        three.setVisibility(View.GONE);
        four.setVisibility(View.GONE);
        five.setVisibility(View.GONE);

        if (model.getHour() == 1) {
            one.setText("0.5 Hour");
            two.setText("1 Hour");
        } else if (model.getHour() == 1.5) {
            one.setText("0.5 Hour");
            two.setVisibility(View.GONE);

        }
        txt_selecthour.setText("0.5 Hour");
        addhour = 0.5;

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setTextColor(getResources().getColor(R.color.WHITE));
                two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                txt_selecthour.setText("0.5 Hour");
                addhour = 0.5;
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                two.setTextColor(getResources().getColor(R.color.WHITE));
                one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                txt_selecthour.setText("1 Hour");
                addhour = 1;
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_selecthour.setText("");
                alertDialog2.dismiss();
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();

    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parent = (CommonStatusResultObj) response;
        if (parent.isError())
            Alerts.okAlert(EndSession.this, parent.getMessage(), "Error");
        else {
            successAlert(EndSession.this, parent.getMessage(), "Success");

        }
    }


    public void successAlert(final Context activity, String message, String title) {
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
                startActivity(new Intent(context, Dashboard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        alertDialog2.show();
    }

    /*private void sendPaymentNonceToServer(String paymentNonce) {
        RequestParams params = new RequestParams("NONCE", paymentNonce);
        AsyncHttpClient androidClient = new AsyncHttpClient();
        androidClient.post("https://your-server/client_token", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "Error: Failed to create a transaction");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.d(TAG, "Output " + responseString);
            }
        });
    }*/


}
