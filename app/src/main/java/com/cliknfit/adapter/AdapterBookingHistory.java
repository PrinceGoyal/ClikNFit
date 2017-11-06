package com.cliknfit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.activity.SessionDetails;
import com.cliknfit.activity.SessionTimer;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.pojo.ModelBookingResult;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by prince on 27/09/17.
 */

public class AdapterBookingHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<ModelBookingResult> datalist;
    protected String userId;
    protected String type;
    private Adapterinterface listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterBookingHistory(Adapterinterface listener, Context context, ArrayList<ModelBookingResult> datalist) {
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
                R.layout.item_mysession, parent, false);
        return vh = new AdapterBookingHistory.AdapterVideoListViewHolder(v);
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
        ((AdapterVideoListViewHolder) holder).tv_trainerName.setText(datalist.get(position).getTrainer().getName());
        ((AdapterVideoListViewHolder) holder).tv_DateTime.setText(datalist.get(position).getCreated_at());
        ((AdapterVideoListViewHolder) holder).tv_start_time.setText(datalist.get(position).getStart_time());
        ((AdapterVideoListViewHolder) holder).tv_endtime.setText(datalist.get(position).getEnd_time());

        if (datalist.get(position).getStatus() == 1) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Request Pending");
            ((AdapterVideoListViewHolder) holder).statusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_roundstatusone));
            ((AdapterVideoListViewHolder) holder).tv_comments.setVisibility(View.GONE);

        } else if (datalist.get(position).getStatus() == 2) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Request Accepted");
            ((AdapterVideoListViewHolder) holder).statusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_roundstatustwo));
            ((AdapterVideoListViewHolder) holder).tv_comments.setVisibility(View.GONE);

        } else if (datalist.get(position).getStatus() == 3) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("On Session");
            ((AdapterVideoListViewHolder) holder).statusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_roundstatusthree));
            ((AdapterVideoListViewHolder) holder).tv_comments.setVisibility(View.GONE);

        } else if (datalist.get(position).getStatus() == 4) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Session Completed");
            ((AdapterVideoListViewHolder) holder).statusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_roundstatusfour));
            ((AdapterVideoListViewHolder) holder).tv_comments.setVisibility(View.VISIBLE);

        } else if (datalist.get(position).getStatus() == 5) {
            ((AdapterVideoListViewHolder) holder).tv_status.setText("Request Rejected");
            ((AdapterVideoListViewHolder) holder).statusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_roundstatusfive));
            ((AdapterVideoListViewHolder) holder).tv_comments.setVisibility(View.GONE);

        }


        String imageid = Constants.PICBASE_URL + "" + datalist.get(position).getTrainer().getImage();
        Picasso.with(context)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(((AdapterVideoListViewHolder) holder).profilepic);


        ((AdapterVideoListViewHolder) holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            listener.sendcallback(position,"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_trainerName, tv_DateTime, tv_start_time, tv_endtime, tv_status, tv_comments;
        protected ImageView profilepic, statusIcon;
        private RelativeLayout row;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            profilepic = (ImageView) itemView.findViewById(R.id.img_profile_pic);
            statusIcon = (ImageView) itemView.findViewById(R.id.statusIcon);

            tv_trainerName = (TextView) itemView.findViewById(R.id.tv_trainerName);
            tv_DateTime = (TextView) itemView.findViewById(R.id.tv_DateTime);
            tv_start_time = (TextView) itemView.findViewById(R.id.tv_start_time);
            tv_endtime = (TextView) itemView.findViewById(R.id.tv_endtime);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            row = (RelativeLayout) itemView.findViewById(R.id.row);
            tv_comments = (TextView) itemView.findViewById(R.id.tv_comments);
        }
    }
}