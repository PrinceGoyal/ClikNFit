package com.cliknfit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;

import java.util.ArrayList;

/**
 * Created by katrina on 27/07/17.
 */

public class AdapterTrainer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<String> datalist;
    protected String userId;
    private Adapterinterface listener;

    public AdapterTrainer(Adapterinterface listener, Context context, ArrayList<String> datalist) {
        this.context = context;
        this.listener = listener;
        this.datalist = datalist;
        userId = AppPreference.getPreference(context, Constants.USERID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_toptrainer, parent, false);
        return vh = new AdapterTrainer.AdapterVideoListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
       // ((AdapterVideoListViewHolder) holder).title.setText(datalist.get(position));
       /* ((AdapterVideoListViewHolder) holder).title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.sendcallback(position, "nav");
            }
        });*/

    }

    @Override
    public int getItemCount() {
       // return datalist.size();
        return 10;
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.trainerPic);

        }
    }

}
