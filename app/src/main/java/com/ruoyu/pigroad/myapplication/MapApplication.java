package com.ruoyu.pigroad.myapplication;


import android.support.multidex.MultiDexApplication;
/**
 * Created by PIGROAD on 2018/9/3
 * Email:920015363@qq.com
 */
public class MapApplication extends MultiDexApplication {

    public static final float VALUE_BRIGHTNESS = 40.0F;
    public static final float VALUE_BLURNESS = 0.7F;
    public static final float VALUE_OCCLUSION = 0.6F;
    public static final int VALUE_HEAD_PITCH = 15;
    public static final int VALUE_HEAD_YAW = 15;
    public static final int VALUE_HEAD_ROLL = 15;
    public static final int VALUE_CROP_FACE_SIZE = 400;
    public static final int VALUE_MIN_FACE_SIZE = 120;
    public static final float VALUE_NOT_FACE_THRESHOLD = 0.6F;


    @Override
    public void onCreate() {
        super.onCreate();
    }


}
