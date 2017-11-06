package com.cliknfit.activity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.cliknfit.R;
import com.cliknfit.adapter.AdapterTimeList;
import com.cliknfit.database.DataBaseHelper;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CardDBModel;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cliknfit.util.Validations;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import static android.R.id.list;
import static com.cliknfit.R.id.amdivider;
import static com.cliknfit.R.id.hours;
import static com.cliknfit.R.id.thirty;
import static com.cliknfit.R.id.zero;


public class BookingInfo extends AppCompatActivity implements ApiResponse, PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener {

    private ImageView img_profile_pic;
    private EditText from, to, no_of_people;
    private RadioGroup locationradiogroup;
    private TextView save;
    private String client_id, trainer_id;
    private TextView trainer_name;
    private RadioButton trainer_gym;
    private RadioButton homeradio;
    private TextView trainer_gymaddress;
    private int ADDRESSCALLBACK = 7657;
    private String selectedfrommin, selectedfromformat;
    private String selectedtomin, selectedtoformat, selectedfromdate, selectedtodate;
    private int selectedfromhour, selectedtohour;
    private BraintreeFragment mBraintreeFragment;
    public String clientToken;
    private CardDBModel cardModel;
    private String nonce = "";
    private TextView selectmap;
    private int LOCATION_CALLBACK = 678;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        AppPreference.setBooleanPreference(this, Constants.PROFILEUPDATED, false);
        initViews();
    }


    public void onBraintreeSubmit() {
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, clientToken);
        } catch (InvalidArgumentException e) {
        }
        CardBuilder cardBuilder = new CardBuilder()
                .cardNumber(cardModel.getCardnumber().replaceAll("-", ""))
                .cvv(cardModel.getCvv())
                .expirationDate(cardModel.getExpdate());
        Card.tokenize(mBraintreeFragment, cardBuilder);
        mBraintreeFragment.addListener(this);
    }

    private void getClientTokenFromServer() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait ...");
        pd.setCancelable(false);
        pd.show();
        AsyncHttpClient androidClient = new AsyncHttpClient();
        androidClient.get("http://13.56.144.221/public/get_client_token", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  String responseString, Throwable throwable) {


            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String clientToken) {
                BookingInfo.this.clientToken = clientToken;
                onBraintreeSubmit();
                pd.dismiss();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean update = AppPreference.getBooleanPreference(this, Constants.PROFILEUPDATED);
        if (!update) {
            AppPreference.setBooleanPreference(this, Constants.PROFILEUPDATED, true);
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

            if (dataBaseHelper.getAllCards().size() > 0) {
                cardModel = dataBaseHelper.getAllCards().get(0);
                getClientTokenFromServer();
            } else {
                startActivity(new Intent(BookingInfo.this, PaymentMethods.class)
                        .putExtra("back", "0")
                );
            }
        }

    }

    private void initViews() {
        trainer_id = "" + getIntent().getIntExtra("trainer_id", 0);

        client_id = AppPreference.getPreference(this, Constants.USERID);
        img_profile_pic = (ImageView) findViewById(R.id.img_profile_pic);
        trainer_gymaddress = (TextView) findViewById(R.id.Trainer_gymaddress);
        // trainer_gymaddress.setText(getIntent().getStringExtra("gymaddress"));

        trainer_gymaddress.setText(AppPreference.getPreference(BookingInfo.this, Constants.CurrentAddress));

        trainer_gym = (RadioButton) findViewById(R.id.gym);
        homeradio = (RadioButton) findViewById(R.id.home);

        Picasso.with(this)
                .load(Constants.PICBASE_URL + "" + getIntent().getStringExtra("image"))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img_profile_pic);

        from = (EditText) findViewById(R.id.from);
        trainer_name = (TextView) findViewById(R.id.trainer_name);
        trainer_name.setText(getIntent().getStringExtra("name"));

        to = (EditText) findViewById(R.id.to);
        to.setEnabled(false);
        no_of_people = (EditText) findViewById(R.id.no_of_people);
        locationradiogroup = (RadioGroup) findViewById(R.id.locationradiogroup);
        save = (TextView) findViewById(R.id.save);
        selectmap = (TextView) findViewById(R.id.selectmap);

        click();
    }

    private void click() {
        selectmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookingInfo.this, SelectAddress.class);
                startActivityForResult(i, LOCATION_CALLBACK);
            }
        });


        no_of_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPeopleDialog();
            }
        });

        homeradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyAddress.class);
                startActivityForResult(intent, ADDRESSCALLBACK);
            }
        });

        trainer_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainer_gymaddress.setText(getIntent().getStringExtra("gymaddress"));
                trainer_gymaddress.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    requestBooking();
                }
            }
        });
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromTimeDialog(from);
            }
        });


        to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectToTimeDialog(to);
            }
        });
    }

    private void selectToTimeDialog(final EditText txt) {
        AlertDialog.Builder alert = new AlertDialog.Builder(BookingInfo.this);
        View dialog = LayoutInflater.from(BookingInfo.this).inflate(R.layout.alert_select_time, null, false);
        final TextView am = (TextView) dialog.findViewById(R.id.am);
        final View amdivider = dialog.findViewById(R.id.amdivider);
        final TextView pm = (TextView) dialog.findViewById(R.id.pm);
        final TextView datetime = (TextView) dialog.findViewById(R.id.datetime);

        final TextView zero = (TextView) dialog.findViewById(R.id.zero);
        final TextView thirty = (TextView) dialog.findViewById(R.id.thirty);
        final ArrayList<String> items = new ArrayList<String>();

        Calendar cCur = Calendar.getInstance();

        final int currenthours = cCur.get(Calendar.HOUR);

        Calendar cNext = Calendar.getInstance();
        cNext.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        final String currentdate = df.format(cCur.getTime());
        final String tommorowdate = df.format(cNext.getTime());


        int showhours = selectedfromhour;

        if (selectedfromformat.equals("PM")) {
            selectedtoformat = "PM";
            am.setVisibility(View.GONE);
            pm.setVisibility(View.VISIBLE);
            if (selectedfromhour <= 10) {
                if (selectedfrommin.equals("30")) {
                    showhours = selectedfromhour + 1;
                    selectedtohour = showhours;
                    items.add("" + showhours);
                    if (selectedtohour == 11)
                        items.add("00");
                    else
                        items.add("" + (showhours + 1));
                } else {
                    selectedtohour = showhours + 1;
                    items.add("" + (showhours + 1));
                    if (selectedtohour == 11)
                        items.add("00");
                    else
                        items.add("" + (showhours + 2));
                }

            } else {
                if (showhours + 1 == 12) {
                    am.setVisibility(View.VISIBLE);
                    pm.setVisibility(View.GONE);
                    am.setTextColor(getResources().getColor(R.color.WHITE));
                    selectedtoformat = "AM";
                    showhours = 00;
                    selectedtohour = showhours;
                    items.add("0" + showhours);
                } else {
                    showhours = 00;
                    selectedtohour = 01;
                    items.add("01");
                }
                if (selectedtohour == 0) {
                    showhours = 0 + 1;
                    items.add("01");
                } else if (selectedtohour + 1 == 12) {
                    showhours = 00;
                    items.add("12");
                } else {
                    if(showhours == 00)
                    items.add("0" + (showhours + 2));
                    else
                    items.add("" + (showhours + 2));

                }
            }
            if (selectedfromdate.equals(currentdate)) {
                if (selectedtoformat.equals("PM"))
                    selectedtodate = currentdate;
                else
                    selectedtodate = tommorowdate;

            } else {

            }
        } else {
            selectedtoformat = "AM";
            am.setVisibility(View.VISIBLE);
            pm.setVisibility(View.GONE);
            if (selectedfromhour < 10) {
                selectedtohour = showhours + 1;
                if(selectedtohour==10)
                    items.add("" + (showhours + 1));
                    else
                items.add("0" + (showhours + 1));
                if (selectedtohour < 8)
                items.add("0" + (showhours + 2));
                else
                    items.add("" + (showhours + 2));
            } else {
                if (showhours == 12) {
                    selectedtoformat = "PM";
                    pm.setVisibility(View.VISIBLE);
                    am.setVisibility(View.GONE);
                    pm.setTextColor(getResources().getColor(R.color.WHITE));
                    showhours = 01;
                    selectedtohour = showhours;
                    items.add("0" + showhours);
                } else if(showhours>12){
                    selectedtohour =  01;
                    items.add("01");
                    selectedtoformat = "PM";
                    pm.setVisibility(View.VISIBLE);
                    am.setVisibility(View.GONE);
                    pm.setTextColor(getResources().getColor(R.color.WHITE));
                }else{
                    if (showhours==11){
                    selectedtohour =  00;
                    items.add("12");
                        selectedtoformat = "PM";
                        pm.setVisibility(View.VISIBLE);
                        am.setVisibility(View.GONE);
                        pm.setTextColor(getResources().getColor(R.color.WHITE));
                    }else{
                        selectedtohour = (showhours + 1);
                        items.add("" + (showhours + 1));
                    }
                }
                if (selectedtohour == 0) {
                    showhours = 0 + 1;
                    items.add("01");
                } else if (selectedtohour == 01) {
                    items.add("0" + (showhours + 1));
                } else if (selectedtohour + 1 == 12) {
                    showhours = 00;
                    items.add("12");
                } else
                    items.add("" + (showhours + 2));
            }
            if (selectedfromdate.equals(currentdate)) {
               /* if(selectedtohour ==0){
                    selectedtoformat = "PM";
                }else if (selectedtohour < 12)
                    selectedtoformat = "AM";
                else
                    selectedtoformat = "PM";*/

                selectedtodate = currentdate;
            } else {
                selectedtoformat = "AM";
                selectedtodate = tommorowdate;
            }
        }

        if (selectedtoformat.equals("PM")) {
            amdivider.setVisibility(View.GONE);
            am.setVisibility(View.GONE);
            selectedtoformat = "PM";
            pm.setTextColor(getResources().getColor(R.color.WHITE));
            pm.setVisibility(View.VISIBLE);
        } else {
            amdivider.setVisibility(View.GONE);
            pm.setVisibility(View.GONE);
            selectedtoformat = "AM";
            am.setTextColor(getResources().getColor(R.color.WHITE));
            am.setVisibility(View.VISIBLE);
        }


        selectedtomin = "00";

        if (selectedfrommin.equals("30")) {
            selectedtomin = "30";
            thirty.setTextColor(getResources().getColor(R.color.WHITE));
            zero.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
        }
        if (selectedtohour < 10)
            datetime.setText(selectedtodate + " 0" + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);
        else
            datetime.setText(selectedtodate + " " + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);

        final ListView list = (ListView) dialog.findViewById(R.id.timelist);

        final AdapterTimeList adapter = new AdapterTimeList(this, items, 0);
        list.setAdapter(adapter);

        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_done = (TextView) dialog.findViewById(R.id.tv_done);


        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.changeposition(i);

                selectedtohour = Integer.valueOf(items.get(i));
                if (selectedfrommin.equals("30") && selectedtohour == Integer.valueOf(items.get(0))) {
                    thirty.setTextColor(getResources().getColor(R.color.WHITE));
                    zero.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                    selectedtomin = "30";
                }
                if (selectedfrommin.equals("00") && selectedtohour == Integer.valueOf(items.get(items.size() - 1))) {
                    zero.setTextColor(getResources().getColor(R.color.WHITE));
                    thirty.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                    selectedtomin = "00";
                }
                if (selectedtohour == 0) {
                    selectedtodate = tommorowdate;
                } else {
                    if(selectedtohour==1 && selectedtoformat.equals("AM"))
                        selectedtodate = tommorowdate;
                    else
                    selectedtodate = currentdate;
                }
                if (selectedfromformat.equals("AM")) {
                    if (selectedfromdate.equals(currentdate)) {
                        if (selectedtohour < 12 && selectedtohour > 1)
                            selectedtoformat = "AM";
                        else
                            selectedtoformat = "PM";

                        selectedtodate = currentdate;
                    } else {
                        selectedtoformat = "AM";
                        selectedtodate = tommorowdate;
                    }
                } else {
                    if (selectedtodate.equals(tommorowdate)) {
                        selectedtoformat = "AM";

                    } else {

                        selectedtoformat = "PM";
                    }
                }

                if (selectedtoformat.equals("AM")) {
                    am.setVisibility(View.VISIBLE);
                    pm.setVisibility(View.GONE);
                    am.setTextColor(getResources().getColor(R.color.WHITE));
                    pm.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                }
                if (selectedtoformat.equals("PM")) {
                    pm.setVisibility(View.VISIBLE);
                    am.setVisibility(View.GONE);
                    pm.setTextColor(getResources().getColor(R.color.WHITE));
                    am.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                }

                if (selectedtohour < 10)
                    datetime.setText(selectedtodate + " 0" + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);
                else
                    datetime.setText(selectedtodate + " " + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);

            }
        });

        am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.setTextColor(getResources().getColor(R.color.WHITE));
                pm.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                selectedtoformat = "AM";
            }
        });
        pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pm.setTextColor(getResources().getColor(R.color.WHITE));
                am.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                selectedtoformat = "PM";
            }
        });
        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedfrommin.equals("00") && selectedtohour == Integer.valueOf(items.get(items.size() - 1))) {
                    zero.setTextColor(getResources().getColor(R.color.WHITE));
                    thirty.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                    selectedtomin = "00";
                } else {
                    thirty.setTextColor(getResources().getColor(R.color.WHITE));
                    zero.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                    selectedtomin = "30";
                }
                datetime.setText(selectedtodate + " " + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedfrommin.equals("30") && selectedtohour == Integer.valueOf(items.get(0))) {
                    thirty.setTextColor(getResources().getColor(R.color.WHITE));
                    zero.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                    selectedtomin = "30";
                } else {
                    zero.setTextColor(getResources().getColor(R.color.WHITE));
                    thirty.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                    selectedtomin = "00";
                }
                datetime.setText(selectedtodate + " " + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();

            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedtohour < 10)
                    txt.setText("0" + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);
                else
                    txt.setText("" + selectedtohour + ":" + selectedtomin + " " + selectedtoformat);
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();

    }


    private void selectFromTimeDialog(final EditText txt) {
        AlertDialog.Builder alert = new AlertDialog.Builder(BookingInfo.this);
        View dialog = LayoutInflater.from(BookingInfo.this).inflate(R.layout.alert_select_time, null, false);
        final TextView am = (TextView) dialog.findViewById(R.id.am);
        final TextView datetime = (TextView) dialog.findViewById(R.id.datetime);
        final View amdivider = dialog.findViewById(R.id.amdivider);
        final TextView pm = (TextView) dialog.findViewById(R.id.pm);
        final Calendar mcurrentTime = Calendar.getInstance();
        final int hours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        selectedfromdate = df.format(mcurrentTime.getTime());
        String currebntformat = "";

        final ArrayList<String> items = new ArrayList<String>();

        selectedfrommin = "00";
        if (hours == 12) {
            // hours = 0;
            selectedfromformat = "PM";
            currebntformat = selectedfromformat;
        } else if (hours > 12) {
            selectedfromformat = "PM";
            currebntformat = selectedfromformat;
        } else {
            selectedfromformat = "AM";
            currebntformat = selectedfromformat;

        }


        int currenthours = mcurrentTime.get(Calendar.HOUR) + 1;
        selectedfromhour = currenthours;

        datetime.setText(selectedfromdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);

        int currentPos = 0;
        for (int i = 1; i <= 12; i++) {
            if (i < 10)
                items.add("0" + i);
            else if (i == 12) {
                items.add("12");
            } else {
                items.add("" + i);
            }
            if (i == mcurrentTime.get(Calendar.HOUR)) {
                currentPos = i;
            }
        }


        final ListView list = (ListView) dialog.findViewById(R.id.timelist);

        final AdapterTimeList adapter = new AdapterTimeList(this, items, 0);
        list.setAdapter(adapter);


        list.setSelection(currentPos);
        adapter.changeposition(currentPos);
        if (selectedfromformat.equals("PM")) {
            am.setVisibility(View.GONE);
            pm.setTextColor(getResources().getColor(R.color.WHITE));
        }


        final TextView zero = (TextView) dialog.findViewById(R.id.zero);
        final TextView thirty = (TextView) dialog.findViewById(R.id.thirty);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_done = (TextView) dialog.findViewById(R.id.tv_done);


        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();

        final String finalCurrebntformat = currebntformat;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.changeposition(i);
                selectedfromhour = Integer.valueOf(items.get(i));
                if (selectedfromhour == 00) {
                    selectedfromhour = 12;
                }
                Calendar cCur = Calendar.getInstance();

                final int currenthours = cCur.get(Calendar.HOUR) + 1;

                Calendar cNext = Calendar.getInstance();
                cNext.add(Calendar.DAY_OF_YEAR, 1);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                String currentdate = df.format(cCur.getTime());
                String tommorowdate = df.format(cNext.getTime());

                if (finalCurrebntformat.equals("PM")) {
                    if (selectedfromhour < currenthours) {
                        am.setTextColor(getResources().getColor(R.color.WHITE));
                        pm.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                        selectedfromformat = "AM";
                        pm.setVisibility(View.GONE);
                        am.setVisibility(View.VISIBLE);
                        selectedfromdate = tommorowdate;
                        if (selectedfromhour == 12)
                            datetime.setText(tommorowdate + " 12" + ":" + selectedfrommin + " " + selectedfromformat);
                        else
                            datetime.setText(tommorowdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);

                    } else {
                        pm.setTextColor(getResources().getColor(R.color.WHITE));
                        am.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                        selectedfromformat = "PM";
                        am.setVisibility(View.GONE);
                        pm.setVisibility(View.VISIBLE);

                        if (selectedfromhour == 12) {
                            datetime.setText(tommorowdate + " 12" + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = tommorowdate;
                            pm.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                            am.setTextColor(getResources().getColor(R.color.WHITE));
                            selectedfromformat = "AM";
                            am.setVisibility(View.VISIBLE);
                            pm.setVisibility(View.GONE);

                        } else {
                            datetime.setText(currentdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = currentdate;
                        }
                    }

                }
                if (finalCurrebntformat.equals("AM")) {
                    if (selectedfromhour <= currenthours) {
                        if (selectedfromformat.equals("AM")) {
                            if (selectedfromhour == 12)
                                datetime.setText(tommorowdate + " 12" + ":" + selectedfrommin + " " + selectedfromformat);
                            else
                                datetime.setText(tommorowdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = tommorowdate;
                        } else {
                            if (selectedfromhour == 12)
                                datetime.setText(currentdate + " 12" + ":" + selectedfrommin + " " + selectedfromformat);
                            else
                                datetime.setText(currentdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = currentdate;
                        }
                    } else {
                        selectedfromdate = currentdate;
                        if (selectedfromhour == 12) {
                            selectedfromformat = "PM";
                            am.setVisibility(View.GONE);
                            pm.setVisibility(View.VISIBLE);
                            pm.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                            datetime.setText(currentdate + " 12" + ":" + selectedfrommin + " " + selectedfromformat);
                        }else
                            datetime.setText(currentdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                    }
                }
            }
        });


        am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cCur = Calendar.getInstance();
                final int currenthours = cCur.get(Calendar.HOUR);
                Calendar cNext = Calendar.getInstance();
                cNext.add(Calendar.DAY_OF_YEAR, 1);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                String currentdate = df.format(cCur.getTime());
                String tommorowdate = df.format(cNext.getTime());

                am.setTextColor(getResources().getColor(R.color.WHITE));
                pm.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                selectedfromformat = "AM";

                if (finalCurrebntformat.equals("AM")) {
                    if (selectedfromhour <= currenthours) {
                        if (selectedfromformat.equals("AM")) {
                            datetime.setText(tommorowdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = tommorowdate;
                        } else {
                            datetime.setText(currentdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = currentdate;
                        }
                    } else {
                        selectedfromdate = currentdate;
                        datetime.setText(currentdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                    }

                }

            }
        });
        pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cCur = Calendar.getInstance();
                final int currenthours = cCur.get(Calendar.HOUR);
                Calendar cNext = Calendar.getInstance();
                cNext.add(Calendar.DAY_OF_YEAR, 1);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                String currentdate = df.format(cCur.getTime());
                String tommorowdate = df.format(cNext.getTime());

                pm.setTextColor(getResources().getColor(R.color.WHITE));
                am.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                selectedfromformat = "PM";

                if (finalCurrebntformat.equals("AM")) {
                    if (selectedfromhour <= currenthours) {
                        if (selectedfromformat.equals("AM")) {
                            datetime.setText(tommorowdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = tommorowdate;
                        } else {
                            datetime.setText(currentdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                            selectedfromdate = currentdate;
                        }
                    } else {
                        selectedfromdate = currentdate;
                        datetime.setText(currentdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                    }
                }
            }
        });
        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirty.setTextColor(getResources().getColor(R.color.WHITE));
                zero.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                selectedfrommin = "30";
                datetime.setText(selectedfromdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zero.setTextColor(getResources().getColor(R.color.WHITE));
                thirty.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                selectedfrommin = "00";
                datetime.setText(selectedfromdate + " " + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedfromhour = 0;
                selectedfrommin = "";
                selectedfromformat = "";
                txt.setText("select");
                to.setText("select");
                alertDialog2.dismiss();

            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedfromhour == 12) {
                    txt.setText("12" + ":" + selectedfrommin + " " + selectedfromformat);
                } else if(selectedfromhour<10)
                    txt.setText("0" + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                else
                    txt.setText("" + selectedfromhour + ":" + selectedfrommin + " " + selectedfromformat);
                to.setText("select");
                to.setEnabled(true);
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();

    }


    private void selectPeopleDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(BookingInfo.this);
        View dialog = LayoutInflater.from(BookingInfo.this).inflate(R.layout.alertno_of_people, null, false);
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
        if (no_of_people.getText().toString().equals("1")) {
            one.setTextColor(getResources().getColor(R.color.WHITE));
            four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));

        } else if (no_of_people.getText().toString().equals("2")) {
            two.setTextColor(getResources().getColor(R.color.WHITE));
            four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));

        } else if (no_of_people.getText().toString().equals("3")) {
            three.setTextColor(getResources().getColor(R.color.WHITE));
            four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));

        } else if (no_of_people.getText().toString().equals("4")) {
            four.setTextColor(getResources().getColor(R.color.WHITE));
            three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));

        } else if (no_of_people.getText().toString().equals("5")) {
            five.setTextColor(getResources().getColor(R.color.WHITE));
            four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
            one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));

        }


        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setTextColor(getResources().getColor(R.color.WHITE));
                four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                no_of_people.setText("1");
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                two.setTextColor(getResources().getColor(R.color.WHITE));
                four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                no_of_people.setText("2");
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three.setTextColor(getResources().getColor(R.color.WHITE));
                four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                no_of_people.setText("3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                four.setTextColor(getResources().getColor(R.color.WHITE));
                five.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                no_of_people.setText("4");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                five.setTextColor(getResources().getColor(R.color.WHITE));
                four.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                three.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                two.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                one.setTextColor(getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));
                no_of_people.setText("5");
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_of_people.setText("");
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

    private void requestBooking() {
        if(selectedfromformat.equals("PM")){
            if(selectedfromhour<12)
            selectedfromhour= selectedfromhour+12;
        }

        if(selectedtoformat.equals("PM")){
            if(selectedtohour<12)
            selectedtohour= selectedtohour+12;
        }

        CommonAsyncTask ca = new CommonAsyncTask(this);
        String fromhour = "" + selectedfromhour;
        if (selectedfromhour < 10)
            fromhour = "0" + selectedfromhour;

        String tohour = "" + selectedtohour;
        if (selectedtohour < 10)
            tohour = "0" + selectedtohour;

        ca.reqBooktask(selectedfromdate + " " + fromhour + ":" + selectedfrommin + ":00",
                selectedtodate + " " + tohour + ":" + selectedtomin + ":00", no_of_people.getText().toString(), "req");
    }

    private void confirmBooking() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        String fromhour = "" + selectedfromhour;

        if (selectedfromhour < 10)
            fromhour = "0" + selectedfromhour;

        String tohour = "" + selectedtohour;
        if (selectedtohour < 10)
            tohour = "0" + selectedtohour;

        ca.confirmBooktask(client_id,
                trainer_id,
                selectedfromdate + " " + fromhour + ":" + selectedfrommin + ":00",
                selectedtodate + " " + tohour + ":" + selectedtomin + ":00", "", "",
                "", "",
                trainer_gymaddress.getText().toString(), no_of_people.getText().toString(), nonce, "confirm");
    }


    private boolean isValid() {
        if (!Validations.isFieldNotSelect(from)) {
            Toast.makeText(this, "Select start time first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Validations.isFieldNotSelect(to)) {
            Toast.makeText(this, "Select end time first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Validations.isFieldNotEmpty(no_of_people))
            return false;
        else if (nonce.equals("")) {
            Alerts.okAlert(BookingInfo.this, "Something wrong in card details", "Error");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == LOCATION_CALLBACK) {
                String add = data.getStringExtra("str");
                trainer_gymaddress.setText(add);

            } else {
                trainer_gymaddress.setText(data.getStringExtra("homeaddress"));
                trainer_gymaddress.setVisibility(View.VISIBLE);
            }
        }
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


    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parent = (CommonStatusResultObj) response;
        if (!parent.isError()) {
            if (service.equals("req")) {
                ConfirmAlert(BookingInfo.this, parent.getData().getResults().getHours(), parent.getData().getResults().getPrice());
            } else
                Alerts.okAlert(BookingInfo.this, parent.getMessage(), "", true);
        } else
            Alerts.okAlert(BookingInfo.this, parent.getMessage(), "");

    }

    public void ConfirmAlert(final Context activity, float hours, int price) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        View dialog = LayoutInflater.from(activity).inflate(R.layout.alertbooking, null, false);
        TextView txtstart_time = (TextView) dialog.findViewById(R.id.start_time);
        TextView txtend_time = (TextView) dialog.findViewById(R.id.end_time);
        TextView txtno_people = (TextView) dialog.findViewById(R.id.no_people);
        TextView txtprice = (TextView) dialog.findViewById(R.id.price);
        TextView txthours = (TextView) dialog.findViewById(R.id.hours);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);

        txtstart_time.setText(from.getText().toString());
        txtend_time.setText(to.getText().toString());
        txtno_people.setText(no_of_people.getText().toString());
        txthours.setText("" + hours);
        txtprice.setText("$" + price);


        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBooking();
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();
    }


    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        nonce = paymentMethodNonce.getNonce();
    }

    @Override
    public void onError(Exception error) {
        if (error instanceof ErrorWithResponse) {
            Alerts.okAlert(BookingInfo.this, error.getMessage(), "Error");
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = ((ErrorWithResponse) error).errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    Log.e("Braintreeerror", expirationMonthError.getMessage());
                    // Alerts.okAlert(BookingInfo.this, expirationMonthError.getMessage(), "Error");
                }
            }
        }
    }


    @Override
    public void onCancel(int requestCode) {
    }
}
