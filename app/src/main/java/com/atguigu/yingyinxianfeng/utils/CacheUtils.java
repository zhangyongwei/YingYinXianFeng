package com.atguigu.yingyinxianfeng.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.atguigu.yingyinxianfeng.service.MusicPlayerService;


/**
 *
 * 作用：缓存工具类
 */
public class CacheUtils {
    /**
     * 得到缓存的文本数据
     * @param mContext
     * @param key
     * @return
     */
    public static String getString(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    /**
     * 保持数据
     * @param mContext
     * @param key
     * @param value
     */
    public static void putString(Context mContext, String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    /**
     * 保持播放模式
     * @param context
     * @param key
     * @param value
     */
    public static void setPlaymode(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    /**
     * 得到保存播放模式
     * @param context
     * @param key
     * @return
     */
    public static int getPlaymode(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getInt(key, MusicPlayerService.REPEATE_NOMAL);

    }
}
