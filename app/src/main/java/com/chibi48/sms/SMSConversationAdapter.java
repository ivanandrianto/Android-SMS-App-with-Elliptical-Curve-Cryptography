package com.chibi48.sms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chibi48.sms.model.Message;

import java.util.ArrayList;

/**
 * Created by ivan on 11/30/16.
 */

public class SMSConversationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Message> messages;

    public SMSConversationAdapter(Context context, ArrayList<Message> messages) {
        super();
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int messageType = messages.get(position).getType();

        if (convertView == null) {
            if (messageType == 1) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.list_item_message_left, parent, false);
            } else {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.list_item_message_right, parent, false);
            }
        }

        TextView textViewTime = (TextView)convertView.findViewById(R.id.message_date);
        TextView textViewBody = (TextView)convertView.findViewById(R.id.message_body);

        String dateStr = messages.get(position).getDate().toString();
        String[] dateStrSplit = dateStr.split(" ");
        textViewTime.setText(dateStrSplit[1] + " " + dateStrSplit[2] + " " + dateStrSplit[3]);
        textViewBody.setText(messages.get(position).getBody());

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
