package com.cliknfit.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.gms.common.api.Api;

import static com.cliknfit.R.id.countrycode;
import static com.cliknfit.R.id.dob;
import static com.cliknfit.R.id.mobile;

public class AddAddress extends AppCompatActivity implements ApiResponse {

    private TextView save;
    private EditText tv_myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

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
//        http://13.56.144.221/public/api/client/save_address

        save = (TextView) findViewById(R.id.save);
        tv_myAddress = (EditText) findViewById(R.id.tv_myAddress);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations.isFieldNotEmpty(tv_myAddress)) {
                    saveAddress();
                }
            }
        });

    }

    private void saveAddress() {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        ca.saveAddressTask(AppPreference.getPreference(this, Constants.USERID), tv_myAddress.getText().toString(), "save"
        );
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parent= (CommonStatusResultObj) response;
        if(!parent.isError()){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            finish();
        }else
            Alerts.okAlert(getApplicationContext(), parent.getMessage(), "");
    }
}
