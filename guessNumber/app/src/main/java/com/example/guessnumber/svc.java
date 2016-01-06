package com.example.guessnumber;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by 信宇 on 2016/1/4.
 */
public class svc extends Service{
    int time=0;
    AuthTest a = AuthTest.getInstance();
    Intent intent = null;
    MyThread t_Thread;
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        t_Thread.flag = false;
        t_Thread.interrupt();
        t_Thread=null;
    }
    @Override
    public void onStart(Intent intent,int startId){
        t_Thread = new MyThread();
        t_Thread.start();
    }

    class MyThread extends Thread{
        boolean flag = true;
        int sec=30;
        @Override
        public void run(){
            while (flag){
                Intent i = new Intent("a filter string");
                i.putExtra("background_service", a.getK());
                i.putExtra("secound", sec);
                sendBroadcast(i);
                time=a.getT();

                if (time==1)
                    sec=30;
                else {
                    if (sec==0){
                        sec=31;
                    }else {
                        sec--;
                    }
                }

                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };
}
