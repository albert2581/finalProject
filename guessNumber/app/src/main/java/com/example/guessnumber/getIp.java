package com.example.guessnumber;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by 信宇 on 2015/12/16.
 */
public class getIp {
    //------------------------獲得自己的IP---------------------------------------------
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
    //    獲得廣播出去的IP
    public String brocastIp(String Ip){
        String[] AfterSplit = Ip.split("[.,\\s]+");
        return AfterSplit[0]+"."+AfterSplit[1]+"."+AfterSplit[2]+".255";
    }
}
