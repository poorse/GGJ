package com.ruoyu.pigroad.myapplication.Ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyu.pigroad.myapplication.Adapter.OilListAdapter;
import com.ruoyu.pigroad.myapplication.Adapter.OnItemClickLitener;
import com.ruoyu.pigroad.myapplication.Bean.OilListBean;
import com.ruoyu.pigroad.myapplication.GGJActivity;
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
 * Created by PIGROAD on 2018/11/17
 * Email:920015363@qq.com
 */
public class FristChooseActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.tv_choose)
    TextView tv_choose;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    private String oil_id="";
    private String oil_name="";
    private String oil_img="";

    private ActivityManager activityManager;

    private List<OilListBean> datas=new ArrayList<>();
    private OilListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_choose_layout);
        ButterKnife.bind(this);
        this.init();
        activityManager = ActivityManager.getInstance();
        activityManager.addActivity(this);
    }

    /**
     * 初始化
     */
    private void init() {
        adapter=new OilListAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        getData();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_choose.getText().toString().isEmpty()){
                    Toast.makeText(FristChooseActivity.this,"请选择所在油站！",Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences mSharedPreferences =getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("oil_id",oil_id);
                    editor.putString("oil_name",oil_name);
                    editor.putString("once_login","true");
                    editor.putString("oil_img",oil_img);
                    editor.commit();
                    startActivity(new Intent(FristChooseActivity.this,GGJActivity.class));
                    finish();
                }
            }
        });

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                tv_choose.setText(datas.get(position).getName());
                oil_id=datas.get(position).getId();
                oil_name=datas.get(position).getName();
                oil_img=datas.get(position).getImg();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityManager.exit();
            }
        });

    }

    /**
     * 获取 数据
     */
    private void getData(){
        OkGo.<String>post(API_URL+"?request=public.service.get.all.store.no.distance&platform=app")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code= jsonObject.getInt("code");
                            if (code == 0){
                                final JSONArray data=jsonObject.getJSONArray("data");
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    OilListBean bean=new OilListBean();
                                    bean.setId(temp.getString("id"));
                                    bean.setName(temp.getString("name"));
                                    bean.setImg(temp.getString("pic_url"));
                                    bean.setAddress(temp.getString("address"));
                                    datas.add(bean);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
