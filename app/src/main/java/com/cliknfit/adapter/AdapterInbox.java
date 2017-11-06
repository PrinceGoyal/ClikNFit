package com.cliknfit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;

import java.util.ArrayList;

/**
 * Created by prince on 08/08/17.
 */

public class AdapterInbox extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<String> datalist;
    protected String userId;
    protected String type;
    private Adapterinterface listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterInbox(Adapterinterface listener, Context context, ArrayList<String> datalist) {
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
                R.layout.item_inbox, parent, false);
        return vh = new AdapterInbox.AdapterVideoListViewHolder(v);
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
        /*((AdapterVideoListViewHolder) holder).place.setText(datalist.get(position).getPlace());
        ((AdapterVideoListViewHolder) holder).time.setText(datalist.get(position).getDate());

        String imageid = Constants.PICBASE_URL + "" + datalist.get(position).getProfilePicture();
        Picasso.with(context)
                .load(imageid)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(((AdapterVideoListViewHolder) holder).profilepic);
*/
        ((AdapterVideoListViewHolder) holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.sendcallback(position,"");
            }
        });
    }


    @Override
    public int getItemCount() {
        // return datalist.size();
        return 6;
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView client_name, message, date,time;
        protected ImageView profilepic;
        protected RelativeLayout row;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            profilepic = (ImageView) itemView.findViewById(R.id.img_profile_pic);
            client_name = (TextView) itemView.findViewById(R.id.tv_clientName);
            message = (TextView) itemView.findViewById(R.id.tv_msg);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            row = (RelativeLayout) itemView.findViewById(R.id.row);
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