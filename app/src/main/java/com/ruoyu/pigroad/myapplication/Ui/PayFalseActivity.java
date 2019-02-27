package com.ruoyu.pigroad.myapplication.Ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyu.pigroad.myapplication.R;
import com.ruoyu.pigroad.myapplication.Util.ActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PIGROAD on 2018/11/19
 * Email:920015363@qq.com
 */
public class PayFalseActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_titile)
    TextView tv_title;
    @BindView(R.id.tv_downtime)
    TextView tv_downtime;
    @BindView(R.id.btn_close)
    LinearLayout btn_close;
    private int recLen = 60;

    private ActivityManager activityManager;

    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    tv_downtime.setText("重新操作/本窗口在（" + recLen+"）秒内自动关闭");

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
        setContentView(R.layout.pay_error_layout);
        ButterKnife.bind(this);
        this.init();
        activityManager = ActivityManager.getInstance();
        activityManager.addActivity(this);
    }

    /**
     * 初始化
     */
    private void init() {
        iv_back.setVisibility(View.INVISIBLE);

        tv_title.setText("付款结果");

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.exit();
            }
        });

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);      // send message
    }

}
