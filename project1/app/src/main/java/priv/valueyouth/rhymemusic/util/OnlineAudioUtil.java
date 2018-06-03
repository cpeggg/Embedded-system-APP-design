package priv.valueyouth.rhymemusic.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.show.api.ShowApiRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import priv.valueyouth.rhymemusic.bean.AudioBean;
import priv.valueyouth.rhymemusic.bean.JsonBean;

import static android.net.Uri.encode;

/**
 * 发送在线音乐请求，并解析出来，放在一个列表中。
 * Created by Idea on 2016/6/23.
 */
public class OnlineAudioUtil
{
    private static final String TAG = "RhymeMusic";
    private static final String SUB = "[OnlineAudioUtil]#";

    public static String jsonData = null;

    /**
     * 接收在线服务器发过来的数据。
     * @param id 榜单id
     */
    public List<AudioBean> handleJsonData(final String id)
    {
        Log.d(TAG, SUB + "startThread");

        List<AudioBean> audioBeen = new ArrayList<>();
        String appId = "62988";
        String secret = "098222d182cd45d3b8ca5a4177bb02f6";
        String url = "http://route.showapi.com/213-4";
        ShowApiRequest request = new ShowApiRequest(url, appId,secret);


        request.addTextPara("topid", id); // 设置参数
        jsonData = request.post(); // 得到json数据

        Log.d(TAG, SUB + "123234324" +jsonData);

        Gson gson = new Gson();
        JsonBean jsonBean = gson.fromJson(jsonData, JsonBean.class);
        audioBeen = jsonBean.getShowapi_res_body().getPagebean().getSonglist();

        Log.d(TAG, SUB + audioBeen.size());
        return audioBeen;
    }
    public void songidGetThreadstart(final String songname,final HttpCallbackListener listener) throws InterruptedException {
        Thread thr = new Thread() {
            @Override
            public void run()
            {
                HttpURLConnection conn = null;
                try {

                    // 根据歌名查id
                    URL url = new URL("https://api.imjad.cn/cloudmusic/?type=search&search_type=1&s="+encode(songname,"utf-8"));
                    Log.d(TAG, "songname: "+url);
                    conn = (HttpsURLConnection) url.openConnection();
                    Log.d("INFO", "Connection built1.");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(30000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    // 请求方法
                    conn.setRequestMethod("POST");
                    // header
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");


                    StringBuffer strBuf = new StringBuffer();

                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));
                    String line;
                    // 读取服务器返回的数据
                    while ((line = reader.readLine()) != null) {
                        strBuf.append(line).append("\n");
                    }
                    Log.d(TAG, "strbuf:"+strBuf.toString());
                    // 关闭输入流
                    reader.close();




                    //提取返回结果第一个匹配歌曲的id
                    if (listener != null){
                        listener.onFinish(strBuf.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null){
                        listener.onError(e);
                    }
                } finally {
                    if (conn != null){
                        conn.disconnect();
                    }
                }

            }


        };
        thr.start();
        thr.join();
        Log.d(TAG, SUB + "线程1执行结束。");

    }
    public void DownurlGetThreadstart(final String Musicid,final HttpCallbackListener listener) throws InterruptedException {
        Thread thr = new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    //根据id查url

                    URL newurl = new URL("https://api.imjad.cn/cloudmusic/?type=song" + "&id=" + Musicid + "&br=128000");
                    conn = (HttpsURLConnection) newurl.openConnection();
                    Log.d("INFO", "Connection built2." + newurl);
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(30000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                    // 读取服务器返回的数据
                    StringBuffer strBuf = new StringBuffer();


                    BufferedReader reader2 = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    String line;

                    while ((line = reader2.readLine()) != null) {
                        strBuf.append(line).append("\n");
                    }
                    Log.d("INFO", "wwwwwwwwwwwwwwwwwwwwww" + strBuf.toString());
                    // 关闭输入流
                    reader2.close();

                    if (listener != null) {
                        listener.onFinish(strBuf.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        };
        thr.start();
        thr.join();
        Log.d(TAG, SUB + "线程2执行结束。");
    }

}
