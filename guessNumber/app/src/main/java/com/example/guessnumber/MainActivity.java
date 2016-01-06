package com.example.guessnumber;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends Activity {
    private Button b1;
    private Button b2;


    protected void setButton()
    {
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
    }//setButton
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButton();
        addListener();
    }
    /* button */

    protected void addListener(){
        b1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //開啟Establish Activity
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Establish.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        b2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //開啟Connection Activity
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Connection.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }
}
