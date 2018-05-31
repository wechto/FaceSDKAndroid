package com.ljx.shopbox;

import android.app.Activity;
import android.os.Message;

import com.liu.Info;
import com.ljx.utils.Base64Util;
import com.ljx.utils.FileUtil;

import java.io.IOException;

/**
 * Created by Administrator on 2018/2/3.
 */

public class Up {

    public static void up(Activity context,ClientThread clientThread,String Filepath){
        Message msg = new Message();
        String image = null;
        try {
            image = Base64Util.encode(FileUtil.readFileByBytes(Filepath));
            msg.obj = new Info(image);
            clientThread.handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
