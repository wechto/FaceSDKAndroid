package com.baidu.idl.face.example;

//import com.baidu.aip.face.R;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.idl.face.example.widget.DefaultDialog;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.liu.InfoBack;
import com.ljx.fmfordoor.R;
import com.ljx.shopbox.ClientThread;
import com.ljx.shopbox.Up;
import com.ljx.speak.MyService;

import java.io.File;

import static com.ljx.shopbox.MainActivity.cVibrator;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerCustomAdapter mCustomAdapter;
    private Dialog mDefaultDialog;

    //my
    ClientThread clientThread = null;
    Handler revHandler = null;
    File dir =null;

//    private int[] mDataset = new int[]{
//            R.string.main_item_face_live,
//            R.string.main_item_face_detect
//    };
    private int[] mDataset = new int[]{
            R.string.main_item_face_live
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 根据需求添加活体动作
        ExampleApplication.livenessList.clear();
        ExampleApplication.livenessList.add(LivenessTypeEnum.Eye);
        ExampleApplication.livenessList.add(LivenessTypeEnum.Mouth);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadUp);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadDown);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadLeft);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadRight);
        ExampleApplication.livenessList.add(LivenessTypeEnum.HeadLeftOrRight);

        mLayoutManager = new LinearLayoutManager(this);
        mCustomAdapter = new RecyclerCustomAdapter(mDataset);
        this.findViewById(R.id.main_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startItemActivity(SettingsActivity.class);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mCustomAdapter);

        requestPermissions(99, Manifest.permission.CAMERA);
        requestPermissions(99, Manifest.permission.RECORD_AUDIO);

        initLib();

        initMY();

        initService();
    }

    private void initService() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},4);
//        Toast.makeText(this,"wakeup",Toast.LENGTH_LONG).show();
//        new MyWakeUp(this).start();
        startService(new Intent(this, MyService.class));
    }

    private void initMY() {
        this.revHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                String info =  ((InfoBack)(msg.obj)).getState();
                showInfoBackDialog(info);
                Toast.makeText(MainActivity.this,info,Toast.LENGTH_LONG).show();
            }
        };
        this.clientThread = new ClientThread(revHandler);
        new Thread(clientThread).start();
        dir = new File(Environment.getExternalStorageDirectory(), "ljx");
        if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
            dir.mkdirs();
        }
    }

    // 简单消息提示框
//    AlertDialog alertDialogShowInfoBack = null
//    private DefaultDialog alertDialogShowInfoBack = null ;
    private void showInfoBackDialog(String info){
        String showInfo = null;
        switch (info){
            case "open_save":showInfo="正在打开柜门……\n可放置物品";
                cVibrator(MainActivity.this,500);
                break;
            case "open_get":showInfo="正在打开柜门\n请取走您的物品……";
                cVibrator(MainActivity.this,500);
                break;
            case "open_false":showInfo="没有空余的收纳柜……";
                cVibrator(MainActivity.this,300);
                break;
            default:showInfo="error！\n客服热线：151234 85800";
                break;
        }

        if (mDefaultDialog == null) {
            DefaultDialog.Builder builder = new DefaultDialog.Builder(this);
            builder.setTitle("-  -  -  -  -  -  -  -  -  -").
                    setMessage(showInfo).
                    setNegativeButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(mDefaultDialog!=null&&mDefaultDialog.isShowing())
                    mDefaultDialog.dismiss();
            }
        },5000);
    }



    /**
     * 初始化SDK
     */
    private void initLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(this, Config.licenseID, Config.licenseFileName);
//        setFaceConfig();
    }

    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(ExampleApplication.livenessList);
        config.setLivenessRandom(ExampleApplication.isLivenessRandom);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setLivenessRandomCount(2);
        config.setFaceDecodeNumberOfThreads(2);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    private void startItemActivity(Class itemClass) {
        setFaceConfig();
        startActivity(new Intent(this, itemClass));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("image3", Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("send?",false);
        if(!b)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("send?", false);
        editor.apply();
        //原图
        File sdRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdRoot, "test.PNG");
        String filePath = file.getAbsolutePath();
        final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        //利用Bitmap对象创建缩略图
        new Thread(new Runnable() {
            @Override
            public void run() {
//                File file = new File(dir, "up.jpg");
//                FileUtils.compressImage(bitmap,file.getAbsolutePath());
                File sdRoot = Environment.getExternalStorageDirectory();
                File file = new File(sdRoot, "test.PNG");
                Up.up(MainActivity.this,clientThread,file.getAbsolutePath());
            }
        }).start();
    }

    class RecyclerCustomAdapter extends RecyclerView.Adapter<RecyclerCustomAdapter.ViewHolder> {
        final int[] itemDataSet;

        class ViewHolder extends RecyclerView.ViewHolder {

            public final View rv;
            public final TextView tv;

            public ViewHolder(View v) {
                super(v);
                rv = v;
                tv = (TextView) v.findViewById(R.id.item_main_text);
            }
        }

        public RecyclerCustomAdapter(int[] dataSet) {
            itemDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_main, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.tv.setText(itemDataSet[position]);
            viewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (itemDataSet[position]) {
                        case R.string.main_item_face_live:
                            startItemActivity(FaceLivenessExpActivity.class);
                            break;
                        case R.string.main_item_face_detect:
                            startItemActivity(FaceDetectExpActivity.class);
                            break;
                        default:
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemDataSet.length;
        }
    }

    public void requestPermissions(int requestCode, String permission) {
        if (permission != null && permission.length() > 0) {
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    // 检查是否有权限
                    int hasPer = checkSelfPermission(permission);
                    if (hasPer != PackageManager.PERMISSION_GRANTED) {
                        // 是否应该显示权限请求
                        boolean isShould = shouldShowRequestPermissionRationale(permission);
                        requestPermissions(new String[]{permission}, requestCode);
                    }
                } else {

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        boolean flag = false;
        for (int i = 0; i < permissions.length; i++) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                flag = true;
            }
        }
        if (!flag) {
            requestPermissions(99, Manifest.permission.CAMERA);
        }
    }

    protected void showMessageDialog(String title, String message) {
        if (mDefaultDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    MainActivity.this.finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }
}
