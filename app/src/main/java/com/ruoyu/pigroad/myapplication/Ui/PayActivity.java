package com.ruoyu.pigroad.myapplication.Ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyu.pigroad.myapplication.R;
import com.ruoyu.pigroad.myapplication.Util.ActivityManager;
import com.ruoyu.pigroad.myapplication.Util.Toastutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ruoyu.pigroad.myapplication.Config.Config_API.API_URL;

/**
 * Created by PIGROAD on 2018/9/6
 * Email:920015363@qq.com
 */
public class PayActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_oil_img)
    ImageView iv_oil_img;
    @BindView(R.id.tv_titile)
    TextView tv_title;
    @BindView(R.id.tv_oil_type)
    TextView tv_oil_type;
    @BindView(R.id.tv_oil_name)
    TextView tv_oil_name;
    @BindView(R.id.tv_oil_money)
    TextView tv_oil_money;
    @BindView(R.id.ll_ali)
    RelativeLayout ll_ali;
    @BindView(R.id.ll_wx)
    RelativeLayout ll_wx;
    @BindView(R.id.rb_wx)
    ImageView rb_wx;
    @BindView(R.id.rb_ali)
    ImageView rb_ali;
    @BindView(R.id.btn_pay)
    Button btn_pay;
    private String pay_method = "";
    private int id = 0;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private ActivityManager activityManager;
    private String login_token;

    private String notify_url = "";

    @SuppressLint("HandlerLeak")


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);
        ButterKnife.bind(this);
        activityManager = ActivityManager.getInstance();
        activityManager.addActivity(this);
        this.init();
    }

    private void init() {
        id = getIntent().getIntExtra("id", 0);

        //拿token
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this).load(getIntent().getStringExtra("oil_img")).into(iv_oil_img);
        tv_title.setText(getIntent().getStringExtra("oil_title"));
        tv_oil_name.setText(getIntent().getStringExtra("oil_title"));
        tv_oil_type.setText(getIntent().getStringExtra("oil_name"));
        tv_oil_money.setText("￥" + getIntent().getIntExtra("money", 0));

        ll_ali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_ali.setBackgroundResource(R.drawable.icon_check);
                rb_wx.setBackgroundResource(R.drawable.icon_un_check);
                pay_method = "alipay";
            }
        });

        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_wx.setBackgroundResource(R.drawable.icon_check);
                rb_ali.setBackgroundResource(R.drawable.icon_un_check);
                pay_method = "wx";
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pay_method.equals("")) {
                    Toastutil.show(PayActivity.this, "请选择付款方式！");
                } else {
                    //payConfirm();
                    ScanGunPay();
                }
            }
        });

    }

    /**
     * 扫一扫付款
     */
    private void ScanGunPay() {
        if (pay_method.equals("alipay")) {
            Intent intent = new Intent(PayActivity.this, WaitPayActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        } else {

        }
    }


}