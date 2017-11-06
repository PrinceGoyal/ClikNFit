package com.cliknfit.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterMySession;
import com.cliknfit.adapter.AdapterTrainerList;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.DataTrainerInfo;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;

import java.util.ArrayList;

import static com.cliknfit.R.id.Trainer_gymaddress;
import static com.cliknfit.R.id.view;

public class TrainerList extends AppCompatActivity implements ApiResponse {


    private RecyclerView mRecycler;
    private AdapterTrainerList adapter;
    private ArrayList<UserModel> trainerList;
    private String keyword;
    private String selectedAddress;
    private TextView norecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initviews();
    }


    private void initviews() {
        selectedAddress = getIntent().getStringExtra("selectedAddress");
        keyword = getIntent().getStringExtra("keyword");
        mRecycler = (RecyclerView) findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));


        addList();


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
    private void addList() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.gettrainerSearch(keyword, "list");
    }

    @Override
    public void getResponse(Object response, String service) {
        if (service.equals("list")) {
            DataTrainerInfo parentmodel = (DataTrainerInfo) response;
            if (!parentmodel.isError()) {
                if (parentmodel.getData().getResults().size() > 0) {
                    trainerList = parentmodel.getData().getResults();
                    adapter = new AdapterTrainerList(this, trainerList,selectedAddress);
                    mRecycler.setAdapter(adapter);
                } else
                    Alerts.okAlert(TrainerList.this, "Nothing found with that keyword. Try with some different keywords", "",true);
            } else
                Alerts.okAlert(TrainerList.this, "Nothing found with that keyword. Try with some different keywords", "",true);
        }
    }
}
