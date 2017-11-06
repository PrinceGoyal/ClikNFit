package com.cliknfit.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;
import com.cliknfit.util.Validations;

public class AboutChangeContact extends AppCompatActivity implements ApiResponse, View.OnClickListener {

    private int pageno;
    private LinearLayout ll_website, ll_email, ll_phone;
    private TextView txt_aboutus;
    private EditText oldPassword, newPassword, confirmPassword;
    private TextView changepassbtn;
    private static final int PERMISSIONS_REQUEST_CALL = 369;
    private String mobileno = "1-833-254-5634";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageno = getIntent().getIntExtra("pageno", 0);
        if (pageno == 1) {
            setContentView(R.layout.content_about);
            initAboutUs();
        } else if (pageno == 3) {
            setContentView(R.layout.content_changepassword);
            initChangePassword();
        } else if (pageno == 5) {
            setContentView(R.layout.content_contactus);
            initContactUs();
        }
        setTitle(getIntent().getStringExtra("title"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    private void initContactUs() {
        ll_website = (LinearLayout) findViewById(R.id.ll_website);
        ll_website.setOnClickListener(this);
        ll_email = (LinearLayout) findViewById(R.id.ll_email);
        ll_email.setOnClickListener(this);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        ll_phone.setOnClickListener(this);

    }

    private void initChangePassword() {
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        changepassbtn = (TextView) findViewById(R.id.submit);
        changepassbtn.setOnClickListener(this);

    }

    private void initAboutUs() {
        txt_aboutus = (TextView) findViewById(R.id.txt_aboutus);
        txt_aboutus.setText("A simple and convenient way for users to connect with a local certified personal trainer for on demand and same day training sessions. Unlike other similar applications, Cliknfit offers the client the option to book any trainer of their choice for the same amount and absolutely no commitment.\n\nThe option to search by name and view the trainers profile prior to booking is another added quality feature for the user. \n\nCliknfit allows easy scheduling and user friendly maneuvering throughout.");

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
        if (service.equals("updatepass"))
            if (parent.isError()) {
                Alerts.okAlert(AboutChangeContact.this, parent.getMessage(), "", false);
            } else {
                Alerts.okAlert(AboutChangeContact.this, parent.getMessage(), "", true);
                AppPreference.setPreference(AboutChangeContact.this, Constants.PASSWORD,newPassword.getText().toString());
            }
    }

    private void changePassword() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.updatePasswordtask(newPassword.getText().toString(), AppPreference.getPreference(this, Constants.USERID), "updatepass");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_website:
                startActivity(new Intent(AboutChangeContact.this, PrivacyPolicy.class)
                        .putExtra("no", "no")
                );
                break;
            case R.id.ll_email:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@cliknfit.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AboutChangeContact.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_phone:
                callOnPhone();
                break;

            case R.id.submit:
                if (isValid())
                    changePassword();
                break;

            default:
                break;


        }
    }

    private boolean isValid() {
        if (!Validations.isFieldNotEmpty(oldPassword))
            return false;
        else if (!Validations.isFieldNotEmpty(newPassword))
            return false;
        else if (!Validations.isFieldNotEmpty(confirmPassword))
            return false;
        else if (!Validations.isPassMatch(newPassword, confirmPassword))
            return false;
        else if (!AppPreference.getPreference(this, Constants.PASSWORD).equals(oldPassword.getText().toString())) {
            oldPassword.requestFocus();
            oldPassword.setError("Wrong Password");
            return false;
        } else {
            return true;
        }
    }

    public void callOnPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL);
            } else {
                Intent callIntent1 = new Intent(Intent.ACTION_CALL);
                callIntent1.setData(Uri.parse("tel:" + mobileno));
                startActivity(callIntent1);
            }
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + mobileno));
            startActivity(callIntent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callOnPhone();
                } else {
                    Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
