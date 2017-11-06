package com.cliknfit.activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterMyAddress;
import com.cliknfit.adapter.AdapterWorkoutAddress;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.AddressModel;
import com.cliknfit.pojo.CommonStatusResultObj;
import com.cliknfit.pojo.WorkChildModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;

import java.util.ArrayList;

import static android.R.attr.id;
import static com.cliknfit.R.id.add;
import static com.cliknfit.R.id.search;
import static com.cliknfit.R.id.tv_myAddress;
import static com.cliknfit.R.id.username;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAddress extends AppCompatActivity implements ApiResponse,Adapterinterface {

    private RecyclerView myaddressRecyclerView, workoutLocationRecyclerView;
    private ArrayList<WorkChildModel> workOutLocationList;
    private ArrayList<AddressModel> addressList;
    private AdapterMyAddress myAddressAdapter;
    private AdapterWorkoutAddress workoutLocationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        initViews();
    }

    private void initViews() {
        myaddressRecyclerView = (RecyclerView) findViewById(R.id.myaddressRecyclerView);
        workoutLocationRecyclerView = (RecyclerView) findViewById(R.id.workoutLocationRecyclerView);

        myaddressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        workoutLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        addlist();
    }

    private void addlist() {
        //client/get_address
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.getAddressTask(AppPreference.getPreference(this, Constants.USERID), "list");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        return true;
    }


    @Override
    public void getResponse(Object response, String service) {
        CommonStatusResultObj parent = (CommonStatusResultObj) response;
        if(workOutLocationList!=null) {
            if (workOutLocationList.size() > 0) {
                workOutLocationList.clear();
            }
            if (addressList.size() > 0) {
                addressList.clear();
            }
        }
        workOutLocationList=parent.getData().getResults().getWorkout_locations();
        addressList=parent.getData().getResults().getAddresses();

        myAddressAdapter=new AdapterMyAddress(this,this,addressList);
        workoutLocationAdapter=new AdapterWorkoutAddress(this,this,workOutLocationList);

        myaddressRecyclerView.setAdapter(myAddressAdapter);
        workoutLocationRecyclerView.setAdapter(workoutLocationAdapter);

    }

    @Override
    public void sendcallback(int position, String string) {
        Intent returnFromGalleryIntent = new Intent();
        returnFromGalleryIntent.putExtra("homeaddress", string);
        setResult(RESULT_OK, returnFromGalleryIntent);
        MyAddress.this.finish();
    }

    @Override
    public void sendcallbackforpreference(int position, String string, String date, String time) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            super.onBackPressed();
            return true;
        }

        if (id == R.id.addaddress) {
         startActivity(new Intent(getApplicationContext(),AddAddress.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
