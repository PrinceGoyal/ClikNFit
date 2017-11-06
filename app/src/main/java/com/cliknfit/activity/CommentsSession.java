package com.cliknfit.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.pojo.ModelBookingResult;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.cliknfit.R.id.img_profile_pic;

public class CommentsSession extends AppCompatActivity {


    private RecyclerView mRecycler;
    private ArrayList<String> dataList;
    private ModelBookingResult model;
    private ImageView img_profile_pic,onestar,twostar,threestar,fourstar,fivestar;
    private TextView tv_description,tv_DateTime,tv_trainerName;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_session);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initviews();
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


    private void initviews() {
        context=this;
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            model = (ModelBookingResult) bundle.getSerializable("model");
        }

        img_profile_pic=(ImageView)findViewById(R.id.img_profile_pic);
        onestar=(ImageView)findViewById(R.id.onestar);
        twostar=(ImageView)findViewById(R.id.twostar);
        threestar=(ImageView)findViewById(R.id.threestar);
        fourstar=(ImageView)findViewById(R.id.fourstar);
        fivestar=(ImageView)findViewById(R.id.fivestar);


        tv_description=(TextView)findViewById(R.id.tv_description);

        tv_trainerName=(TextView)findViewById(R.id.tv_trainerName);
        tv_DateTime=(TextView)findViewById(R.id.tv_DateTime);


        tv_trainerName.setText(model.getTrainer().getName());
        tv_DateTime.setText(model.getCreated_at());
        tv_description.setText(model.getTrainerrating().getComment());

        String imageid = Constants.PICBASE_URL + "" + model.getTrainer().getImage();
        Picasso.with(this)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img_profile_pic);

        if(!model.getTrainerrating().getRating().equals("")) {
            if (Integer.parseInt(model.getTrainerrating().getRating()) == 0) {
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
            }

            if (Integer.parseInt(model.getTrainerrating().getRating()) == 1) {
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
            }
            if (Integer.parseInt(model.getTrainerrating().getRating()) == 2) {
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
            }
            if (Integer.parseInt(model.getTrainerrating().getRating()) == 3) {
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
            }
            if (Integer.parseInt(model.getTrainerrating().getRating()) == 4) {
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.emptypink_star));
            }
            if (Integer.parseInt(model.getTrainerrating().getRating()) == 5) {
                onestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                twostar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                threestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                fourstar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
                fivestar.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_pinkstar));
            }
        }



    }

}
