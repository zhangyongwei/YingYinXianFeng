package com.atguigu.yingyinxianfeng.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.atguigu.mobileplayer1020.IMusicPlayerService;
import com.atguigu.yingyinxianfeng.R;
import com.atguigu.yingyinxianfeng.activity.SystemAudioPlayerActivity;
import com.atguigu.yingyinxianfeng.bean.MediaItem;
import com.atguigu.yingyinxianfeng.utils.CacheUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * 作用： 播放音乐的服务
 */
public class MusicPlayerService extends Service {

    public static final String OPEN_COMPLETE = "open_complete";
    /**
     * AIDL生成的类
     */
    IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        //把服务当成成员变量
        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();

        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return service.getArtistName();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public void next() throws RemoteException {
            service.next();

        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            service.setPlayMode(mode);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mediaPlayer.isPlaying();
        }

        @Override
        public void seekTo(int postion) throws RemoteException {
            mediaPlayer.seekTo(postion);
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return mediaItem.getData();
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return mediaPlayer.getAudioSessionId();
        }
    };
    private ArrayList<MediaItem> mediaItems;
    /**
     * 音频是否加载完成
     */
    private boolean isLoaded = false;
    private MediaItem mediaItem;
    private int position;
    /**
     * 播放器
     */
    private MediaPlayer mediaPlayer;

    /**
     * 顺序播放
     */
    public static final int REPEATE_NOMAL = 1;

    /**
     * 单曲播放
     */
    public static final int REPEATE_SINGLE = 2;


    /**
     * 全部循环
     */
    public static final int REPEATE_ALL = 3;

    private int playmode = REPEATE_NOMAL;
    private boolean isNext = false;


    /**
     * 返回代理类
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","service=="+this.toString());
        playmode = CacheUtils.getPlaymode(this, "playmode");
        getDataFromLocal();
    }

    /**
     * 子线程中得到音频
     */
    private void getDataFromLocal() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                //初始化集合
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                //sdcard 的视频路径
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//在sdcard显示的视频名称
                        MediaStore.Audio.Media.DURATION,//视频的时长,毫秒
                        MediaStore.Audio.Media.SIZE,//文件大小-byte
                        MediaStore.Audio.Media.DATA,//在sdcard的路径-播放地址
                        MediaStore.Audio.Media.ARTIST//艺术家
                };
                Cursor cusor = resolver.query(uri, objs, null, null, null);
                if (cusor != null) {

                    while (cusor.moveToNext()) {

                        MediaItem mediaItem = new MediaItem();

                        //添加到集合中
                        mediaItems.add(mediaItem);//可以

                        String name = cusor.getString(0);
                        mediaItem.setName(name);
                        long duration = cusor.getLong(1);
                        mediaItem.setDuration(duration);
                        long size = cusor.getLong(2);
                        mediaItem.setSize(size);
                        String data = cusor.getString(3);//播放地址
                        mediaItem.setData(data);
                        String artist = cusor.getString(4);//艺术家
                        mediaItem.setArtist(artist);

                    }

                    cusor.close();
                }


                //音频加载完成
                isLoaded = true;


            }
        }.start();

    }

    /**
     * 根据位置打开一个音频并且播放
     *
     * @param position
     */
    void openAudio(int position) {
        if (mediaItems != null && mediaItems.size() > 0) {
            mediaItem = mediaItems.get(position);
            this.position = position;

            //MediaPlayer
            if (mediaPlayer != null) {
                mediaPlayer.reset();//上一曲重置
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();
            //设置三个监听
            mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
            mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
            mediaPlayer.setOnErrorListener(new MyOnErrorListener());

            //设置播放地址
            try {
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();
                isNext = false;
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (!isLoaded) {
            Toast.makeText(this, "没有加载完成", Toast.LENGTH_SHORT).show();
        }

    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {


        @Override
        public void onCompletion(MediaPlayer mp) {
            isNext = true;
            next();
        }
    }


    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
//            notifyChange(OPEN_COMPLETE);
            //4.发消息-传递数据
            EventBus.getDefault().post(mediaItem);
            start();
        }
    }

    private void notifyChange(String action) {
        Intent intent = new Intent();
        intent.setAction(action);

        //发广播
        sendBroadcast(intent);
    }

    private NotificationManager nm;

    /**
     * 开始播放音频
     */
    void start() {
        mediaPlayer.start();
        //在状态栏创建通知
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, SystemAudioPlayerActivity.class);
        intent.putExtra("notification", true);//标识来自状态栏
        //包含意图
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notificaton = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notificaton = new Notification.Builder(this)
                    //图片
                    .setSmallIcon(R.drawable.notification_music_playing)
                    //标题
                    .setContentTitle("321音乐")
                    //内容
                    .setContentText("正在播放:" + getAudioName())
                    //点击动作，延期意图
                    .setContentIntent(pendingIntent)
                    .build();

            //点击后还存在属性
            notificaton.flags = Notification.FLAG_ONGOING_EVENT;
        }
        nm.notify(1, notificaton);

    }

    /**
     * 暂停
     */
    void pause() {
        mediaPlayer.pause();
        //移除状态栏的通知
        nm.cancel(1);
    }

    /**
     * 得到歌曲的名称
     */
    String getAudioName() {
        if (mediaItem != null) {
            return mediaItem.getName();
        }
        return "";
    }

    /**
     * 得到歌曲演唱者的名字
     */
    String getArtistName() {
        if (mediaItem != null) {
            return mediaItem.getArtist();
        }
        return "";

    }

    /**
     * 得到歌曲的当前播放进度
     */
    int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 得到歌曲的当前总进度
     */
    int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 播放下一首歌曲
     */
    void next() {

        //设置下一曲对应的位置
        setNextPostion();
        //根据对应的位置去播放
        openNextAudio();

    }

    private void openNextAudio() {
        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEATE_NOMAL) {

            if (position <= mediaItems.size() - 1) {
                openAudio(position);
            } else {
                position = mediaItems.size() - 1;
            }

        } else if (playmode == MusicPlayerService.REPEATE_SINGLE) {
            openAudio(position);

        } else if (playmode == MusicPlayerService.REPEATE_ALL) {
            openAudio(position);
        } else {
            if (position <= mediaItems.size() - 1) {
                openAudio(position);
            } else {
                position = mediaItems.size() - 1;
            }
        }
    }

    private void setNextPostion() {
        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEATE_NOMAL) {

            position++;

        } else if (playmode == MusicPlayerService.REPEATE_SINGLE) {
            if(!isNext){
                isNext = false;
                position++;
                if (position > mediaItems.size() - 1) {
                    position = 0;
                }

            }

        } else if (playmode == MusicPlayerService.REPEATE_ALL) {
            position++;
            if (position > mediaItems.size() - 1) {
                position = 0;
            }
        } else {
            position++;
        }
    }

    /**
     * 播放上一首歌曲
     */
    void pre() {
        //设置下一曲对应的位置
        setPrePostion();
        //根据对应的位置去播放
        openPreAudio();
    }

    private void setPrePostion() {
        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEATE_NOMAL) {
            position--;
        } else if (playmode == MusicPlayerService.REPEATE_SINGLE) {
            if(!isNext){
                isNext = false;
                position--;
                if (position < 0) {
                    position = mediaItems.size()-1;
                }

            }

        } else if (playmode == MusicPlayerService.REPEATE_ALL) {
            position--;
            if (position < 0) {
                position = mediaItems.size()-1;
            }
        } else {
            position--;
        }
    }

    private void openPreAudio() {
        int playmode = getPlayMode();

        if (playmode == MusicPlayerService.REPEATE_NOMAL) {

            if (position >= 0) {
                openAudio(position);
            } else {
                position = 0;
            }

        } else if (playmode == MusicPlayerService.REPEATE_SINGLE) {
            openAudio(position);

        } else if (playmode == MusicPlayerService.REPEATE_ALL) {
            openAudio(position);
        } else {
            if (position >= 0) {
                openAudio(position);
            } else {
                position = 0;
            }
        }
    }

    /**
     * 得到播放模式
     */
    int getPlayMode() {
        return playmode;
    }

    /**
     * 设置播放模式
     */
    void setPlayMode(int mode) {
        this.playmode = mode;
        CacheUtils.setPlaymode(this, "playmode", playmode);

    }


}
