package com.cliknfit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.pojo.NavItem;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;

import java.util.ArrayList;

/**
 * Created by katrina on 24/07/17.
 */

public class AdapterNavigation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<NavItem> datalist;
    protected String userId;
    private Adapterinterface listener;
    private int row_index=0;

    public AdapterNavigation(Adapterinterface listener, Context context, ArrayList<NavItem> datalist) {
        this.context = context;
        this.listener = listener;
        this.datalist = datalist;
        userId = AppPreference.getPreference(context, Constants.USERID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_navigation, parent, false);
        return vh = new AdapterNavigation.AdapterVideoListViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((AdapterVideoListViewHolder) holder).title.setText(datalist.get(position).getName());
        ((AdapterVideoListViewHolder) holder).icon.setImageDrawable(context.getResources().getDrawable(datalist.get(position).getImg()));

        ((AdapterVideoListViewHolder) holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
                listener.sendcallback(position,"nav");
            }
        });
       /* if(row_index==position){
            ((AdapterVideoListViewHolder) holder).title.setTextColor(context.getResources().getColor(R.color.BUTTON_PINK_COLOR));
        }
        else
        {
            ((AdapterVideoListViewHolder) holder).title.setTextColor(context.getResources().getColor(R.color.BACKGROUND_COLOR));
        }*/

    }


    @Override
    public int getItemCount() {
         return datalist.size();
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageView icon;
        protected LinearLayout row;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            row = (LinearLayout) itemView.findViewById(R.id.row);

        }
    }

}