package com.example.guessnumber;

/**
 * Created by 信宇 on 2015/12/16.
 */

import org.json.JSONException;


import android.os.Environment;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class AuthTest {
    int ask=0,start=0,rNumber=0,gNumber=0,status=0,iVar=0,jVar=0,k=1,t=0;
    String ip="",rec_ip="";
    private static AuthTest authTest = null;
    getIp myip =new getIp();
    private AuthTest(){

    }

    public static synchronized AuthTest getInstance(){
        if (authTest == null){
            authTest = new AuthTest();
        }
        return authTest;
    }

    public String getIp()                                                                        //回傳未配對過之ip
    {
        return ip;
    }
    public int getrNumber(){
        return rNumber;
    }
    public int getgNumber(){
        return gNumber;
    }
    public int getAsk()
    {
        return ask;
    }
    public int getStart()
    {
        return start;
    }
    public int getiVar(){
        return iVar;
    }
    public int getjVar(){
        return jVar;
    }
    public int getK(){
        return k;
    }
    public int getT(){return t;}
    public void rec_handler(String msgString){
        Log.e("Message", msgString);
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            rec_ip = jsonObject.getString("ip");
            if (rec_ip.equals(myip.getLocalIpAddress())){
            }
            else {
                ip=rec_ip;
                Log.d("Message",ip);
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            ask= jsonObject.getInt("ask");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            start= jsonObject.getInt("start");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            rNumber= jsonObject.getInt("rNumber");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            gNumber= jsonObject.getInt("gNumber");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            iVar= jsonObject.getInt("iVar");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            jVar= jsonObject.getInt("jVar");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            k= jsonObject.getInt("k");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(msgString);
            t= jsonObject.getInt("time");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}

