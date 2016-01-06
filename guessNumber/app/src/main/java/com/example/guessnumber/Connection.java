package com.example.guessnumber;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Connection extends Activity implements SocketConnect.DataCallback {
    AuthTest a = AuthTest.getInstance();
    int port=7777;	//send

    DatagramPacket sendPkg;
    DatagramSocket ds= null;

    private Button b1;
    private ListView L1;


    /* 記錄未配對過機上盒之名稱 */
    String ip_Unpaired="";
    /* 為紀錄未配對之機上盒(預設最多一次接收三台) */
    String check_list[]=new String [3];
    /* 使L1開始顯示接收訊息 */
    int startL1=0;


    /* list view之宣告 */
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;

    protected void setButton()
    {
        b1=(Button)findViewById(R.id.button);
    }//setButton
    protected void setListView()
    {
        L1=(ListView)findViewById(R.id.listView);
    }//setListView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        setButton();
        setListView();
        addListener();
        list();
        SocketConnect.getInstance().setDataCallback(this);
    }
    protected void addListener(){
        b1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                items.clear();
                Log.d("sb1", "start srech");
                //並顯示於ListView1上
                for (int i = 0; i < 3; i++) {
                    check_list[i] = "";                                                //預設check_list為空
                }
                startL1 = 1;                                                            //直到按下此按鈕 ListView1才開始顯示
                pRecvBcast();
            }
        });
    }
    //---------------list view------------------------------------------
    public void list(){
        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);

        L1.setAdapter(adapter);

		/* 未配對過的機上盒選單 */
        L1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "你選擇的是" + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                ask_connect(parent.getItemAtPosition(position).toString());
                game(a.getStart());
            }
        });
    }


    /*------增加未配對過的電腦名稱於listView1上--------*/
    public void menuAddItem(){
        items.add(ip_Unpaired.toString());
        L1.setAdapter(adapter);
    }

    public void pRecvBcast(){
        ip_Unpaired=a.getIp();
        if((check_list[0]=="")&&(ip_Unpaired!="")&&(startL1==1))
        {
            check_list[0]=ip_Unpaired;
            check_list[1]=ip_Unpaired;
            check_list[2]=ip_Unpaired;
            menuAddItem();
        }
        else if((check_list[0]!=check_list[1])&&(ip_Unpaired!="")&&(startL1==1))
        {
            check_list[1]=ip_Unpaired;
            check_list[2]=ip_Unpaired;
            menuAddItem();
        }
        else if((check_list[0]!=check_list[1])&& (check_list[1]!=check_list[2]&&
                (check_list[0]!=check_list[2]))&&(ip_Unpaired!="")&&(startL1==1))
        {
            check_list[2]=ip_Unpaired;
            menuAddItem();
        }
    }
    //-------------要求連線----------------------------------------
    public void ask_connect(final String ask_pair){
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    ds = new DatagramSocket();
                    getIp ip =new getIp();
                    InetAddress inet = InetAddress.getByName(ask_pair);
                    JSONObject jsonRoot = new JSONObject();
                    try {
                        jsonRoot.put("ip", ip.getLocalIpAddress());
                        jsonRoot.put("ask", 1);                                                //要求連線
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
    public void game(int s){
        if (s==1){
            Intent intent = new Intent();
            Bundle sele = new Bundle();
            sele.putInt("select",0);
            intent.putExtras(sele);
            intent.setClass(Connection.this, game.class);
            startActivity(intent);
            Connection.this.finish();
        }

    }
    @Override
    public void onDataReceive(String data) {
        a.rec_handler(data);
    }
}
