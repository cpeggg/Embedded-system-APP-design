package priv.valueyouth.rhymemusic.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import priv.valueyouth.rhymemusic.R;
import priv.valueyouth.rhymemusic.bean.EmotionBean;



public class MotionAnalysis extends AppCompatActivity
{

    private SurfaceView sv_takephoto;
    private Button b_OK;
    private Button b_qh;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotion_analysis);
        //获得SurfaceView的ID
        sv_takephoto =(SurfaceView) findViewById(R.id.sv_takephoto);
        //获得拍照的ID
        b_OK =(Button)findViewById(R.id.b_OK);
        //获得切换镜头的ID
        b_qh =(Button)findViewById(R.id.b_qh);

        sv_takephoto.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //打开相机
                camera = Camera.open();
                //给相机设置参数
                Camera.Parameters parameters= camera.getParameters();
                //设置保存的格式
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.set("jpeg-quality",85);
                camera.setParameters(parameters);

                //将画面展示到SurfaceView中
                try {
                    camera.setPreviewDisplay(sv_takephoto.getHolder());
                    //开启预览
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

        //拍照的点击事件
        b_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拍照
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        //将字节数组转成图片
                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        String filename=System.currentTimeMillis()+".jpg";
                        String picpath="/mnt/sdcard/DCIM/"+filename;
                        try {
                            FileOutputStream fos=new FileOutputStream(picpath);
                            bitmap.compress(Bitmap.CompressFormat.PNG,85,fos);
                            camera.stopPreview();
                            camera.startPreview();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //--------------------
                        //upload the picture
                        upload_picture(picpath,filename);
                    }
                });

            }
            @SuppressLint("StaticFieldLeak")
            private void upload_picture(final String picpath,final String filename) {
                new AsyncTask<String, Void, Object>() {

                    //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
                    // 后台的计算结果将通过该方法传递到UI 线程，并且在界面上展示给用户.
                    protected void onPostExecute(Object result) {
                        super.onPostExecute(result);

                        Gson gson = new Gson();
                        Log.d("INFO","return value:\n"+result);
                        String transfer=result.toString();
                        transfer = transfer.substring(transfer.indexOf("emotion")+9,transfer.length()-4);

                        Log.d("INFO","return transfer:\n"+transfer);
                        EmotionBean emotionBean = gson.fromJson(transfer, EmotionBean.class);
                        result=emotionBean.getEmotion();

                        String tips = "情绪识别结果："+result;
                        Toast.makeText(MotionAnalysis.this, tips, Toast.LENGTH_SHORT).show();
                        return ;
                    }

                    //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
                    protected Object doInBackground(String... params) {
                        String result = uploadpic(picpath,filename);
                        return result;
                    }

                }.execute();

            }

        });

    }
    private String uploadpic(String picpath,String filename){

        Log.d("INFO","upload the picture");
        //--------------------

        HttpURLConnection conn = null;

        /// boundary就是request头和上传文件内容的分隔符(可自定义任意一组字符串)
        String BOUNDARY = "******";
        // 用来标识payLoad+文件流的起始位置和终止位置(相当于一个协议,告诉你从哪开始,从哪结束)
        String preFix = ("\r\n--" + BOUNDARY + "--\r\n");

        try {
            // (HttpConst.uploadImage 上传到服务器的地址

            URL url = new URL("https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect?returnFaceId=false&returnFaceLandmarks=false&returnFaceAttributes=emotion");
            conn = (HttpsURLConnection) url.openConnection();
            Log.d("INFO","Connection built.");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方法
            conn.setRequestMethod("POST");
            // 设置header
            conn.setRequestProperty("Host","westcentralus.api.cognitive.microsoft.com");
            //conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Content-Type","application/octet-stream");
            conn.setRequestProperty("Ocp-Apim-Subscription-Key","154ff89e136d41cd80d7ad5d3fe9eae5");
            // 获取写输入流
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 获取上传文件
            File file = new File(picpath);
            Log.d("INFO","FILE got.");

            // 获取文件流
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream inputStream = new DataInputStream(fileInputStream);

            // 每次上传文件的大小(文件会被拆成几份上传)
            int picbytes;
            // 每次上传的大小
            byte[] bufferOut = new byte[1024];
            // 上传文件

            Log.d("INFO","Start to upload files");
            while ((picbytes = inputStream.read(bufferOut)) != -1) {
                // 上传文件(一份)
                out.write(bufferOut, 0, picbytes);
            }
            Log.d("INFO","Ready to close inputStream");

            // 关闭文件流
            inputStream.close();

            // 至此上传代码完毕

            // 总结上传数据的流程：preFix + payLoad(标识服务器表单接收文件的格式) + 文件(以流的形式) + preFix
            // 文本与图片的不同,仅仅只在payLoad那一处的后缀的不同而已。

            // 输出所有数据到服务器
            out.flush();

            // 关闭网络输出流
            out.close();
            Log.d("INFO","GET request from server.");
            // 重新构造一个StringBuffer,用来存放从服务器获取到的数据
            StringBuffer strBuf = new StringBuffer();


            // 打开输入流 , 读取服务器返回的数据
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            String line;

            // 一行一行的读取服务器返回的数据
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }

            // 关闭输入流
            reader.close();

            Log.d("INFO","UPLOAD SUCCESS");

            if (conn != null) {
                conn.disconnect();
            }

            // 打印服务器返回的数据logD("上传成功:"+strBuf.toString());
            return strBuf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}