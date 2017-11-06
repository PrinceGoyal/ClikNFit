package com.cliknfit.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cliknfit.R;
import com.cliknfit.adapter.AdapterQna;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.interfaces.ApiResponse;
import com.cliknfit.pojo.CommonStatus;
import com.cliknfit.pojo.DataModel;
import com.cliknfit.pojo.QNAAnswersModel;
import com.cliknfit.pojo.QNAModel;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.CommonAsyncTask;
import com.cliknfit.util.Constants;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.OnClick;

public class QNA extends AppCompatActivity implements Adapterinterface, ApiResponse {

    private TextView skip;
    private RecyclerView mRecycler;
    private AdapterQna adapter;
    private Context context;
    private ArrayList<QNAModel> dataList;
    private TextView next;
    private ArrayList<String> answersId = new ArrayList<>();
    private ArrayList<String> questionsId = new ArrayList<>();
    private String answerIdStr = "";
    private String questionIdStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initviews();

    }

    private void initviews() {
        context = this;

        skip = (TextView) findViewById(R.id.skip);
        next = (TextView) findViewById(R.id.next);

        mRecycler = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new AdapterQna(this, this, dataList);
        mRecycler.setAdapter(adapter);

        addList();
        click();

    }

    private void click() {
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Dashboard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getselectedId(adapter.getupdatedlist());
                if (answersId.size() > 0)
                    submitdata();
                else
                    Alerts.okAlert(context, "Please select at least 1 answer.", "");
            }
        });
    }

    private boolean getselectedId(ArrayList<QNAModel> dataList) {
        answersId.clear();
        questionsId.clear();
        for (QNAModel model : dataList) {
            for (int i = 0; i < model.getAnswers().size(); i++) {
                QNAModel.QNABEANAnswer answerModel = model.getAnswers().get(i);
                int checkcount = 0;
                if (answerModel.isCheck()) {
                    if (checkcount == 0) {
                        questionsId.add("" + answerModel.getQuestion_id());
                        checkcount++;
                    }
                    answersId.add("" + answerModel.getId());
                }
            }
        }

        return false;
    }


    private void submitdata() {
        CommonAsyncTask ca = new CommonAsyncTask(this);

        answerIdStr = answersId.toString().substring(1, answersId.toString().length() - 1).replaceAll("\\s", "");
        questionIdStr = questionsId.toString().substring(1, questionsId.toString().length() - 1).replaceAll("\\s", "");
        ca.qnaSubmittask(AppPreference.getPreference(QNA.this, Constants.USERID), questionIdStr, answerIdStr, "submit");
    }


    private void addList() {
        CommonAsyncTask ca = new CommonAsyncTask(this);
        ca.qnaListtask("list");
    }

    @Override
    public void getResponse(Object response, String service) {
        CommonStatus parentModel = (CommonStatus) response;
        if (!parentModel.isError()) {
            if (service.equals("list")) {
                DataModel dataModel = parentModel.getData();
                dataList.clear();
                for (QNAModel model : dataModel.getQna()) {
                    for (QNAModel.QNABEANAnswer answerModel : model.getAnswers()) {
                        answerModel.setCheck(false);
                    }
                    dataList.add(model);
                }
                adapter.notifyDataSetChanged();
            }
            if (service.equals("submit")) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        } else {
            Alerts.okAlert(context, parentModel.getmessage(), "");
        }

    }

    @Override
    public void sendcallback(int position, String string) {


    }

    @Override
    public void sendcallbackforpreference(int position, String questionId, String answerId, String time) {

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
}
