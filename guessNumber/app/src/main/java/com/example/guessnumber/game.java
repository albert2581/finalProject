package com.example.guessnumber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;

public class game extends AppCompatActivity implements SocketConnect.DataCallback {
    AuthTest a = AuthTest.getInstance();
    int port=7777,p=0;	//send
    DatagramPacket sendPkg;
    DatagramSocket ds= null;

    private TheBroadcasReceiver myBroadcasReceiver;
    private EditText e1;
    private Button b1;
    private TextView t1,t5,t7;
    int randN=0,guessN=0,i=0,j=0,k=0;
    String oldMessage="";
    Bundle sele;
    protected void setButton()
    {
        b1=(Button)findViewById(R.id.button3);
    }//setButton
    protected void setEditText()
    {
        e1=(EditText)findViewById(R.id.editText);
    }//setButton
    protected void setTextView()
    {
        t1=(TextView)findViewById(R.id.textView3);
        t5=(TextView)findViewById(R.id.textView5);
        t7=(TextView)findViewById(R.id.textView7);
    }//setButton
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setButton();
        setEditText();
        setTextView();
        miber();
        sele = getIntent().getExtras();
        if (sele.getInt("select")==1) {
            randN=randomNumber();
            Log.d("rNumber主機", String.valueOf(randN));
        }
        addListener();
        SocketConnect.getInstance().setDataCallback(this);
        IntentFilter intentFilter = new IntentFilter("a filter string");
        myBroadcasReceiver = new TheBroadcasReceiver();
        registerReceiver(myBroadcasReceiver,intentFilter);
    }
    protected void addListener(){
        b1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                send_t();
                if (sele.getInt("select") == 1)
                    j = a.getjVar();
                if (sele.getInt("select") == 0)
                    i = a.getiVar();
                Log.d("VARi", String.valueOf(i));
                Log.d("VARj", String.valueOf(j));
                if (sele.getInt("select") == 0)
                    randN = a.getrNumber();
                if ((i == j) && (sele.getInt("select") == 1)) {
                    guess("別人的猜測", a.getgNumber());
                    guessN = Integer.valueOf(e1.getText().toString());
                    i = i + 1;
                    guess("自己的猜測", guessN);
                    oldMessage = t1.getText().toString();
                    t5.setText("等待");
                    Log.d("runhere", "主");
                } else if ((i == j + 1) && (sele.getInt("select") == 0)) {
                    guess("別人的猜測", a.getgNumber());
                    guessN = Integer.valueOf(e1.getText().toString());
                    j = j + 1;
                    guess("自己的猜測", guessN);
                    Log.d("runhere", "客");
                    oldMessage = t1.getText().toString();
                    t5.setText("等待");
                }
                e1.setText("");
            }
        });
    }
    public int randomNumber(){
        return (int)(Math.random()* 100);
    }
    public void send_msg(final int gNumber){
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    ds = new DatagramSocket();
                    InetAddress inet = InetAddress.getByName(a.getIp());
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        if ((sele.getInt("select")==1))
                            jsonRoot.put("rNumber", randN);
                        jsonRoot.put("gNumber", gNumber);                                                //要求連線
                        jsonRoot.put("iVar", i);
                        jsonRoot.put("jVar", j);
                        jsonRoot.put("k", 0);
                        Log.d("VARi_Send", String.valueOf(i));
                        Log.d("VARj_Send", String.valueOf(j));
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length, inet, port);
                        ds.send(sendPkg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable t) {
                    // just end the background thread
                }
            }
        });
        background.start();
    }
    public void send_k(final int k){
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    ds = new DatagramSocket();
                    InetAddress inet = InetAddress.getByName(a.getIp());
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        jsonRoot.put("k", String.valueOf(k));
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length, inet, port);
                        ds.send(sendPkg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable t) {
                    // just end the background thread
                }
            }
        });
        background.start();
    }
    public void send_t(){
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    ds = new DatagramSocket();
                    InetAddress inet = InetAddress.getByName(a.getIp());
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        jsonRoot.put("time", String.valueOf(0));
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length, inet, port);
                        ds.send(sendPkg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable t) {
                    // just end the background thread
                }
                try {
                    ds = new DatagramSocket();
                    InetAddress inet = InetAddress.getByName(getLocalIpAddress());
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        jsonRoot.put("time", String.valueOf(1));
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length, inet, port);
                        ds.send(sendPkg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable t) {
                    // just end the background thread
                }

            }
        });
        background.start();
    }
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if(inetAddress instanceof Inet4Address)
                        {
                            return ((Inet4Address)inetAddress).getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "";
    }
    public void guess(String who,int guessN){
        if (guessN==-1){
            oldMessage = t1.getText().toString();
            t1.setText(oldMessage + "\n" + "沒猜----" + who);
            send_msg(guessN);
        }
        else{
            if (guessN > randN ) {
                oldMessage = t1.getText().toString();
                t1.setText(oldMessage + "\n" + String.valueOf(guessN) + "猜錯囉----" + who);
                send_msg(guessN);
            } else if (guessN < randN) {
                oldMessage = t1.getText().toString();
                t1.setText(oldMessage + "\n" + String.valueOf(guessN) + "猜錯囉----" + who);
                send_msg(guessN);
            } else if (guessN == randN) {
                oldMessage = t1.getText().toString();
                t1.setText(oldMessage + "\n" + String.valueOf(guessN) + "答對囉!!----" + who);
                send_msg(guessN);
            } else {
                t1.setText("Error");
            }
        }
    }
    public void onDataReceive(String data) {
        a.rec_handler(data);
    }

    public void miber(){
        Intent i = new Intent();
        i.setClass(game.this, svc.class);
        startService(i);
    }
    public class TheBroadcasReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){
            Bundle myBundle = intent.getExtras();
            int myInt = myBundle.getInt("background_service");
            int sec = myBundle.getInt("secound");
            if (sec==31){
                t7.setText("超時囉");
                send_t();
                if (sele.getInt("select") == 1)
                    j = a.getjVar();
                if (sele.getInt("select") == 0)
                    i = a.getiVar();
                if (sele.getInt("select") == 0)
                    randN = a.getrNumber();
                if ((i == j) && (sele.getInt("select") == 1)) {
                    guess("別人的猜測", a.getgNumber());
                    guessN = Integer.valueOf(-1);
                    i = i + 1;
                    guess("自己的猜測", guessN);
                    oldMessage = t1.getText().toString();
                    t5.setText("等待");
                    Log.d("runhere", "主");
                } else if ((i == j + 1) && (sele.getInt("select") == 0)) {
                    guess("別人的猜測", a.getgNumber());
                    guessN = Integer.valueOf(-1);
                    j = j + 1;
                    guess("自己的猜測", guessN);
                    Log.d("runhere", "客");
                    oldMessage = t1.getText().toString();
                    t5.setText("等待");
                }
            }else{
                t7.setText("剩下" + String.valueOf(sec));
            }
                    k=myInt;
                    if (sele.getInt("select")==1)
                        j=a.getjVar();
                    if (sele.getInt("select")==0)
                        i=a.getiVar();
                    if ((i==j)&&(sele.getInt("select")==1)&&(k==0)){
                        t5.setText("換你囉");
                        p=0;
                        k=1;
                        send_k(k);
                    }
                    else if ((i==j+1)&&(sele.getInt("select")==0)&&(k==0)){
                        t5.setText("換你囉");
                        p=0;
                        k=1;
                        send_k(k);
                    }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myBroadcasReceiver);
        storePrefs();

    }
    private void storePrefs(){
        SharedPreferences setting = getSharedPreferences("PREF_DATA",0);
        setting.edit().putString("NAME","started").commit();
    }
    private boolean restorePrefs(){
        SharedPreferences settings = getSharedPreferences("PREF_DATA",0);
        String pref_name = settings.getString("NAME","");

        if (pref_name.equals("started") == true)
            return true;
        return false;
    }



//    public void run(){
//        Thread background = new Thread(new Runnable() {
//            public void run() {
//                while (true)
//                {
//                    k=a.getK();

////                    Log.d("stillrun",String.valueOf(k));
//                }
//            }
//        });
//        background.start();
//    }
}
