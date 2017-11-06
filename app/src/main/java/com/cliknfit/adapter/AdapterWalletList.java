package com.cliknfit.adapter;

import android.content.Context;
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
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.pojo.CardDBModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.cliknfit.R.id.card_number;
import static com.cliknfit.R.id.card_type;
import static com.cliknfit.R.id.cardicon;
import static com.cliknfit.R.id.exp_date;

/**
 * Created by prince on 22/08/17.
 */

public class AdapterWalletList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<CardDBModel> datalist;
    protected String userId;
    protected String type;
    private Adapterinterface listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterWalletList(Adapterinterface listener, Context context, ArrayList<CardDBModel> datalist) {
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
                R.layout.item_wallet, parent, false);
        return vh = new AdapterWalletList.AdapterVideoListViewHolder(v);
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


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String first,last;

        if (datalist.get(position).getCardtype().equals("visa") || datalist.get(position).getCardtype().equals("master") || datalist.get(position).getCardtype().equals("discover")) {
            first="xxxx-xxxx-xxxx-";
            last=datalist.get(position).getCardnumber().substring(Math.max(datalist.get(position).getCardnumber().length() - 4, 0));
            ((AdapterVideoListViewHolder) holder).cvv.setText("xxx");
        }else
        {
            first="xxxx-xxxxxx-";
            last=datalist.get(position).getCardnumber().substring(Math.max(datalist.get(position).getCardnumber().length() - 5, 0));
            ((AdapterVideoListViewHolder) holder).cvv.setText("xxxx");
        }

        ((AdapterVideoListViewHolder) holder).cardnumber.setText(first+last);


        ((AdapterVideoListViewHolder) holder).expdate.setText(datalist.get(position).getExpdate());
        ((AdapterVideoListViewHolder) holder).username.setText(AppPreference.getPreference(context, Constants.NAME));

        if (datalist.get(position).getCardtype().equals("visa"))
            ((AdapterVideoListViewHolder) holder).cardicon.setImageDrawable(context.getResources().getDrawable(R.drawable.visa_icon));
        else if (datalist.get(position).getCardtype().equals("master"))
            ((AdapterVideoListViewHolder) holder).cardicon.setImageDrawable(context.getResources().getDrawable(R.drawable.mastercard_icon));
        else if (datalist.get(position).getCardtype().equals("american"))
            ((AdapterVideoListViewHolder) holder).cardicon.setImageDrawable(context.getResources().getDrawable(R.drawable.american_express_logo));
        else if (datalist.get(position).getCardtype().equals("discover"))
            ((AdapterVideoListViewHolder) holder).cardicon.setImageDrawable(context.getResources().getDrawable(R.drawable.discover_icon));


    }

    @Override
    public int getItemCount() {
         return datalist.size();
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView cardnumber, expdate, cvv, username;
        protected ImageView cardicon;
        private RelativeLayout row;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            cardicon = (ImageView) itemView.findViewById(R.id.cardicon);

            cardnumber = (TextView) itemView.findViewById(R.id.cardnumber);
            expdate = (TextView) itemView.findViewById(R.id.expdate);
            cvv = (TextView) itemView.findViewById(R.id.cvv);
            username = (TextView) itemView.findViewById(R.id.username);
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