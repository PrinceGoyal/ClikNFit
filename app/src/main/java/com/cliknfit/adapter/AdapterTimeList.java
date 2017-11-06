package com.cliknfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cliknfit.R;

import java.util.ArrayList;

import static android.R.attr.data;
import static com.androidquery.util.AQUtility.getContext;

/**
 * Created by prince on 12/09/17.
 */

public class AdapterTimeList extends ArrayAdapter<String> {

    int pos;
    Context context;

    public AdapterTimeList(Context context, ArrayList<String> datalist, int pos) {
        super(context, 0, datalist);
        this.pos = pos;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String time = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_timepopup, parent, false);
        }
        // Lookup view for data population
        TextView txttime = (TextView) convertView.findViewById(R.id.txttime);
        // Populate the data into the template view using the data object
        txttime.setText(time);
        if (pos == position)
            txttime.setTextColor(context.getResources().getColor(R.color.WHITE));
        else
            txttime.setTextColor(context.getResources().getColor(R.color.TEXTFIELD_PLACEHOLDER_TEXT_COLOR_LIGHT));

        return convertView;
    }

    public void changeposition(int position) {
        pos = position;
        AdapterTimeList.this.notifyDataSetChanged();
    }
}
