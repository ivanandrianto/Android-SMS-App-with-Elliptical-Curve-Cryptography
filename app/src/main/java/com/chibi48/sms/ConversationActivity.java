package com.chibi48.sms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chibi48.sms.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ConversationActivity extends AppCompatActivity {

    private int threadId;
    private String address;
    private boolean encryptOn;
    private boolean signatureOn;
    private EditText content;
    private ListView listViewSMS;
    private ToggleButton encryptToggle;
    private ToggleButton signatureToggle;
    private SMSConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        threadId = Integer.parseInt(getIntent().getExtras().get("THREAD_ID").toString());
        address = getIntent().getExtras().get("ADDRESS").toString();

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(address);
        content = (EditText) findViewById(R.id.message_body);

        // Get messages
        ArrayList<Message> messages = readMessageOfThisConversation();
        System.out.println(messages);
        Collections.sort(messages, new MessageDateComparator());

        // Set list view adapter
        listViewSMS = (ListView)findViewById(R.id.list);
        adapter = new SMSConversationAdapter(this, messages);
        listViewSMS.setAdapter(adapter);
        listViewSMS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                TextView textViewBody = (TextView)v.findViewById(R.id.message_body);
                System.out.println(textViewBody.getText().toString());

                Intent intent = new Intent(getBaseContext(), DecryptActivity.class);
                intent.putExtra("MESSAGE_BODY", textViewBody.getText().toString());
                startActivity(intent);

            }
        });
        listViewSMS.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listViewSMS.smoothScrollToPosition(adapter.getCount() - 1);
        scrollMyListViewToBottom();

        encryptToggle = (ToggleButton) findViewById(R.id.encryptToggle);
        encryptOn = true;
        encryptToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    encryptOn = true;
                } else {
                    encryptOn = false;
                }
            }
        });

        signatureToggle = (ToggleButton) findViewById(R.id.signatureToggle);
        signatureOn = true;
        signatureToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    signatureOn = true;
                } else {
                    signatureOn = false;
                }
            }
        });
    }

    private ArrayList<Message> readMessageOfThisConversation() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, "thread_id=" + threadId, null, null);
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

    class MessageDateComparator implements Comparator<Message> {
        public int compare(Message msg1, Message msg2) {
            return msg1.getDate().compareTo(msg2.getDate());
        }
    }

    public void sendSMS(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSend:
                String to = address;
                String msg = content.getText().toString();
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(to, null, msg, null, null);
                break;
        }
    }

    private void scrollMyListViewToBottom() {
        listViewSMS.post(new Runnable() {
            @Override
            public void run() {
                listViewSMS.setSelection(adapter.getCount() - 1);
            }
        });
    }

}
