package com.ljx.shopbox;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.liu.Info;
import com.liu.InfoBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ClientThread implements Runnable{

    public static Socket socket = null;
    //TODO 端口和IP地址
    final String HOST = "182.254.226.107";
    final int PORT = 30011;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    //发送消息
    public Handler handler = null;
    //接收消息
    public Handler revHandler = null;

    private String content = "";
    private BufferedReader in = null;


    public ClientThread(Handler revHandler) {
        super();
        this.revHandler = revHandler;
    }

    @Override
    public void run() {
        try {
            try {
                socket = new Socket(HOST,PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            oos = new ObjectOutputStream(socket.getOutputStream());
            //TODO 判断网络状态
//            Community_Main.isNetConnection = true;
            if (revHandler != null) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                try {
                                    ois = new ObjectInputStream(socket.getInputStream());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                InfoBack infoBack = readFromServer();
                                Message msg = new Message();
                                msg.obj = infoBack;
                                revHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            Looper.prepare();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        if (oos == null)
                            Log.d("liu", "oos  =  null");
                        oos.writeObject((Info)msg.obj);
//                            oos.writeObject(null);
                        oos.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Looper.loop();
        } catch (SocketTimeoutException e) {
            System.out.print("网络连接超时");
//            Toast.makeText(MainActivity.this,"网络连接超时",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            try {
                //TODO 判断网络状态
//                Community_Main.isNetConnection = false;
                if (socket != null)
                    socket.close();
                Log.d("liu", "----------socket.close()--------------------------");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public Handler getHandler() {
        return this.handler;
    }

    synchronized private InfoBack readFromServer() {
        try {
            return (InfoBack) ois.readObject();
        }
        // 如果捕捉到异常，表明该Socket对应的客户端已经关闭
        catch (Exception e) {
            System.out.println("异常，呵呵了.");
            e.printStackTrace();
        }
        return null;
    }

}
