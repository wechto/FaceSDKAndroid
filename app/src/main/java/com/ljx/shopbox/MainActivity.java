package com.ljx.shopbox;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;


public class MainActivity {

    private static final int MSG_TAKE_PHOTO = 1;
    public static File currentImageFile = null;
    File dir = null;

    Button button = null;
    ImageView imageView = null;

    ClientThread clientThread = null;
    Handler revHandler = null;

    Context context=null;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        myPermission();
////        FileUtils.init();
//        this.button = (Button) findViewById(R.id.button);
//        this.imageView = (ImageView) findViewById(R.id.image);
//        this.button.setOnClickListener(this);
//        this.context=context;
//        this.revHandler = new Handler(){
//            @Override
//            public void handleMessage(Message msg){
//                String info =  ((InfoBack)(msg.obj)).getState();
//                if(info.equals("opened")){
//                    cVibrator(MainActivity.this,500);
//                }else{
//                    cVibrator(MainActivity.this,200);
//                }
//                Toast.makeText(MainActivity.this,info,Toast.LENGTH_LONG).show();
//
//            }
//        };
//        this.clientThread = new ClientThread(revHandler);
//        new Thread(clientThread).start();
//    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.button:
//                try {
//                    takePhoto();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            default:break;
//        }
//    }

//    private void takePhoto() throws Exception {
//        //调用系统相机
//        //在sd下创建文件夹myimage；Environment.getExternalStorageDirectory()得到SD卡路径文件
//        dir = new File(Environment.getExternalStorageDirectory(), "ljx");
//        if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
//            dir.mkdirs();
//        }
//        //设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        String filename = df.format(new Date());
//        currentImageFile = new File(dir, filename + ".jpg");
//        if (!currentImageFile.exists()) {
//            currentImageFile.createNewFile();
//        }
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUriForFile(this,currentImageFile));
//        startActivityForResult(openCameraIntent, MSG_TAKE_PHOTO);
//    }

//    void myPermission(){
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.ACCESS_NETWORK_STATE,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE},6);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == MSG_TAKE_PHOTO) {
//            String sdStatus = Environment.getExternalStorageState();
//            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
//                return;
//            }
//            //原图
//            String filePath = currentImageFile.getAbsolutePath();
//            final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//            //利用Bitmap对象创建缩略图
//            Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 250, 250);
//            imageView.setImageBitmap(showbitmap);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    File file = new File(dir, "up.jpg");
//                    FileUtils.compressImage(bitmap,file.getAbsolutePath());
//                    Up.up(MainActivity.this,clientThread,file.getAbsolutePath());
//                }
//            }).start();
//        }
//    }

    public static void cVibrator(Activity activity,int milliseconds){
        Vibrator vib = (Vibrator)activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

}
