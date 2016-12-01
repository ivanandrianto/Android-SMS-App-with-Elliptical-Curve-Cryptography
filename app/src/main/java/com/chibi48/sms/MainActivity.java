package com.chibi48.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chibi48.sms.model.ECC;
import com.chibi48.sms.model.Message;

import org.apache.commons.codec.DecoderException;

import java.io.UnsupportedEncodingException;
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

    private ListView listViewSMS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Messaging App");

        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String publicKey = prefs.getString("public_key", null);
        System.out.println("_-------------------PUBLIC KEY-------------------:" + publicKey);

        ECC ecc = new ECC();

        try {
            Log.d("encrypt",ecc.encryptString("abcd"));
            System.out.println("------------ENCRYPT---------------" + ecc.encryptString("abcd"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            Log.d("decrypt",ecc.decryptString(ecc.encryptString("abcd")));
            System.out.println("------------DECRYPT---------------" + ecc.decryptString(ecc.encryptString("abcd")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }


        // Read messages
        ArrayList<Message> messages = readMessage();
        HashMap<Integer, ArrayList<Message>> groupedMessages = groupMessage(messages);
        sortMessages(groupedMessages);
        ArrayList<Message> lastMessages = getLastMessageFromEachThread(groupedMessages);

        // set list view adapter
        listViewSMS = (ListView)findViewById(R.id.list);
        SMSListAdapter adapter = new SMSListAdapter(this, lastMessages);
        listViewSMS.setAdapter(adapter);
        listViewSMS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3) {
            TextView threadId = (TextView)v.findViewById(R.id.threadId);
            TextView address = (TextView)v.findViewById(R.id.address);

            Intent intent = new Intent(getBaseContext(), ConversationActivity.class);
            intent.putExtra("THREAD_ID", Integer.parseInt(threadId.getText().toString()));
            intent.putExtra("ADDRESS", address.getText().toString());
            startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_new_msg:
                Intent composeIntent = new Intent();
                composeIntent.setClass(this, NewMessageActivity.class);
                startActivity(composeIntent);
                break;
            case R.id.button_settings:
                Intent settingIntent = new Intent();
                settingIntent.setClass(this, SettingsActivity.class);
                startActivity(settingIntent);
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
            return msg2.getDate().compareTo(msg1.getDate());
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
