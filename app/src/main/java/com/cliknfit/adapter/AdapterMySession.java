package com.cliknfit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.activity.Dashboard;
import com.cliknfit.activity.SessionDetails;
import com.cliknfit.activity.SessionTimer;
import com.cliknfit.activity.TrainerProfile;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.pojo.ModelMySession;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.R.attr.mode;
import static com.cliknfit.R.id.session;

/**
 * Created by prince on 09/08/17.
 */

public class AdapterMySession extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<ModelMySessionResults> datalist;
    protected String userId;
    protected String type;
    private Adapterinterface listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterMySession(Adapterinterface listener, Context context, ArrayList<ModelMySessionResults> datalist) {
        this.context = context;
        this.listener = listener;
        this.datalist = datalist;
        userId = AppPreference.getPreference(context, Constants.USERID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        // if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_ongoingsession, parent, false);
        return vh = new AdapterMySession.AdapterVideoListViewHolder(v);
      /*  } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;*/

    }

    protected class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        }
    }

    public void removeItem(int position) {
        /*datalist.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, datalist.size());*/
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
         ModelMySessionResults model = datalist.get(position);
        ((AdapterVideoListViewHolder) holder).tv_trainerName.setText(datalist.get(position).getTrainer().getName());
        String imageid = Constants.PICBASE_URL + "" + datalist.get(position).getTrainer().getImage();
        Picasso.with(context)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(((AdapterVideoListViewHolder) holder).profilepic);
        if(datalist.get(position).getHas_additional_hours()==1){
            model = datalist.get(position).getAdditional_hours();
            ((AdapterVideoListViewHolder) holder).invitefriend.setVisibility(View.GONE);
        }

        ((AdapterVideoListViewHolder) holder).tv_start_time.setText(model.getStart_time());
        ((AdapterVideoListViewHolder) holder).tv_endtime.setText(model.getEnd_time());

        ((AdapterVideoListViewHolder) holder).tv_location.setText(model.getLocation_tag());
        ((AdapterVideoListViewHolder) holder).tv_people.setText("" + model.getNo_of_people());
        ((AdapterVideoListViewHolder) holder).locationaddress.setText(model.getLocation_address());
        ((AdapterVideoListViewHolder) holder).tv_payment.setText("$" + model.getTotal_price());
        ((AdapterVideoListViewHolder) holder).tv_point.setText("" + model.getDisplay_point());
        ((AdapterVideoListViewHolder) holder).tv_hour.setText("" + model.getHour());



        if (model.getStatus() == 1) {
            ((AdapterVideoListViewHolder) holder).session.setVisibility(View.GONE);
            ((AdapterVideoListViewHolder) holder).invitefriend.setText("Request Pending");
            ((AdapterVideoListViewHolder) holder).invitefriend.setClickable(false);

        } else if (model.getStatus() == 2) {
            ((AdapterVideoListViewHolder) holder).session.setVisibility(View.GONE);
            ((AdapterVideoListViewHolder) holder).invitefriend.setVisibility(View.VISIBLE);
            if(model.getHas_additional_hours()==1)
            ((AdapterVideoListViewHolder) holder).invitefriend.setVisibility(View.GONE);

        } else if (model.getStatus() == 4) {
            ((AdapterVideoListViewHolder) holder).session.setVisibility(View.GONE);
            ((AdapterVideoListViewHolder) holder).invitefriend.setVisibility(View.GONE);

        } else if (model.getStatus() == 5) {
            ((AdapterVideoListViewHolder) holder).session.setVisibility(View.GONE);
            ((AdapterVideoListViewHolder) holder).invitefriend.setText("Request Rejected");
            ((AdapterVideoListViewHolder) holder).invitefriend.setClickable(false);

        }

        if (model.getIs_trainer_started() == 1 && model.getIs_client_started() == 0) {
            ((AdapterVideoListViewHolder) holder).invitefriend.setVisibility(View.GONE);
            ((AdapterVideoListViewHolder) holder).session.setVisibility(View.VISIBLE);
        }

        if (model.getIs_trainer_started() == 1 && model.getIs_client_started() == 1) {
            ((AdapterVideoListViewHolder) holder).session.setVisibility(View.GONE);
        }


        if (model.getStatus() == 1) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Request Pending");
            ((AdapterVideoListViewHolder) holder).cancelbooking.setVisibility(View.VISIBLE);
        } else if (model.getStatus() == 2) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Request Accepted");
            ((AdapterVideoListViewHolder) holder).cancelbooking.setVisibility(View.VISIBLE);
            if(model.getHas_additional_hours()==1)
                ((AdapterVideoListViewHolder) holder).tv_status.setText("ADDITIONAL HOURS");
        } else if (model.getStatus() == 3) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("On Session");
            if(model.getHas_additional_hours()==1)
                ((AdapterVideoListViewHolder) holder).tv_status.setText("ADDITIONAL HOURS");

        } else if (model.getStatus() == 4) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Session Completed");

        } else if (model.getStatus() == 5) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Request Rejected");

        }


        ((AdapterVideoListViewHolder) holder).invitefriend.setVisibility(View.GONE);



       /* ((AdapterVideoListViewHolder) holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelMySessionResults model;
                if (datalist.get(position).getHas_additional_hours() == 1)
                    model = datalist.get(position).getAdditional_hours();
                else
                    model = datalist.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("model", model);

                if (datalist.get(position).getStatus() == 3) {
                    context.startActivity(new Intent(context, SessionTimer.class)
                            .putExtras(bundle)
                    );
                } else if (datalist.get(position).getIs_trainer_started() == 1 && datalist.get(position).getIs_client_started() == 1) {
                    context.startActivity(new Intent(context, SessionTimer.class)
                            .putExtras(bundle)
                    );
                } else {
                    *//*context.startActivity(new Intent(context, SessionDetails.class)
                            .putExtras(bundle)
                    );*//*
                }
            }
        });*/

        final ModelMySessionResults finalModel = model;
        ((AdapterVideoListViewHolder) holder).viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TrainerProfile.class)
                        .putExtra("id", "" + datalist.get(position).getTrainer().getId())
                        .putExtra("book",0)
                );
            }
        });
        ((AdapterVideoListViewHolder) holder).callclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.sendcallback(0, datalist.get(position).getTrainer().getPhone());
            }
        });

        ((AdapterVideoListViewHolder) holder).cancelbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.sendcallbackforpreference(0, ""+ finalModel.getId(),"","cancel");
            }
        });


        ((AdapterVideoListViewHolder) holder).session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalModel.getIs_trainer_started() == 1 && finalModel.getIs_client_started() == 0) {
                    listener.sendcallbackforpreference(0, ""+ finalModel.getId(),"","");

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_trainerName, tv_people, tv_hour, locationaddress,cancelbooking,
                tv_payment, tv_point, tv_start_time,session,invitefriend, tv_location, tv_endtime, tv_status, viewProfile, callclient;

        protected ImageView profilepic;
        private LinearLayout row;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            profilepic = (ImageView) itemView.findViewById(R.id.profile_pic);

            cancelbooking = (TextView) itemView.findViewById(R.id.cancelbooking);
            invitefriend = (TextView) itemView.findViewById(R.id.deny);
            session = (TextView) itemView.findViewById(R.id.session);
            tv_trainerName = (TextView) itemView.findViewById(R.id.trainer_name);
            tv_start_time = (TextView) itemView.findViewById(R.id.tv_strattime);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_people = (TextView) itemView.findViewById(R.id.tv_people);
            tv_endtime = (TextView) itemView.findViewById(R.id.end_time);
            tv_status = (TextView) itemView.findViewById(R.id.status);
            viewProfile = (TextView) itemView.findViewById(R.id.viewProfile);
            callclient = (TextView) itemView.findViewById(R.id.callclient);
            tv_hour = (TextView) itemView.findViewById(R.id.tv_hour);
            tv_payment = (TextView) itemView.findViewById(R.id.tv_payment);
            tv_point = (TextView) itemView.findViewById(R.id.tv_point);
            locationaddress = (TextView) itemView.findViewById(R.id.locationaddress);
            row = (LinearLayout) itemView.findViewById(R.id.row);
        }
    }
}

 /*   @Override
    public int getItemViewType(int position) {
        if (datalist.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (datalist.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }*/