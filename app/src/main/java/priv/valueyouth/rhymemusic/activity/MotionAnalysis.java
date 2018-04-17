package priv.valueyouth.rhymemusic.activity;

import android.graphics.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import priv.valueyouth.rhymemusic.R;
import priv.valueyouth.rhymemusic.application.MusicApplication;
import priv.valueyouth.rhymemusic.util.Audio;
import priv.valueyouth.rhymemusic.util.AudioUtil;


public class MotionAnalysis extends AppCompatActivity {

    private SurfaceView sv_takephoto;
    private Button b_OK;
    private Button b_qh;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
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
                        try {
                            FileOutputStream fos=new FileOutputStream("/mnt/sdcard/DCIM/"+System.currentTimeMillis()+".jpg");
                            bitmap.compress(Bitmap.CompressFormat.PNG,85,fos);
                            camera.stopPreview();
                            camera.startPreview();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }
}