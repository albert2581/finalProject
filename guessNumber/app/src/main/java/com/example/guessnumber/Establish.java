package com.example.guessnumber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by 信宇 on 2015/12/16.
 */
public class Establish extends Activity implements SocketConnect.DataCallback {
    AuthTest a = AuthTest.getInstance();
    /* port */
    int port=7777;
    DatagramPacket sendPkg;
    DatagramSocket ds= null;
    int running=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establish);
        broadCast();
        SocketConnect.getInstance().setDataCallback(this);
    }
    public void broadCast(){
        Thread background = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running==0){
                    if (a.getAsk()==0) {
                        getIp myip = new getIp();
                        InetAddress inet;
                        try {
                            ds = new DatagramSocket();
                            inet = InetAddress.getByName(myip.brocastIp(myip.getLocalIpAddress()));
                            Log.d("mineip", myip.getLocalIpAddress());
                            Log.d("brocast", myip.brocastIp(myip.getLocalIpAddress()));
                            JSONObject jsonRoot = new JSONObject();
                            try {
                                jsonRoot.put("ip", myip.getLocalIpAddress());
                                sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                        jsonRoot.toString().getBytes().length, inet, port);
                                ds.send(sendPkg);
                                ds.close();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } catch (UnknownHostException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (a.getAsk()==1)
                    {running=1;
                    }
                }
                InetAddress inet;
                try {
                    ds = new DatagramSocket();
                    inet = InetAddress.getByName(a.getIp());
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        jsonRoot.put("start", 1);
                        sendPkg = new DatagramPacket(jsonRoot.toString().getBytes(),
                                jsonRoot.toString().getBytes().length, inet, port);
                        ds.send(sendPkg);
                        ds.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (UnknownHostException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                //開啟Establish Activity
                connection();
            }
        });
        background.start();
    }
    public void connection(){
        Intent intent = new Intent();
        Bundle sele = new Bundle();
        sele.putInt("select",1);
        intent.putExtras(sele);
        intent.setClass(Establish.this, game.class);
        startActivity(intent);
        Establish.this.finish();
    }

    @Override
    public void onDataReceive(String data) {
        a.rec_handler(data);
    }
}
