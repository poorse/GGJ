package com.ruoyu.pigroad.myapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyu.pigroad.myapplication.Bean.GoodsBean;
import com.ruoyu.pigroad.myapplication.R;
import com.ruoyu.pigroad.myapplication.Ui.PointShopActivity;
import com.ruoyu.pigroad.myapplication.Util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyu.pigroad.myapplication.Config.Config_API.API_URL;
import static com.ruoyu.pigroad.myapplication.Config.Config_API.LOG_TIP;


/**
 * Created by fengyongge on 2016/5/24 0024.
 */

/***
 * 底部购物车
 */
public class ProductAdapter extends BaseAdapter {
    GoodsAdapter goodsAdapter;
    private PointShopActivity activity;
    private SparseArray<GoodsBean> dataList;
    private String login_token;
    private Context context;
    public ProductAdapter(PointShopActivity activity, GoodsAdapter goodsAdapter, SparseArray<GoodsBean> dataList,String login_token,Context context) {
        this.goodsAdapter =goodsAdapter;
        this.activity = activity;
        this.dataList = dataList;
        this.login_token=login_token;
        this.context=context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.valueAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Viewholder viewholder;
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.product_item, null);
            viewholder = new Viewholder();
            viewholder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewholder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewholder.iv_add= (ImageView) view.findViewById(R.id.iv_add);
            viewholder.iv_remove= (ImageView) view.findViewById(R.id.iv_remove);
            viewholder.tv_count= (TextView) view.findViewById(R.id.tv_count);

            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }


            StringUtils.filtNull(viewholder.tv_name,dataList.valueAt(position).getTitle());//商品名称
            StringUtils.filtNull(viewholder.tv_price,"￥"+dataList.valueAt(position).getPrice());//商品价格
            viewholder.tv_count.setText(String.valueOf(dataList.valueAt(position).getNum()));//商品数量

            viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkGo.<String>post(API_URL + "?request=private.point_shop.change_the_cart_point_product&token=" + login_token + "&platform=app&product_id=" + dataList.valueAt(position).getProduct_id() + "&qty=1")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body());
                                        int code =jsonObject.getInt("code");
                                        if (code == 0){
                                            activity.handlerCarNum(1,dataList.valueAt(position),true);
                                            goodsAdapter.notifyDataSetChanged();
                                        }
                                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            });
            viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updata(dataList.valueAt(position).getProduct_id(),-1);
                    activity.handlerCarNum(0,dataList.valueAt(position),true);
                    goodsAdapter.notifyDataSetChanged();
                }
            });

        return view;
    }

    class Viewholder {
        TextView tv_price;
        TextView tv_name;
        ImageView iv_add,iv_remove;
        TextView tv_count;
    }

    /**
     * 购物车增删 操作
     */
    private void updata(int product_id, int qty) {
        OkGo.<String>post(API_URL + "?request=private.point_shop.change_the_cart_point_product&token=" + login_token + "&platform=app&product_id=" + product_id + "&qty=" + qty)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

}