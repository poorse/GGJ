package com.ruoyu.pigroad.myapplication.Ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyu.pigroad.myapplication.Bean.OilListBean;
import com.ruoyu.pigroad.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ruoyu.pigroad.myapplication.Config.Config_API.API_URL;

/**
 * Created by PIGROAD on 2018/11/15
 * Email:920015363@qq.com
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_titile)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.ll_choose_oil)
    LinearLayout ll_choose_oil;
    @BindView(R.id.tv_oilid)
    TextView tv_oilid;

    private String oil_id="";
    private String oil_name="";
    private List<OilListBean> datas=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        tv_title.setText("设置中心");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_choose_oil.setOnClickListener(this);

        SharedPreferences sp = this.getSharedPreferences("LoginUser", MODE_PRIVATE);
        oil_id = sp.getString("oil_id", "");
        oil_name = sp.getString("oil_name", "");

        if (oil_id.equals("")){
            tv_oilid.setText("");
        }else {
            tv_oilid.setText(oil_name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_choose_oil:
                showChooseOil();
                break;
        }
    }

    /**
     * 弹出油站选择
     */
    private void showChooseOil(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("请选择你所在的油站");
        getOilList(builder);
    }

    /**
     * 获取门店列表
     */
    private void getOilList(final AlertDialog.Builder builder){
        OkGo.<String>post(API_URL+"?request=public.service.get.all.store.no.distance&platform=app")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code= jsonObject.getInt("code");
                            if (code == 0){
                                final JSONArray data=jsonObject.getJSONArray("data");
                                List<String> keysList = new ArrayList<>();
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    OilListBean bean=new OilListBean();
                                    bean.setId(temp.getString("id"));
                                    bean.setName(temp.getString("name"));
                                    bean.setImg(temp.getString("pic_url"));
                                    datas.add(bean);
                                    keysList.add(temp.getString("name"));
                                }
                                final String[] oil_list=keysList.toArray(new String[keysList.size()]);
                                builder.setItems(oil_list, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        tv_oilid.setText(datas.get(which).getName());
                                        SharedPreferences mSharedPreferences =getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                                        editor.putString("oil_id",datas.get(which).getId());
                                        editor.putString("oil_name",datas.get(which).getName());
                                        editor.putString("oil_img",datas.get(which).getImg());
                                        editor.commit();
                                    }
                                });
                                builder.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
