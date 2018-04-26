package priv.valueyouth.rhymemusic.service;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import priv.valueyouth.rhymemusic.application.MusicApplication;
import priv.valueyouth.rhymemusic.bean.AudioBean;
import priv.valueyouth.rhymemusic.util.HttpCallbackListener;
import priv.valueyouth.rhymemusic.util.OnlineAudioUtil;
import priv.valueyouth.rhymemusic.util.downLoadFromUrl;

public class DownloadService {
    private String url;
    private static String songid;
    private static String downloadpath = "/storage/sdcard/DCIM/SharedFolder/";

    private static final String TAG = "RhymeMusic";
    private static final String SUB = "[OnlineMusicFragment]#";
    private MusicService.MusicBinder musicBinder;

    public void setsongid(String onlinegetsongid){
        songid = onlinegetsongid;
    }
    private void maindownload(String songname,MusicApplication application,View view,OnlineAudioUtil audioUtil){
        Log.d(TAG, SUB + "onItemClick1" + songname);
        try {
            audioUtil.songidGetThreadstart(songname, new HttpCallbackListener() {
                @Override
                public void onFinish(String result) {
                    Log.d("DEBUG","onFinish result: "+result);
                    String songs = result.substring(result.indexOf("songs") - 2, result.length() - 4);
                    String ssongid = songs.substring(songs.indexOf("id") + 4, songs.indexOf("pst") - 2);
                    setsongid(ssongid);
                    Log.d("INFO", "onlinegetsongid" + songid);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
            Log.d(TAG, "onItemClicksongid: " + songid.toString());
            audioUtil.DownurlGetThreadstart(songid.toString(), new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Log.d("INFO", "res" + response);
                    url = response.substring(response.indexOf("url") + 6, response.indexOf("br") - 3);
                    url=url.replace("\\","");
                    Log.d("INFO", "downloadurl" + url);

                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }
        Log.d("INFO","URL:"+url);
        boolean downloadsfinished=false;
        if (url!=null) {

            downLoadFromUrl download = new downLoadFromUrl();
            String filename = songname+".mp3";
            File f=new File(downloadpath+filename);
            if (!f.exists()) {
                try {
                    downloadsfinished=download.downLoad(url, filename, downloadpath);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ERROR", "download failed");
                }

                Log.d("INFO", "filepath " + url + filename + "\n" + downloadpath);
            }
            else if (f.getTotalSpace()>0) downloadsfinished=true;
            if (downloadsfinished) {
                musicBinder = application.getMusicBinder();
                String SongPath = downloadpath + filename;
                Log.d("INFO", "Songpath" + SongPath);
                musicBinder.startPlay(SongPath,downloadsfinished);
            }
            Snackbar.make(view, "歌曲正在缓冲，请耐心等待！", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Log.d("INFO","playerror");
            Snackbar.make(view, "playerror", Snackbar.LENGTH_SHORT).show();
        }
    }
    public void downloadfromlist(List<AudioBean> audioList,AdapterView<?> parent, View view, int position, long id,MusicApplication application){
        OnlineAudioUtil audioUtil = new OnlineAudioUtil();
        String songname = audioList.get(position).getSongname();
        maindownload(songname,application,view,audioUtil);
    }
    public void downloadbyname(View view,MusicApplication application,String query){
        OnlineAudioUtil audioUtil = new OnlineAudioUtil();
        maindownload(query,application,view,audioUtil);
    }
}
