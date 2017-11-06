package com.cliknfit.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.DataModelResultObj;
import com.cliknfit.pojo.ModelMySession;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.pojo.WorkOutResult;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends AppCompatActivity implements ApiResponse, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_ALL = 1;
    private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 3220;
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    public static Location mLastLocation;
    private int homecount = 0;
    private String lati = "28.603771", longti = "77.356335";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setgoogleapi();
        context = this;

        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            } else {
                splashwork();
            }
        } else {
            splashwork();
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


    private void loginmethod() {
        String refreshedToken = AppPreference.getPreference(context, Constants.REGID);
        CommonAsyncTask ca = new CommonAsyncTask(this, "");

        ca.logintask(AppPreference.getPreference(this, Constants.Email),
                AppPreference.getPreference(this, Constants.PASSWORD),
                AppPreference.getPreference(this, Constants.LOGINTYPE),
                AppPreference.getPreference(this, Constants.FBID), AppPreference.getPreference(this, Constants.GPLUSID),
                AppPreference.getPreference(this, Constants.DEVICEID), refreshedToken,
                AppPreference.getPreference(this, Constants.DEVICETYPE), lati, longti, "login"
        );
    }

    private void splashwork() {
        final String userId = AppPreference.getPreference(this, Constants.USERID);
        final String status = AppPreference.getPreference(this, Constants.STATUS);
        final String agree = AppPreference.getPreference(this, Constants.AGREE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (userId.equals(Constants.BLANCK)) {
                    startActivity(new Intent(getApplicationContext(), Video.class));
                    finish();
                } else {
                    loginmethod();
                 /*   if (status.equals("0") || status.equals(""))
                        startActivity(new Intent(getApplicationContext(), OtpVerification.class));
                    else if (agree.equals("0") || agree.equals("")){
                        startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    }
                    finish();*/
                }
            }
        }, 1000);
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
                splashwork();
                break;
        }
    }

    @Override
    public void getResponse(Object response, String service) {
        if (service.equals("sessionList")) {
            ModelMySession parent = (ModelMySession) response;
            if (!parent.isError() && parent.getData().getResults().size() > 0) {
                ModelMySessionResults model;
                if (parent.getData().getResults().get(0).getHas_additional_hours() == 1)
                    model = parent.getData().getResults().get(0).getAdditional_hours();
                else
                    model = parent.getData().getResults().get(0);

                if (model.getIs_emergency_stop() == 1 && model.getIs_client_complete() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", parent.getData().getResults().get(0));
                    startActivity(new Intent(context, EndSession.class)
                            .putExtras(bundle)
                    );
                    finish();
                } else if (model.getStatus() == 3 && model.getIs_trainer_started() == 1 && model.getIs_client_started() == 1 &&
                        model.getIs_trainer_complete() == 1 && model.getIs_client_complete() == 0
                        ) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", parent.getData().getResults().get(0));
                    startActivity(new Intent(context, EndSession.class)
                            .putExtras(bundle)
                    );
                    finish();
                } else if (model.getStatus() == 3 && model.getIs_trainer_started() == 1 && model.getIs_client_started() == 1 &&
                        model.getIs_trainer_complete() == 0 && model.getIs_client_complete() == 0
                        ) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", parent.getData().getResults().get(0));
                    startActivity(new Intent(context, SessionTimer.class)
                            .putExtras(bundle)
                    );
                    finish();
                } else {
                    startActivity(new Intent(context, Dashboard.class)
                    );
                    finish();
                }
            } else {
                startActivity(new Intent(context, Dashboard.class)
                );
                finish();
            }
        } else {
            CommonStatusResultObj parentModel = (CommonStatusResultObj) response;
            DataModelResultObj dataModel = parentModel.getData();

            if (!parentModel.isError()) {
                WorkOutResult userModel1 = dataModel.getResults();
                UserModel userModel = userModel1.getUser();

                if (userModel.getStatus() == 0) {
                    startActivity(new Intent(context, OtpVerification.class)
                    );
                    finish();
                } else if (userModel.getIs_agree() == 0) {
                    AppPreference.setPreference(Splash.this, Constants.STATUS, "1");
                    startActivity(new Intent(context, PrivacyPolicy.class)
                    );
                    finish();
                } else {
                    sessionMethod();
                }

            } else {
                if (dataModel.is_registered()) {
                    if (dataModel.isDiff_login()) {
                        startActivity(new Intent(context, Login.class)
                                .putExtra("lati", lati)
                                .putExtra("longti", longti));
                        finish();
                    } else {
                        if (dataModel.is_login()) {
                            UserModel userModel = dataModel.getUser();
                            AppPreference.setUser(context, userModel);
                            if (dataModel.isStatus()) {
                                sessionMethod();
                            } else {
                                startActivity(new Intent(context, OtpVerification.class)
                                );
                                finish();
                            }
                        } else {
                            startActivity(new Intent(context, Login.class)
                                    .putExtra("lati", lati)
                                    .putExtra("longti", longti));
                            finish();
                        }
                    }
                } else {
                    startActivity(new Intent(context, Login.class)
                            .putExtra("lati", lati)
                            .putExtra("longti", longti));
                    finish();
                }
            }
        }
    }

    private void sessionMethod() {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.getSessiontask(AppPreference.getPreference(this, Constants.USERID), "sessionList");
    }


}

