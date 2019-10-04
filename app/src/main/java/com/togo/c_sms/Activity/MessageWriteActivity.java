package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.R;

public class MessageWriteActivity extends AppCompatActivity {
   EditText message_write_edittext;
   TextView message_content,count_caracter,perpagecaracter;
   ImageView sendmessage,closemessagebox;
   String caracterLength = String.valueOf(160);
    RadioGroup  destinataire;
    Boolean sendtocontact,sendtogroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_write);

        message_write_edittext = findViewById(R.id.message_write_edittext);
        sendmessage = findViewById(R.id.sendmessage);
        message_content = findViewById(R.id.message_content);
        closemessagebox = findViewById(R.id.closemessagebox);
        count_caracter = findViewById(R.id.count_caracter);
        perpagecaracter = findViewById(R.id.perpagecaracter);
        destinataire = findViewById(R.id.destinataire);
        closemessagebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageWriteActivity.super.onBackPressed();
                Animatoo.animateSlideDown(MessageWriteActivity.this);
            }
        });
        perpagecaracter.setText(caracterLength);
        message_content.setText("Votre message s'affichera ici");
        count_caracter.setText("0");
        message_write_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                int length = message_write_edittext.length();
                String convert = String.valueOf(length);
                count_caracter.setText(convert);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                message_content.setText(message_write_edittext.getEditableText().toString());
                sendmessage.setVisibility(View.VISIBLE);
            }
        });

     sendmessage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
           if (message_content.getText().toString().isEmpty()){
                 Toast.makeText(MessageWriteActivity.this, "Message vide", Toast.LENGTH_SHORT).show();
             }
            else{
                    Intent inten = new Intent(MessageWriteActivity.this,ChooseReceiverActivity.class);
                    inten.putExtra("message",message_content.getText().toString());
                    inten.putExtra("letter",count_caracter.getText().toString());
                    startActivity(inten);
             }
         }
     });


    }

    public void onBackPressed() {
      if (message_write_edittext.length() != 0){
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setMessage("Votre message  sera perdu ... Continuer ?").setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  MessageWriteActivity.super.onBackPressed();
                  Animatoo.animateSlideDown(MessageWriteActivity.this);
              }
          })
                  .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }
                  });
          AlertDialog alertDialog = builder.create();
          alertDialog.show();
      }else{
          MessageWriteActivity.super.onBackPressed();
          Animatoo.animateSlideDown(MessageWriteActivity.this);
      }
    }

}
