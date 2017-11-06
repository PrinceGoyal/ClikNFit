package com.cliknfit.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.ModelMySession;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.cliknfit.R.id.deny;

public class SessionDetails extends AppCompatActivity implements ApiResponse {

    private ImageView profilePic;
    private TextView client_name, viewProfile, callclient, tv_strattime, end_time, tv_location, tv_people, tv_point, tv_hour, tv_payment, locationaddress, session, invitefriend;
    private ModelMySessionResults model;
    private static final int PERMISSIONS_REQUEST_CALL = 369;
    private String mobileno = "";
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);

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


    public void callOnPhone(String phone) {
        mobileno = phone;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL);
            } else {
                Intent callIntent1 = new Intent(Intent.ACTION_CALL);
                callIntent1.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent1);
            }
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        }
    }

    private void initViews() {
        model = (ModelMySessionResults) getIntent().getSerializableExtra("model");
        profilePic = (ImageView) findViewById(R.id.profile_pic);

        String imageid = Constants.PICBASE_URL + "" + model.getTrainer().getImage();
        Picasso.with(this)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(profilePic);
        mobileno = model.getTrainer().getPhone();
        client_name = (TextView) findViewById(R.id.client_name);
        client_name.setText(model.getTrainer().getName());

        viewProfile = (TextView) findViewById(R.id.viewProfile);
        callclient = (TextView) findViewById(R.id.callclient);
        tv_strattime = (TextView) findViewById(R.id.tv_strattime);

        tv_strattime.setText(model.getStart_time());

        end_time = (TextView) findViewById(R.id.end_time);
        end_time.setText(model.getEnd_time());

        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_location.setText(model.getLocation_tag());

        tv_people = (TextView) findViewById(R.id.tv_people);
        tv_people.setText("" + model.getNo_of_people());

        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_hour.setText("" + model.getHour());

        tv_payment = (TextView) findViewById(R.id.tv_payment);
        tv_payment.setText("$" + model.getPrice());

        tv_point = (TextView) findViewById(R.id.tv_point);
        tv_point.setText(String.valueOf("" + model.getDisplay_point()));

        locationaddress = (TextView) findViewById(R.id.locationaddress);
        locationaddress.setText("" + model.getLocation_address());


        invitefriend = (TextView) findViewById(R.id.deny);
        session = (TextView) findViewById(R.id.session);


        if (model.getStatus() == 1) {
            session.setVisibility(View.GONE);
            invitefriend.setText("Request Pending");
            invitefriend.setClickable(false);

        } else if (model.getStatus() == 2) {
            session.setVisibility(View.GONE);
            invitefriend.setVisibility(View.VISIBLE);

        } else if (model.getStatus() == 4) {
            session.setVisibility(View.GONE);
            invitefriend.setVisibility(View.GONE);

        } else if (model.getStatus() == 5) {
            session.setVisibility(View.GONE);
            invitefriend.setText("Request Rejected");
            invitefriend.setClickable(false);

        }

        if (model.getIs_trainer_started() == 1 && model.getIs_client_started() == 0) {
            invitefriend.setVisibility(View.VISIBLE);
            session.setVisibility(View.VISIBLE);
        }

        click();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callOnPhone(mobileno);
                } else {
                    Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void click() {
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TrainerProfile.class)
                        .putExtra("id", "" + model.getTrainer().getId())
                );
            }
        });

        callclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOnPhone(mobileno);

            }
        });


        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // rejectMethod();
            }
        });

        session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getIs_trainer_started() == 1 && model.getIs_client_started() == 0) {
                    startTimerMethod();
                }
            }
        });

    }

    private void startTimerMethod() {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.startTimerTask("" + model.getId(), "starttimer"
        );
    }


    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parent = (CommonStatusResultObj) response;
        if (!parent.isError())
            Alerts.okAlert(SessionDetails.this, parent.getMessage(), "", true);
        else
            Alerts.okAlert(SessionDetails.this, parent.getMessage(), "");

    }




    private void registerFcmReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cliknfit");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //{"notes":"","trainer_points":"20","actual_warm_up_min":0,"no_of_people":1,"is_client_started":true,"client_id":301,"trainerrating":{},"is_trainer_complete":0,
                // "additional_hours":{},"price":70,"actual_end_time":"","id":536,"actual_start_time":"","location_tag":"Client's Address","actual_cool_min":0,"break_end_time":"",
                // "trainer_id":249,"cool_start_time":"","location_address":"A28, Noida, Uttar Pradesh","is_client_complete":0,"trainer_rating":2,"submit_start_time":"2017-10-12 7:00:00 AM",
                // "cool_min":0,"nonce":"b6ed42d1-f45a-0bbc-64ad-d1fb50dce199","workoutlocation_id":0,"start_time":"7:00 AM","display_point":5,"has_additional_hours":0,"is_emergency_stop":0,
                // "status":2,"warm_up_min":0,"break_start_time":"","is_trainer_started":1,"client_rating":4,"created_at":"10-12-2017 12:31 AM","warm_up_end_time":"","
                // point":0,"actual_break_min":0,"hour":1,"updated_at":"2017-10-12 00:39:51","payment_id":null,"trainer":{"image":"2017_09_20_09_09_42_temp_photo.jpg",
                // "address":"C-89, Sector 121,Noida, Uttar Pradesh 201307","device_id":"","city":"Noida","device_type":"android","about_me":"Hello","phone":"+918376032344",
                // "additional_info":"Hshshdgd","device_token":"eUFztdDSnA4:APA91bErip1VI1Ob0tGgQx795iJsTnP7eyJVc9Aoox8UxuwoeRFqIP5OYJzgnnQrOZzPv2Vqa3UWN0_yGIJvTm3rb-9RmgNaPSHdl24HBTZfKj73jCfcsflvflJQLJ6cti15ynpy05nd",
                // "name":"trainer deepak","id":249,"email":"deep.30smarty@gmail.com","age":27},"client":{"image":"","address":"A28, Noida, Uttar Pradesh","device_id":"5455575",
                // "city":"Noida","device_type":"android","about_me":"","phone":"+917417668576","additional_info":"","device_token":"ceEBM2xsO_4:APA91bG9x1Wc_RQBWS39yswpZAvTppZiD9KNIUm-aZaLm9KbEon5Ez7WSAL8GIVpbBmMC0UlSQkvLsluADDtZzrzR1DUCkIPYcZk39_A5OQjlDxWY54rhJY9UPeZtJTlAEPS8kLEGJLP",
                // "name":"Prince","id":301,"email":"princeuser1@gmail.com","age":18},"commission":5,"clientrating":{}
                // ,"warm_up_start_time":"","total_price":75,"client_points":"15","end_time":"8:00 AM","cool_end_time":"","submit_end_time":"2017-10-12 8:00:00 AM","break_min":0,"comment":""}

                String mod = intent.getStringExtra("model");

                try {
                    JSONObject json = new JSONObject(mod);
                    model.setIs_trainer_started(json.getInt("is_trainer_started"));
                    model.setIs_client_started(json.getInt("is_client_started"));
                    model.setStatus(json.getInt("status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (model.getStatus() == 1) {
                    session.setVisibility(View.GONE);
                    invitefriend.setText("Request Pending");
                    invitefriend.setClickable(false);

                } else if (model.getStatus() == 2) {
                    session.setVisibility(View.GONE);
                    invitefriend.setVisibility(View.VISIBLE);

                } else if (model.getStatus() == 4) {
                    session.setVisibility(View.GONE);
                    invitefriend.setVisibility(View.GONE);

                } else if (model.getStatus() == 5) {
                    session.setVisibility(View.GONE);
                    invitefriend.setText("Request Rejected");
                    invitefriend.setClickable(false);

                }

                if (model.getIs_trainer_started() == 1 && model.getIs_client_started() == 0) {
                    invitefriend.setVisibility(View.VISIBLE);
                    session.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }


    public void onResume() {
        super.onResume();
        registerFcmReciever();
    }

}
