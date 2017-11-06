package com.cliknfit.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.DataModel;
import com.cliknfit.pojo.DataModelResultObj;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.pojo.WorkOutResult;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cliknfit.util.RoundedImageView;
import com.cliknfit.util.Validations;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity implements ApiResponse, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    TextView facebook;
    TextView googleplus;
    EditText email;
    EditText password;
    TextView login;
    TextView signup;
    TextView forgotpassword;

    private static final int PERMISSION_ALL = 12;
     private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
             Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
             Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    public static Location mLastLocation;


    private String loginType = "phone", device_type = "android";
    private String fbId = "", gPlusId = "", devideId = "5455575";
    private String lati = "", longti = "";
    private int SOCIALCALLBACKFACEBOOK = 12365;
    private int SOCIALCALLBACKGOOGLE = 65123;
    private String name;
    private String profilePic = "";
    private static int PERMISSION_ACCESS_COARSE_LOCATION = 56;
    private int homecount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        setgoogleapi();
        initViews();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                statusCheck();
                break;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (homecount == 0) {
            statusCheck();
            homecount++;
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    Log.e("", "");
                    lati = String.valueOf(mLastLocation.getLatitude());
                    longti = String.valueOf(mLastLocation.getLongitude());

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_ACCESS_COARSE_LOCATION);
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(broadcastReceiver1);
        mGoogleApiClient.disconnect();
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void setgoogleapi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    private void initViews() {
        facebook = (TextView) findViewById(R.id.facebook);
        googleplus = (TextView) findViewById(R.id.googleplus);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.signup);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);

        //all click listners
        click();
    }


    private void click() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class)
                        .putExtra("lati", lati)
                        .putExtra("longti", longti)
                );
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(context,Dashboard.class));
                if (isValid()) {
                    loginmethod();
                }
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPass.class));
            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fbintent = new Intent(context, SocialLogin.class);
                fbintent.putExtra("socialname", "facebook");
                startActivityForResult(fbintent, SOCIALCALLBACKFACEBOOK);
            }
        });
        googleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fbintent = new Intent(context, SocialLogin.class);
                fbintent.putExtra("socialname", "google");
                startActivityForResult(fbintent, SOCIALCALLBACKGOOGLE);
            }
        });
    }

    private boolean isValid() {
        if (!Validations.isValidEmail(email))
            return false;
        else if (!Validations.isFieldNotEmpty(password))
            return false;
        else {
            return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOCIALCALLBACKFACEBOOK) {
            if (data != null) {
                profilePic = data.getStringExtra("profilepic");
                email.setText(data.getStringExtra("email"));
                fbId = data.getStringExtra("socialid");
                if (!fbId.equals("")) {
                    name = data.getStringExtra("name");
                    loginType = "facebook";
                    password.setText(fbId);
                    loginmethod();
                }
            }
        }

        if (requestCode == SOCIALCALLBACKGOOGLE) {
            if (data != null) {
                profilePic = data.getStringExtra("profilepic");
                email.setText(data.getStringExtra("email"));
                gPlusId = data.getStringExtra("socialid");
                if (!gPlusId.equals("")) {
                    name = data.getStringExtra("name");
                    password.setText(gPlusId);
                    loginType = "google";
                    loginmethod();
                }
            }
        }
    }


    private void loginmethod() {
        String refreshedToken = AppPreference.getPreference(context, Constants.REGID);
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.logintask(email.getText().toString(),
                password.getText().toString(),
                loginType,
                fbId, gPlusId, devideId, refreshedToken, device_type, lati, longti, "login"
        );
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parentModel = (CommonStatusResultObj) response;
        DataModelResultObj dataModel = parentModel.getData();

        if (!parentModel.isError()) {
            WorkOutResult userModel1 = dataModel.getResults();
            UserModel userModel = userModel1.getUser();


            AppPreference.setUser(context, userModel);
            AppPreference.setPreference(context, Constants.USERID, String.valueOf(userModel.getId()));
            if (userModel.getStatus() == 0) {
                startActivity(new Intent(context, OtpVerification.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else if (userModel.getIs_agree() == 0) {
                AppPreference.setPreference(Login.this, Constants.STATUS, "1");
                startActivity(new Intent(context, PrivacyPolicy.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                AppPreference.setPreference(Login.this, Constants.AGREE, "1");
                AppPreference.setPreference(Login.this, Constants.STATUS, "1");
                AppPreference.setPreference(context, Constants.FBID, fbId);
                AppPreference.setPreference(context, Constants.GPLUSID, gPlusId);
                AppPreference.setPreference(context, Constants.DEVICEID, devideId);
                AppPreference.setPreference(context, Constants.DEVICETYPE, device_type);
                AppPreference.setPreference(context, Constants.PASSWORD, password.getText().toString());
                AppPreference.setPreference(context, Constants.LOGINTYPE, loginType);
                startActivity(new Intent(context, Dashboard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

        } else {
            if (dataModel.is_registered()) {
                if (dataModel.isDiff_login()) {
                    Alerts.okAlert(context, parentModel.getMessage(), "");
                    email.setText("");
                    password.setText("");
                    loginType = "phone";
                    fbId = "";
                    gPlusId = "";
                } else {
                    if (dataModel.is_login()) {
                        UserModel userModel = dataModel.getUser();
                        AppPreference.setUser(context, userModel);
                        AppPreference.setPreference(context, Constants.USERID, String.valueOf(userModel.getId()));
                        AppPreference.setPreference(context, Constants.FBID, fbId);
                        AppPreference.setPreference(context, Constants.GPLUSID, gPlusId);
                        AppPreference.setPreference(context, Constants.DEVICEID, devideId);
                        AppPreference.setPreference(context, Constants.DEVICETYPE, device_type);
                        AppPreference.setPreference(context, Constants.PASSWORD, password.getText().toString());
                        AppPreference.setPreference(context, Constants.LOGINTYPE, loginType);

                        if (dataModel.isStatus()) {
                            AppPreference.setPreference(Login.this, Constants.AGREE, "1");
                            AppPreference.setPreference(Login.this, Constants.STATUS, "1");

                            startActivity(new Intent(context, Dashboard.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            startActivity(new Intent(context, OtpVerification.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    } else {
                        Alerts.okAlert(context, parentModel.getMessage(), "");
                    }
                }
            } else {
                if (loginType.equals("phone")) {
                    Alerts.okAlert(context, parentModel.getMessage(), "");
                } else {
                    startActivity(new Intent(getApplicationContext(), Register.class)
                            .putExtra("lati", lati)
                            .putExtra("longti", longti)
                            .putExtra("loginType", loginType)
                            .putExtra("password", password.getText().toString())
                            .putExtra("email", email.getText().toString())
                            .putExtra("image", profilePic)
                            .putExtra("name", name)
                    );
                }
            }
        }

    }


}
