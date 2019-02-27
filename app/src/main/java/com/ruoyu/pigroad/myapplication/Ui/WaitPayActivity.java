package com.ruoyu.pigroad.myapplication.Ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyu.pigroad.myapplication.Login.LoginActivity;
import com.ruoyu.pigroad.myapplication.R;
import com.ruoyu.pigroad.myapplication.ScanHelper.ScanGunKeyEventHelper;
import com.ruoyu.pigroad.myapplication.Widget.CustomStatusView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ruoyu.pigroad.myapplication.Config.Config_API.API_URL;

/**
 * Created by PIGROAD on 2018/11/16
 * Email:920015363@qq.com
 */
public class WaitPayActivity extends AppCompatActivity implements ScanGunKeyEventHelper.OnScanSuccessListener {

    private ScanGunKeyEventHelper mScanGunKeyEventHelper;
    @BindView(R.id.tv_titile)
    TextView tv_title;

    private String login_token;
    private com.ruoyu.pigroad.myapplication.Util.ActivityManager activityManager;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.btn_input)
    Button btn_input;

    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitpay_layout);
        ButterKnife.bind(this);
        this.init();
    }

    private void init() {
        tv_title.setText("扫码付款");

        mScanGunKeyEventHelper = new ScanGunKeyEventHelper(WaitPayActivity.this);

        //拿token
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("login_token", null);

        id = getIntent().getIntExtra("id", 0);

        activityManager = com.ruoyu.pigroad.myapplication.Util.ActivityManager.getInstance();
        activityManager.addActivity(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.exit();
            }
        });

        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

    }

    /**
     * 截获按键事件.发给ScanGunKeyEventHelper
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            mScanGunKeyEventHelper.analysisKeyEvent(event);
            return true;
        }
//        if (mScanGunKeyEventHelper.isScanGunEvent(event)) {
//            mScanGunKeyEventHelper.analysisKeyEvent(event);
//            return true;
//        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onScanSuccess(String barcode) {
        if (barcode.equals("")) {
            Toast.makeText(WaitPayActivity.this, "请将付款码放至扫码处！", Toast.LENGTH_SHORT).show();
        } else {
            PayDialog(barcode);
        }
    }

    private void PayDialog(String barcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.pay_dialog, null);
        builder.setView(view);
        CustomStatusView customStatusView = view.findViewById(R.id.as_status);
        TextView tv_status = view.findViewById(R.id.tv_status);
        customStatusView.loadLoading();
        Dialog dialog = builder.create();
        dialog.show();
        Pay(barcode, dialog, customStatusView, tv_status);
    }

    /**
     * 调起支付接口
     */
    private void Pay(String auth_code, final Dialog dialog, final CustomStatusView customStatusView, final TextView tv_status) {
        OkGo.<String>post(API_URL + "?request=private.pay_temp.action_ggj_pay_gas_station&token=" + login_token + "&platform=app&sence_no=1&id=" + id + "&auth_code=" + auth_code)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                customStatusView.loadSuccess();
                                tv_status.setText("付款成功！");
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();
                                        Intent intent=new Intent(WaitPayActivity.this,PaySuccesActivity.class);
                                        intent.putExtra("id",id);
                                        startActivity(intent);
                                        finish();
                                    }
                                }.start();
                            } else {
                                customStatusView.loadFailure();
                                tv_status.setText("付款失败！");
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();
                                        startActivity(new Intent(WaitPayActivity.this,PayFalseActivity.class));
                                        finish();
                                    }
                                }.start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 弹出手动输入 付款码窗口
     */
    private void showInputDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.input_dialog, null);
        builder.setView(view);
        final EditText et_number=view.findViewById(R.id.et_number);
        final Button btn_next=view.findViewById(R.id.btn_next);
        final Dialog dialog=builder.create();
        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() < 18){
                        btn_next.setEnabled(false);
                    }else {
                        btn_next.setEnabled(true);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PayDialog(et_number.getText().toString().trim());
            }
        });
        dialog.show();
    }

}
