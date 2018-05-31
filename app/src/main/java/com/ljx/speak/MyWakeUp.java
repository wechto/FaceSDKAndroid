package com.ljx.speak;

import android.content.Context;
import android.content.Intent;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.widget.Toast;

import com.baidu.idl.face.example.FaceLivenessExpActivity;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/9.
 */

public class MyWakeUp {

    public static final String TAG = MyWakeUp.class.getSimpleName();
    private EventManager mWpEventManager;
    public static Context context;


    public MyWakeUp(Context context) {
        MyWakeUp.context = context;
        mWpEventManager = EventManagerFactory.create(context, "wp");
        mWpEventManager.registerListener(new MyEventListener());
    }


    public void start() {
        HashMap<String, String> params = new HashMap<String, String>();
        // ??????????, ????????? http://yuyin.baidu.com/wake#m4 ???????????
        params.put("kws-file", "assets:///WakeUp.bin");
        mWpEventManager.send("wp.start", new JSONObject(params).toString(), null, 0, 0);
    }


    public void stop() {
        mWpEventManager.send("wp.stop", null, null, 0, 0);
    }
    public class MyEventListener implements com.baidu.speech.EventListener {

        public void onEvent(String name, String params, byte[] data, int offset, int length) {

            try {
//                JSONObject json = new JSONObject(params);
                if ("wp.data".equals(name)) {
//                    Toast.makeText(MyWakeUp.context,"Oooooook",Toast.LENGTH_LONG).show();
                    MyWakeUp.context.startActivity(new Intent(MyWakeUp.context,FaceLivenessExpActivity.class));
                } else if ("wp.exit".equals(name)) {
                }
            } catch (Exception e) {
                Log.d(TAG, "JSONException");
                Toast.makeText(MyWakeUp.context, "JSONException", Toast.LENGTH_SHORT).show();
                throw new AndroidRuntimeException(e);
            }
        }
    }
}
