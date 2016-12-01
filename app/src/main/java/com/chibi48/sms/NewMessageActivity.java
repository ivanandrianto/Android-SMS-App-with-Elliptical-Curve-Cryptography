package com.chibi48.sms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.chibi48.sms.model.DSS;

public class NewMessageActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText content;
    private ImageButton sendButton;
    private DSS dss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        setTitle("New Message");

        phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        content = (EditText) findViewById(R.id.message_body);
        sendButton = (ImageButton) findViewById(R.id.button_send);

    }

    public void sendSMS(View v) {
        switch (v.getId()) {
            case R.id.button_send:
                String to = phoneNumber.getText().toString();
                String msg = content.getText().toString();
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(to, null, msg, null, null);
                break;
        }
    }

}
