package com.chibi48.sms;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chibi48.sms.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ivan on 11/29/16.
 */

public class MainActivity extends AppCompatActivity {

    ListView listViewSMS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Messaging App");

        // Read messages
        ArrayList<Message> messages = readMessage();
        HashMap<Integer, ArrayList<Message>> groupedMessages = groupMessage(messages);
        sortMessages(groupedMessages);
        ArrayList<Message> lastMessages = getLastMessageFromEachThread(groupedMessages);
        System.out.println(lastMessages);

        // set list view adapter
        listViewSMS = (ListView)findViewById(R.id.list);
        SMSListAdapter adapter = new SMSListAdapter(this, lastMessages);
        listViewSMS.setAdapter(adapter);
        listViewSMS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3) {
                TextView threadId = (TextView)v.findViewById(R.id.threadId);
                System.out.println("Clicked thread id: " + threadId.getText().toString());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newMsgButton:
                Intent composeIntent = new Intent();
                composeIntent.setClass(this, NewMessageActivity.class);
                startActivity(composeIntent);
                break;
        }
    }

    private ArrayList<Message> readMessage() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
        ArrayList<Message> messages = new ArrayList<Message>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int threadId = cursor.getInt(cursor.getColumnIndex("thread_id"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String person = cursor.getString(cursor.getColumnIndex("person"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                Date date = new Date(cursor.getLong(cursor.getColumnIndex("date")));
                Date dateSent = new Date(cursor.getLong(cursor.getColumnIndex("date_sent")));
                Message message = new Message(id, threadId, address, person, date, dateSent, type, body);
                messages.add(message);

            } while (cursor.moveToNext());
        } else {
            System.out.println("Empty inbox");
        }

        return messages;
    }

    private HashMap<Integer, ArrayList<Message>> groupMessage(ArrayList<Message> messages) {
        HashMap<Integer, ArrayList<Message>> groupedMessages = new HashMap<Integer, ArrayList<Message>>();

        for (int i = 0; i < messages.size(); i++) {
           Message message = messages.get(i);
           int threadId = message.getThreadId();
           if (groupedMessages.containsKey(threadId)) {
                ArrayList<Message> messagesInThread = groupedMessages.get(threadId);
                messagesInThread.add(message);
           } else {
                ArrayList<Message> messagesInThread = new ArrayList<Message>();
                messagesInThread.add(message);
                groupedMessages.put(threadId, messagesInThread);
           }
        }

        return groupedMessages;
    }

    private void sortMessages(HashMap<Integer, ArrayList<Message>> groupedMessages) {
        Set set = groupedMessages.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            ArrayList<Message> messagesInThread = (ArrayList<Message>)me.getValue();
            Collections.sort(messagesInThread, new MessageDateComparator());
        }
    }

    class MessageDateComparator implements Comparator<Message> {
        public int compare(Message msg1, Message msg2) {
            return msg2.getActualDate().compareTo(msg1.getActualDate());
        }
    }

    private ArrayList<Message> getLastMessageFromEachThread(HashMap<Integer, ArrayList<Message>> groupedMessages) {
        ArrayList<Message> lastMessages = new ArrayList<Message>();
        Set set = groupedMessages.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            ArrayList<Message> messagesInThread = (ArrayList<Message>)me.getValue();
            Message lastMessage = messagesInThread.get(0);
            lastMessages.add(lastMessage);
        }
        Collections.sort(lastMessages, new MessageDateComparator());
        return lastMessages;
    }
}
