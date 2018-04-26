package priv.valueyouth.rhymemusic.util;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class downLoadFromUrl {
    private boolean downloadflag=false;
    public boolean downLoad(String urlStr,String fileName,String savePath){
        downloadSong(urlStr,fileName,savePath);
        return downloadflag;
    }

    @SuppressLint("StaticFieldLeak")
    private void downloadSong(final String urlStr,final String fileName,final String savePath){
        new AsyncTask<String, Void, Object>() {
            //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
            // 后台的计算结果将通过该方法传递到UI 线程，并且在界面上展示给用户.
            protected void onPostExecute(Object obj){

                Log.d("INFO","onPostExecute finish.");
                downloadflag=(Boolean)obj;
            }

            //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
            protected Object doInBackground(String... params){
                Log.d("INFO","begin doInBackground.");
                return whatyoushoulddo(urlStr,fileName,savePath);
            }
        }.execute();

    }
    // 从输入流中获取字节数组
    private boolean whatyoushoulddo(String urlStr,String fileName,String savePath){
        try {
            URL url = new URL(urlStr);
            Log.d("INFO", "info:" + url + " download start");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            File saveDir = new File(savePath);
            Log.d("INFO", "SAVEDIR" + saveDir.toString());
            if (!saveDir.exists()) {
                saveDir.mkdir();
                Log.d("INFO", "saveDir.mkdir()");
            }
            saveDir=new File(savePath+fileName);
            if (saveDir.exists()){
                if (saveDir.getTotalSpace()<=0) {
                    saveDir.delete();
                    return false;
                }
                Log.d("INFO", "info: file exist.");
                return true;
            }
            Log.d("INFO","FILENAME: "+fileName);
            Log.d("INFO","SAVEPATH+FILENAME: "+savePath+fileName);

            FileOutputStream fs = new FileOutputStream(savePath + fileName);
            InputStream inputStream = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesum = 0;
            int byteread = 0;

            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            Log.d("INFO", "info: download success");

            return true;
        }
        catch (Exception e){
            Log.d("ERROR",e.getMessage());
            return false;
        }
    }
}
