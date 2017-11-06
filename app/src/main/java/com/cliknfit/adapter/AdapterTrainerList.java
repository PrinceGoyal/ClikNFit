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
import com.cliknfit.activity.BookingInfo;
import com.cliknfit.activity.Dashboard;
import com.cliknfit.activity.SessionDetails;
import com.cliknfit.activity.SessionTimer;
import com.cliknfit.activity.TrainerProfile;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.pojo.ModelMySessionResults;
import com.cliknfit.pojo.UserModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by prince on 15/09/17.
 */

public class AdapterTrainerList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<UserModel> datalist;
    protected String userId;
    protected String type;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private String selectedAddress;

    public AdapterTrainerList(Context context, ArrayList<UserModel> datalist,String selectedAddress) {
        this.context = context;
        this.datalist = datalist;
        this.selectedAddress=selectedAddress;
        userId = AppPreference.getPreference(context, Constants.USERID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        // if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_trainerlist, parent, false);
        return vh = new AdapterTrainerList.AdapterVideoListViewHolder(v);
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
        //tv_trainerName, tv_speciality, tv_city, tv_moto, viewProfile, booknoow;


        ((AdapterVideoListViewHolder) holder).tv_trainerName.setText(datalist.get(position).getName());
        ((AdapterVideoListViewHolder) holder).tv_speciality.setText(datalist.get(position).getSpeciality());
        ((AdapterVideoListViewHolder) holder).tv_city.setText(datalist.get(position).getCity());
        ((AdapterVideoListViewHolder) holder).tv_moto.setText(datalist.get(position).getMotto());


        if(datalist.get(position).getIs_online()==1){
            ((AdapterVideoListViewHolder) holder).online.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_online));
            ((AdapterVideoListViewHolder) holder).booknoow.setVisibility(View.VISIBLE);
        }else {
            ((AdapterVideoListViewHolder) holder).online.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_offline));
            ((AdapterVideoListViewHolder) holder).booknoow.setVisibility(View.INVISIBLE);

        }



        String imageid = Constants.PICBASE_URL + "" + datalist.get(position).getImage();
        Picasso.with(context)
                .load(imageid)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(((AdapterVideoListViewHolder) holder).profilepic);


        ((AdapterVideoListViewHolder) holder).viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TrainerProfile.class)
                        .putExtra("id", "" + datalist.get(position).getId())
                );
            }
        });

        ((AdapterVideoListViewHolder) holder).booknoow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, BookingInfo.class)
                        .putExtra("trainer_id", datalist.get(position).getId())
                        .putExtra("image", datalist.get(position).getImage())
                        .putExtra("gymaddress", datalist.get(position).getGym_address())
                        .putExtra("name", datalist.get(position).getName())
                        .putExtra("selectedAddress",selectedAddress)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_trainerName, tv_speciality, tv_city, tv_moto, viewProfile, booknoow;
        protected ImageView profilepic,online;
        private RelativeLayout row;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);
            profilepic = (ImageView) itemView.findViewById(R.id.img_profile_pic);
            online = (ImageView) itemView.findViewById(R.id.online);
            tv_trainerName = (TextView) itemView.findViewById(R.id.trainername);
            tv_speciality = (TextView) itemView.findViewById(R.id.tv_speciality);
            tv_city = (TextView) itemView.findViewById(R.id.tv_city);
            tv_moto = (TextView) itemView.findViewById(R.id.tv_moto);
            viewProfile = (TextView) itemView.findViewById(R.id.viewProfile);
            //     row = (RelativeLayout) itemView.findViewById(R.id.row);
            booknoow = (TextView) itemView.findViewById(R.id.booknoow);
        }
    }
}