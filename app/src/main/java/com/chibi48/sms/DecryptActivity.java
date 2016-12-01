package com.chibi48.sms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chibi48.sms.model.DSS;
import com.chibi48.sms.model.ECC;

import org.apache.commons.codec.DecoderException;

public class DecryptActivity extends AppCompatActivity {

    private String messageBody;
    private TextView textViewBody;
    private TextView textViewDecryptedBody;
    private TextView textViewSignatureVerification;
    private EditText editTextPublicKey;
    private ImageButton imgButtonDecrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        textViewBody = (TextView) findViewById(R.id.message_body);
        textViewDecryptedBody = (TextView) findViewById(R.id.decrypted_message_body);
        textViewSignatureVerification = (TextView) findViewById(R.id.signature_verification);
        editTextPublicKey = (EditText) findViewById(R.id.public_key);

        messageBody = getIntent().getExtras().get("MESSAGE_BODY").toString();
        textViewBody.setText(messageBody);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Messsage");
    }

    public void decrypt(View v) throws DecoderException {
        switch (v.getId()) {
            case R.id.button_decrypt:
                ECC ecc = new ECC();
                String decryptedText = ecc.decryptString(messageBody);

                int idx = decryptedText.lastIndexOf("\n----");
                if (idx != -1) { // REGEX OF HERE
                    String text = decryptedText.substring(0, idx);
                    int idx2 = text.lastIndexOf("\n----");
                    if (idx2 != -1) {
                        verifySignature(decryptedText);
                    }
                }

                textViewDecryptedBody.setText(decryptedText);
                break;
        }
    }

    public void verifySignature(String decryptedMessage) {
        DSS dss = new DSS();
        if (dss.verifySigning(decryptedMessage, Long.parseLong(editTextPublicKey.getText()
                .toString()))) {
            textViewSignatureVerification.setText("Signature valid");
        } else {
            textViewSignatureVerification.setText("Signature invalid");
        }
    }

}
