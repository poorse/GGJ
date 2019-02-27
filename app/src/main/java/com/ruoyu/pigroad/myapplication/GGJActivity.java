package com.ruoyu.pigroad.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruoyu.pigroad.myapplication.Login.LoginActivity;
import com.ruoyu.pigroad.myapplication.Ui.OilGunActivity;
import com.ruoyu.pigroad.myapplication.Ui.PaySuccesActivity;
import com.ruoyu.pigroad.myapplication.Ui.PointShopActivity;
import com.ruoyu.pigroad.myapplication.Ui.SettingActivity;
import com.ruoyu.pigroad.myapplication.Util.PrinterCmdUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import fangpai.cloudabull.com.usblibrary.listener.IUSBListener;
import fangpai.cloudabull.com.usblibrary.usb.MyUSBPrinter;
import fangpai.cloudabull.com.usblibrary.utils.UsbSaveType;
import fangpai.cloudabull.com.usblibrary.utils.UsbUseType;

/**
 * Created by PIGROAD on 2018/11/15
 * Email:920015363@qq.com
 */
public class GGJActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_main_bg)
    ImageView iv_main_bg;
    @BindView(R.id.btn_jyjf)
    ImageView btn_jyjf;
    @BindView(R.id.btn_shop)
    ImageView btn_shop;
    @BindView(R.id.btn_face)
    ImageView btn_face;
    @BindView(R.id.btn_integral)
    ImageView btn_integral;
    @BindView(R.id.btn_invoice)
    ImageView btn_invoice;
    @BindView(R.id.btn_account)
    ImageView btn_account;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;
    private int recLen = 15;


    private String login_token;

    private MyUSBPrinter usbPrinter;

    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    if(recLen > 0){
                        Log.i("pppppp",recLen+"");
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }else{
                        showDialog();
                    }
            }

            super.handleMessage(msg);
        }
    };

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ggj_layout);
        ButterKnife.bind(this);
        this.init();
        //printTicket();
        usbPrinter = MyUSBPrinter.getInstance();
        usbPrinter.initPrinter(this, UsbUseType.USE_PRINT_PRINTER);

    }

    /**
     * 初始化
     */
    private void init() {

        Glide.with(this).load(R.drawable.main_bg).fitCenter().into(iv_main_bg);
        Glide.with(this).load(R.drawable.btn_jyjf).fitCenter().into(btn_jyjf);
        Glide.with(this).load(R.drawable.btn_face).fitCenter().into(btn_face);
        Glide.with(this).load(R.drawable.btn_integral).fitCenter().into(btn_integral);
        Glide.with(this).load(R.drawable.btn_shop).fitCenter().into(btn_shop);
        Glide.with(this).load(R.drawable.btn_invoice).fitCenter().into(btn_invoice);
        Glide.with(this).load(R.drawable.btn_account).fitCenter().into(btn_account);

        new TimeThread().start();

        //进入 蓝牙 设置界面
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAdminDialog();
            }
        });

        btn_jyjf.setOnClickListener(this);
        btn_face.setOnClickListener(this);
        btn_integral.setOnClickListener(this);
        btn_shop.setOnClickListener(this);
        btn_invoice.setOnClickListener(this);
        btn_account.setOnClickListener(this);
        iv_setting.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent2=new Intent(GGJActivity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.btn_jyjf:
                Intent intent=new Intent(GGJActivity.this, OilGunActivity.class);
                if (checkToken()) {
                    startActivity(intent);
                    handler.removeMessages(1);
                } else {
                    intent2.putExtra("type","jyjf");
                    startActivity(intent2);
                    handler.removeMessages(1);
                }
                break;
            case R.id.btn_face:

                break;
            case R.id.iv_setting:
                showAdminDialog();
                break;
            case R.id.btn_integral:
                Intent intent3=new Intent(GGJActivity.this, PointShopActivity.class);
                if (checkToken()) {
                    startActivity(intent3);
                    handler.removeMessages(1);
                } else {
                    intent2.putExtra("type","jfsc");
                    startActivity(intent2);
                    handler.removeMessages(1);
                }
                break;
        }
    }

    public class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    final Calendar c = Calendar.getInstance();
                    c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
                    String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
                    if ("1".equals(mWay)) {
                        mWay = "天";
                    } else if ("2".equals(mWay)) {
                        mWay = "一";
                    } else if ("3".equals(mWay)) {
                        mWay = "二";
                    } else if ("4".equals(mWay)) {
                        mWay = "三";
                    } else if ("5".equals(mWay)) {
                        mWay = "四";
                    } else if ("6".equals(mWay)) {
                        mWay = "五";
                    } else if ("7".equals(mWay)) {
                        mWay = "六";
                    }
                    tv_time.setText(format.format(date));
                    tv_day.setText(format2.format(date) + " 星期" + mWay);
                    break;
                default:
                    break;
            }
        }
    };



    /**
     * 弹出管理员 弹窗
     */
    private void showAdminDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(GGJActivity.this).inflate(R.layout.setting_dialog, null);
        builder.setView(view);

        final EditText et_password = view.findViewById(R.id.et_password);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_confirm = view.findViewById(R.id.btn_confirm);

        final Dialog dialog = builder.create();
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString().trim();
                if (password.equals("666")) {
                    dialog.dismiss();
                    startActivity(new Intent(GGJActivity.this, SettingActivity.class));
                    handler.removeMessages(1);
                } else {
                    Toast.makeText(GGJActivity.this, "管理员密码不对！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

    /**
     * 判断是否有token
     */
    private boolean checkToken() {
        SharedPreferences sp = this.getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("login_token", "");
        if (login_token.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                if (checkToken()){
                    //初始化CountTimer，设置倒计时为15秒。
                    Message message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message, 1000);      // send message
                }else {
                    //countTimerView.cancel();
                    handler.removeMessages(1);
                }
                break;
            //否则其他动作计时取消
            default:
                handler.removeMessages(1);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        recLen = 15;
        if (checkToken()){
            //初始化CountTimer，设置倒计时为15秒。
            Message message = handler.obtainMessage(1);
            handler.sendMessageDelayed(message, 1000);      // send message
        }else {
            handler.removeMessages(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }


    private void showDialog(){
        handler.removeMessages(1);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.leava_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        final Dialog dialog=builder.create();
        final Button btn_next=view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clearToken();
            }
        });
        dialog.show();
    }

    private void clearToken(){
        //倒计时完成
        SharedPreferences preferences = getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login_token", "");
        editor.commit();
    }

    /**
     * 弹出登录弹窗
     */
    private void showLoginDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.leava_dialog, null);
        Dialog dialog=builder.create();
    }

}
