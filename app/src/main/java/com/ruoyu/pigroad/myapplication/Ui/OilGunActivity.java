package com.ruoyu.pigroad.myapplication.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyu.pigroad.myapplication.Bean.OilGunBean;
import com.ruoyu.pigroad.myapplication.R;
import com.ruoyu.pigroad.myapplication.Util.ActivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ruoyu.pigroad.myapplication.Config.Config_API.API_URL;

/**
 * Created by PIGROAD on 2018/11/14
 * Email:920015363@qq.com
 */
public class OilGunActivity extends AppCompatActivity {

    @BindView(R.id.tv_titile)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_oil_img)
    ImageView iv_oil_img;
    @BindView(R.id.et_oil_gun)
    EditText et_oil_gun;
    @BindView(R.id.tv_oil_gun)
    TextView tv_oil_gun;
    @BindView(R.id.btn_oil_confirm)
    Button btn_oil_confirm;

    private ActivityManager activityManager;

    private List<OilGunBean> datas=new ArrayList<>();

    private String login_token,oil_id,oil_name,oil_image,oil_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oilgun_layout);
        ButterKnife.bind(this);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {

        SharedPreferences sp = this.getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("login_token", "");

        oil_id = sp.getString("oil_id", "");
        oil_name = sp.getString("oil_name", "");
        oil_image = sp.getString("oil_img", "");

        check_all_oil_gun();

        activityManager = com.ruoyu.pigroad.myapplication.Util.ActivityManager.getInstance();
        activityManager.addActivity(this);

        tv_title.setText(oil_name);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this)
                .load(oil_image)
                .centerCrop()
                .into(iv_oil_img);

        et_oil_gun.setInputType(InputType.TYPE_CLASS_NUMBER);

        //监听 油枪号
        et_oil_gun.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){

                    //判断是否有 油枪

                    for (int i=0;i<datas.size();i++){
                        String input_value=s.toString();
                        if (input_value.equals(datas.get(i).getGun_id())){
                            tv_oil_gun.setText(datas.get(i).getOil_name());
                            tv_oil_gun.setVisibility(View.VISIBLE);
                            tv_oil_gun.setTextColor(Color.parseColor("#32CD32"));
                            oil_type=datas.get(i).getOil_type_id();
                            oil_name=datas.get(i).getOil_name();
                            btn_oil_confirm.setEnabled(true);
                            btn_oil_confirm.setBackgroundColor(Color.parseColor("#32CD32"));
                            break;
                        }else {
                            tv_oil_gun.setTextColor(Color.RED);
                            tv_oil_gun.setVisibility(View.VISIBLE);
                            tv_oil_gun.setText("油枪号错误！");
                            btn_oil_confirm.setEnabled(false);
                            btn_oil_confirm.setBackgroundColor(Color.parseColor("#C3BAB0"));
                        }
                    }

                }else {
                    tv_oil_gun.setVisibility(View.INVISIBLE);
                    btn_oil_confirm.setBackgroundColor(Color.parseColor("#C3BAB0"));
                    btn_oil_confirm.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //确认按钮
        btn_oil_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OilGunActivity.this,MoneyActivity.class);
                intent.putExtra("oil_id",oil_id);
                intent.putExtra("gun_id",et_oil_gun.getText().toString());
                intent.putExtra("oil_img",oil_image);
                intent.putExtra("oil_title",oil_name);
                intent.putExtra("oil_type",oil_type);
                intent.putExtra("oil_name",oil_name);
                startActivity(intent);
            }
        });

    }

    /**
     * 获取 油枪号
     */
//    private void getOilGun(final AlertDialog.Builder builder){
//        datas.clear();
//        OkGo.<String>post(API_URL+"?request=private.gas_station.get.store.all.oil.gun&token="+login_token+"&platform=app&store_id="+oil_id)
//            .execute(new StringCallback() {
//                @Override
//                public void onSuccess(Response<String> response) {
//                    try {
//                        JSONObject jsonObject=new JSONObject(response.body());
//                        final JSONArray data=jsonObject.getJSONArray("data");
//                        for (int i=0;i<data.length();i++){
//                            JSONObject temp=data.getJSONObject(i);
//                            OilGunBean bean=new OilGunBean();
//                            bean.setOil_gun(temp.getInt("gun_id"));
//                            bean.setOil_name(temp.getString("oil_name"));
//                            datas.add(bean);
//                        }
//                        Collections.sort(datas, new Comparator<OilGunBean>() {
//                            @Override
//                            public int compare(OilGunBean o1, OilGunBean o2) {
//                                int diff=o1.getOil_gun()-o2.getOil_gun();
//                                if ( diff> 0 ){
//                                    return 1;
//                                }else if (diff < 0){
//                                    return -1;
//                                }
//                                return 0;
//                            }
//                        });
//                        List<String> keysList = new ArrayList<>();
//                        for (int i=0;i<datas.size();i++){
//                            keysList.add(datas.get(i).getOil_gun()+"号油枪("+datas.get(i).getOil_name()+")");
//                        }
//                        final String[] oil_list=keysList.toArray(new String[keysList.size()]);
//                        builder.setItems(oil_list, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builder.create().show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//    }

    /**
     * 弹出 list窗口
     */
    /**
     * 弹出油站选择
     */
    private void showChooseOil(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("请选择油枪号");
//        getOilGun(builder);
    }

    /**
     * 查询所有 油枪 是否能使用
     */
    private void check_all_oil_gun(){
        OkGo.<String>post(API_URL+"?request=private.gas_station.get.store.all.oil.gun&token="+login_token+"&platform=app&store_id="+oil_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    OilGunBean bean=new OilGunBean();
                                    bean.setGun_id(temp.getString("gun_id"));
                                    bean.setOil_name(temp.getString("oil_name"));
                                    bean.setOil_type_id(temp.getString("oil_type_id"));
                                    datas.add(bean);
                                }
                            }else if (code == 999){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
