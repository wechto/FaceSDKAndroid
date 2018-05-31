package com.ljx.speak;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/5/9.
 */

public class MyService extends Service {
    MyWakeUp myWakeUp = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.myWakeUp = new MyWakeUp(this);
        myWakeUp.start();
    }
}
