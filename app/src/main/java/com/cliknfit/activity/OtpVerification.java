package com.cliknfit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.customviews.RegulerText;
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
import com.cliknfit.util.Validations;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cliknfit.R.id.txtMobileNumber;

public class OtpVerification extends AppCompatActivity implements ApiResponse {

    TextView submit, resend, txtMobileNumber;
    EditText otp1;
    EditText otp2;
    EditText otp3;
    EditText otp4;
    ImageView back;
    private String phonenumber = "";
    private String otpCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        initviews();

    }

    private void initviews() {
        phonenumber = AppPreference.getPreference(this, Constants.Mobile);
        submit = (TextView) findViewById(R.id.submit);
        resend = (TextView) findViewById(R.id.resend);
        txtMobileNumber = (TextView) findViewById(R.id.txtMobileNumber);
        if (!getIntent().hasExtra("number"))
            txtMobileNumber.setText(AppPreference.getPreference(this, Constants.COUNTRYCODE) + phonenumber);
        else {
            resend.setVisibility(View.GONE);
            phonenumber = getIntent().getStringExtra("number");
            txtMobileNumber.setText(getIntent().getStringExtra("cc") + getIntent().getStringExtra("number"));
            otpCheck = getIntent().getStringExtra("otp");
        }

        otp1 = (EditText) findViewById(R.id.otp1);
        otp2 = (EditText) findViewById(R.id.otp2);
        otp3 = (EditText) findViewById(R.id.otp3);
        otp4 = (EditText) findViewById(R.id.otp4);
        back = (ImageView) findViewById(R.id.back);

        textchangeListeners();
        click();
    }

    private void textchangeListeners() {
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void click() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    final String otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                    if (!getIntent().hasExtra("number")) {
                        resend.setClickable(true);
                        otpMethod(otp);
                    } else {
                        if (otp.equals(otpCheck)) {
                            AppPreference.setPreference(OtpVerification.this, Constants.COUNTRYCODE, getIntent().getStringExtra("cc"));
                            AppPreference.setPreference(OtpVerification.this, Constants.Mobile, getIntent().getStringExtra("number"));
                            Alerts.okAlert(OtpVerification.this, "Mobile Number Changed", "Success", true);
                        } else {
                            Alerts.okAlert(OtpVerification.this, "Otp Mismatched", "Fail");
                        }
                    }
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (isValid()) {
                String otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                otpResendMethod(otp);
                //  }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void otpResendMethod(String otp) {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.resendOtpTask(otp, AppPreference.getPreference(this, Constants.COUNTRYCODE) + phonenumber.replace("(", "").replace(")", "").replace("-", "").replaceAll("\\s+", ""), "resend");
    }


    private void otpMethod(String otp) {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.otpTask(otp, AppPreference.getPreference(this, Constants.COUNTRYCODE) + phonenumber.replace("(", "").replace(")", "").replace("-", "").replaceAll("\\s+", ""), "otp");
    }


    private boolean isValid() {
        if (!Validations.isFieldNotEmpty(otp1))
            return false;
        else if (!Validations.isFieldNotEmpty(otp2))
            return false;
        else if (!Validations.isFieldNotEmpty(otp3))
            return false;
        else if (!Validations.isFieldNotEmpty(otp4))
            return false;
        else {
            return true;
        }
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parentModel = (CommonStatusResultObj) response;
        if (service.equals("resend")) {
            if (!parentModel.isError()) {
                Alerts.okAlert(OtpVerification.this, parentModel.getMessage(), "Success");
            }
        } else {
            if (!parentModel.isError()) {
                DataModelResultObj dataModel = parentModel.getData();
                WorkOutResult userModel1 = dataModel.getResults();
                UserModel userModel = userModel1.getUser();

                AppPreference.setPreference(OtpVerification.this, Constants.STATUS, "1");
                AppPreference.setPreference(OtpVerification.this, Constants.USERID, String.valueOf(userModel.getId()));
                if (!getIntent().hasExtra("number")) {
                    startActivity(new Intent(OtpVerification.this, PrivacyPolicy.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else {
                    AppPreference.setPreference(OtpVerification.this, Constants.COUNTRYCODE, String.valueOf(userModel.getCountry_code()));
                    AppPreference.setPreference(OtpVerification.this, Constants.Mobile, userModel.getPhone());
                    Alerts.okAlert(OtpVerification.this, parentModel.getMessage(), "Success", true);
                }

            } else {
                Alerts.okAlert(OtpVerification.this, parentModel.getMessage(), "");
            }
        }
    }

}
