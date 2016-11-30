package com.chibi48.sms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewMessageActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText content;
    private ImageButton sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        setTitle("New Message");

        phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        content = (EditText) findViewById(R.id.message_text);
        sendButton = (ImageButton) findViewById(R.id.imageButtonSend);

    }

    public void sendSMS(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSend:
                String to = phoneNumber.getText().toString();
                String msg = content.getText().toString();
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(to, null, msg, null, null);
                break;
        }
    }

}
