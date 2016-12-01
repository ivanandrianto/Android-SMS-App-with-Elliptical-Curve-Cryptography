package com.chibi48.sms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DecryptActivity extends AppCompatActivity {

    private String messageBody;
    private TextView textViewBody;
    private TextView textViewDecryptedBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        textViewBody = (TextView) findViewById(R.id.message_body);
        textViewDecryptedBody = (TextView) findViewById(R.id.decrypted_message_body);

        messageBody = getIntent().getExtras().get("MESSAGE_BODY").toString();
        textViewBody.setText(messageBody);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Messsage");
    }

    public void decrypt(String messageBody) {
        String decryptedText = "";

        if (decryptedText.lastIndexOf("---") != -1) { // REGEX OF HERE
            getSignature();
        }

        textViewDecryptedBody.setText(decryptedText);
    }

    public void getSignature() {

    }

}
