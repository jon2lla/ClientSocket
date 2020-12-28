package com.example.clientsocket;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import main.Datos;

public class IOListenerClt implements Runnable{

    private Socket s;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Datos datos;
    private String ip = "192.168.43.49";

    public IOListenerClt(Datos datos){
        this.datos = datos;
    }


    public Datos getDatos(){
        return datos;
    }

    @Override
    public void run() {
        try {
            s = new Socket(ip, 44444);

            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());

            SocketAddress socketAddress = s.getRemoteSocketAddress();

            if (socketAddress instanceof InetSocketAddress) {
                InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
                if (inetAddress instanceof Inet4Address)
                    datos.setIp("IPv4: " + inetAddress);
                else if (inetAddress instanceof Inet6Address)
                    datos.setIp("IPv6: " + inetAddress);
                else
                    datos.setIp("Not an IP address.");
            } else {
                datos.setIp("Not an internet protocol socket.");
            }

            oos.writeObject(datos);
            oos.flush();
            datos = (Datos) ois.readObject();

            if(oos != null)
                oos.close();
            if(ois != null)
                ois.close();
            if(s != null)
                s.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}