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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.activity.NewsFeedDetail;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.pojo.QNAModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by katrina on 27/07/17.
 */

public class AdapterNewsFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<QNAModel> datalist;
    protected String userId;
    protected String type;
    private Adapterinterface listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterNewsFeed(Context context, ArrayList<QNAModel> datalist) {
        this.context = context;
        this.datalist = datalist;
        userId = AppPreference.getPreference(context, Constants.USERID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        // if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_newsfeed, parent, false);
        return vh = new AdapterNewsFeed.AdapterVideoListViewHolder(v);
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
        ((AdapterVideoListViewHolder) holder).ad_title.setText(datalist.get(position).getTitle());

        String imageid = Constants.PICBASE_URL + "" + datalist.get(position).getImage();
        Picasso.with(context)
                .load(imageid)
                .into(((AdapterVideoListViewHolder) holder).ad_image);

        ((AdapterVideoListViewHolder) holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NewsFeedDetail.class)
                        .putExtra("title",datalist.get(position).getTitle())
                        .putExtra("image",datalist.get(position).getImage())
                        .putExtra("description",datalist.get(position).getDescription())

                );
            }
        });

    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView ad_title;
        protected ImageView ad_image;
        private LinearLayout row;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            ad_image = (ImageView) itemView.findViewById(R.id.ad_image);
            ad_title = (TextView) itemView.findViewById(R.id.ad_title);
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