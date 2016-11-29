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
 * Created by ivan on 11/29/16.
 */

public class SMSListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Message> messages;

    public SMSListAdapter(Context context, ArrayList<Message> messages) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.message_list_row, parent, false);
        }

        TextView textViewAddress = (TextView)convertView.findViewById(R.id.address);
        TextView textViewBody = (TextView)convertView.findViewById(R.id.body);
        TextView textViewDate = (TextView)convertView.findViewById(R.id.date);
        TextView textThreadId = (TextView)convertView.findViewById(R.id.threadId);

        String dateStr = messages.get(position).getDate().toString();
        String[] dateStrSplit = dateStr.split(" ");
        textViewAddress.setText(messages.get(position).getAddress());
        textViewBody.setText(messages.get(position).getBody());
        textViewDate.setText(dateStrSplit[1] + " " + dateStrSplit[2] + " " + dateStrSplit[3]);
        textThreadId.setText(String.valueOf(messages.get(position).getThreadId()));

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