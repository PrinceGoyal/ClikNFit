package com.cliknfit.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.cliknfit.R;
import com.cliknfit.pojo.ChatDataModel;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;

import java.util.ArrayList;

/**
 * Created by prince on 04/08/17.
 */

public class AdapterChatDetails extends BaseAdapter {

    private final String userId;
    private Context context;
    private ArrayList<ChatDataModel> chatMessageList;
    private ChatDataModel chatBean;

    public AdapterChatDetails(Context context, ArrayList<ChatDataModel> chatMessageList) {
        this.context = context;
        this.chatMessageList = chatMessageList;
        userId = AppPreference.getPreference(context, Constants.USERID);
    }

    @Override
    public int getItemViewType(int pos) {
        return pos % 2;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        if (chatMessageList != null) {
            return chatMessageList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // return chatMessages.get(position);
        if (chatMessageList != null) {
            return chatMessageList.get(getCount() - position - 1);
        } else {
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        chatBean = chatMessageList.get(position);

        String message = chatBean.getMessage();

        if (!chatBean.getSenderId().equals(userId)) {
            convertView = inflater.inflate(R.layout.left_chat_list, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.right_chat_list, parent, false);

        }
        TextView messageTextView = (TextView) convertView.findViewById(R.id.message);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.timestamp);
        TextView date = (TextView) convertView.findViewById(R.id.datestamp);
        messageTextView.setText(Html.fromHtml(chatBean.getMessage()));
        if (!chatBean.getSenderId().equals(userId)) {
            timeTextView.setText("    "+ chatBean.getCreatedDate()+" " + chatBean.getCreatedOn()+"  ");
        }else {
            timeTextView.setText(chatBean.getCreatedDate()+" "+chatBean.getCreatedOn()+"  ");

        }
        return convertView;
    }


}
