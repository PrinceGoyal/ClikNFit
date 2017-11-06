package com.cliknfit.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cliknfit.R;
import com.cliknfit.activity.UpdateProfile;
import com.cliknfit.interfaces.Adapterinterface;
import com.cliknfit.pojo.QNAModel;
import com.cliknfit.pojo.WorkChildModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.cliknfit.util.Validations;

import java.util.ArrayList;

import static com.cliknfit.R.id.ll_healthConditionProblem1;
import static com.cliknfit.R.id.ll_healthConditionProblem2;
import static com.paypal.android.sdk.onetouch.core.metadata.ah.f;
import static com.paypal.android.sdk.onetouch.core.metadata.ah.i;

/**
 * Created by prince on 10/08/17.
 */

public class AdapterQna extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected ArrayList<QNAModel> datalist;
    protected String userId;
    protected String type;
    private Adapterinterface listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterQna(Adapterinterface listener, Context context, ArrayList<QNAModel> datalist) {
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
                R.layout.item_qna, parent, false);
        return vh = new AdapterQna.AdapterVideoListViewHolder(v);
      /*  } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;*/

    }

    public ArrayList<QNAModel> getupdatedlist() {
        return datalist;
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
        ((AdapterVideoListViewHolder) holder).title.setText(datalist.get(position).getTitle());
        final ArrayList<QNAModel.QNABEANAnswer> answerList = datalist.get(position).getAnswers();
        for (int i=0;i<answerList.size();i++) {
            setCheckBox(((AdapterVideoListViewHolder) holder).chechbox, answerList.get(i).getTitle(), position,i);
        }

    }

    private void setCheckBox(LinearLayout rg, String text, final int position, final int checkpos) {
        CheckBox cb = new CheckBox(context);
        cb.setText(text);
        cb.setTextColor(context.getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR));
        cb.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.BUTTON_PINK_COLOR)));
        rg.addView(cb);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    datalist.get(position).getAnswers().get(checkpos).setCheck(true);
                   // AdapterQna.this.notifyDataSetChanged();
                    Log.e("","");
                }else{
                    datalist.get(position).getAnswers().get(checkpos).setCheck(false);
                   // AdapterQna.this.notifyDataSetChanged();
                    Log.e("","");
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    protected class AdapterVideoListViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected LinearLayout chechbox;

        public AdapterVideoListViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            chechbox = (LinearLayout) itemView.findViewById(R.id.chechbox);
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