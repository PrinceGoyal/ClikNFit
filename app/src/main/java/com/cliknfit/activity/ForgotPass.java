package com.cliknfit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cliknfit.util.Validations;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cliknfit.R.id.mobile;

public class ForgotPass extends AppCompatActivity implements ApiResponse {

    EditText email;
    EditText phone,countrycode;
    TextView submit;
    ImageView back;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowTitleEnabled(true);
        initViews();
    }

    private void initViews() {
        context = this;
        phone = (EditText) findViewById(mobile);
        countrycode = (EditText) findViewById(R.id.countrycode);
        email = (EditText) findViewById(R.id.email);
        submit = (TextView) findViewById(R.id.submit);
        back = (ImageView) findViewById(R.id.back);
        mobileType();
        click();

    }


 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


    private void click() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    forgotMethod();
                }
            }
        });

    }

    private void mobileType() {
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i2 == 1) {
                    phone.setText("("+charSequence);
                    phone.setSelection(phone.getText().length());
                }
                if (i == 3 && i2 == 1) {
                    phone.setText(charSequence + ") ");
                    phone.setSelection(phone.getText().length());
                }
                if (i == 8 && i2 == 1) {
                    phone.setText(charSequence + "-");
                    phone.setSelection(phone.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void forgotMethod() {
        String phone1=countrycode.getText().toString()+phone.getText().toString().replace("(","").replace(")","").replace("-","").replaceAll("\\s+","");
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.forgetPassTask(countrycode.getText().toString(),email.getText().toString(),phone1, "forgot");
    }


    private boolean isValid() {
        boolean returnflag = false;
        if (email.getText().toString().trim().isEmpty() && phone.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter email or phone number", Toast.LENGTH_SHORT).show();
        } else if (!email.getText().toString().trim().isEmpty()) {
            if (!Validations.isValidEmail(email)) {
                returnflag = false;
            } else {
                returnflag = true;
            }
        } else {
            if(!Validations.isFieldNotEmpty(countrycode)){
                return false;
            }else {
                if (!Validations.isValidMobile(phone)) {
                    returnflag = false;
                } else {
                    returnflag = true;
                }
            }
        }
        return returnflag;

    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parentModel = (CommonStatusResultObj) response;
        if (parentModel.isError())
            Alerts.okAlert(context, parentModel.getMessage(), "");
        else {
            Toast.makeText(context, parentModel.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
