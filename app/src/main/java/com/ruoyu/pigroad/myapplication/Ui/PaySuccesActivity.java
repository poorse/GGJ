package com.ruoyu.pigroad.myapplication.Ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyu.pigroad.myapplication.R;
import com.ruoyu.pigroad.myapplication.Util.ActivityManager;
import com.ruoyu.pigroad.myapplication.Util.PrinterCmdUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fangpai.cloudabull.com.usblibrary.listener.IUSBListener;
import fangpai.cloudabull.com.usblibrary.usb.MyUSBPrinter;
import fangpai.cloudabull.com.usblibrary.utils.UsbSaveType;
import fangpai.cloudabull.com.usblibrary.utils.UsbUseType;

import static com.ruoyu.pigroad.myapplication.Config.Config_API.API_URL;

/**
 * Created by PIGROAD on 2018/11/17
 * Email:920015363@qq.com
 */
public class PaySuccesActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_titile)
    TextView tv_title;
    @BindView(R.id.tv_downtime)
    TextView tv_downtime;
    @BindView(R.id.btn_close)
    LinearLayout btn_close;
    @BindView(R.id.btn_next)
    Button btn_next;

    private ActivityManager activityManager;

    private String login_token;

    private MyUSBPrinter usbPrinter;

    private int id;
    private int recLen = 60;

    private String title, time, order_id, gas_station, oil_type, oil_price, oil_lit, oil_gun, total, pay="";

    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    tv_downtime.setText("退出登录/本窗口在（" + recLen+"）秒内自动关闭");

                    if(recLen > 0){
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }else{
                        //倒计时完成
                        SharedPreferences preferences = getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("login_token","");
                        editor.commit();
                        activityManager.exit();
                    }
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_succes_layout);
        activityManager = ActivityManager.getInstance();
        activityManager.addActivity(this);
        usbPrinter = MyUSBPrinter.getInstance();
        usbPrinter.initPrinter(this, UsbUseType.USE_PRINT_PRINTER);
        ButterKnife.bind(this);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {

        //拿token
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("login_token", null);
        id = getIntent().getIntExtra("id", 0);

        iv_back.setVisibility(View.INVISIBLE);

        tv_title.setText("支付结果");

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("login_token", "");
                editor.commit();
                activityManager.exit();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.exit();
            }
        });

        printTicket();

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);      // send message

    }


    /**
     * 小票打印
     */
    private void printTicket() {
        OkGo.<String>post(API_URL + "?request=public.ticket.ggj_order_ticket_get&token=" + login_token + "&platform=app&imei=11111111&id=" + id + "&pay_method=4")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject ticket = jsonObject.getJSONObject("ticket");
                                title = ticket.getString("1");
                                time = ticket.getString("2");
                                order_id = ticket.getString("3");
                                gas_station = ticket.getString("4");
                                oil_type = ticket.getString("5");
                                oil_price = ticket.getString("6");
                                oil_lit = ticket.getString("7");
                                oil_gun = ticket.getString("9");
                                total = ticket.getString("10");
                                pay = ticket.getString("11");
                                showDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void print(String title, String time, String order_id, String oil_station, String oil_type, String oil_price, String oil_lit,
                       String oil_gun, String total, String pay) {
        byte[] check = PrinterCmdUtils.transfer();
        byte[] esc_bytes = PrinterCmdUtils.clientPaper2(title, time, order_id, oil_station, oil_type, oil_price, oil_lit, oil_gun, total, pay);
        usbPrinter.print(check, true, esc_bytes, 1, false, null, new IUSBListener() {
            @Override
            public void onUsbListener(String type) {
                Log.d("usbPrinter", type);
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.succes_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        final Dialog dialog=builder.create();
        final Button btn_next=view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print(title,time,order_id,gas_station,oil_type,oil_price,oil_lit,oil_gun,total,pay);
                dialog.dismiss();
            }
        });
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                btn_next.performClick();
            }
        }).run();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }
}
