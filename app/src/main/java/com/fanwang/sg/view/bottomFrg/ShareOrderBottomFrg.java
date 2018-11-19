package com.fanwang.sg.view.bottomFrg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.OrderDetailsCollageAdapter;
import com.fanwang.sg.base.BaseBottomSheetFrag;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.ImageUtils;
import com.fanwang.sg.utils.ZXingUtils;
import com.fanwang.sg.weight.WithScrollGridView;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：yc on 2018/10/18.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  生成分享图片
 */

@SuppressLint("ValidFragment")
public class ShareOrderBottomFrg extends BaseBottomSheetFrag implements View.OnClickListener{

    private DataBean bean;
    private ImageView ivImg;
    private TextView tvCollageTitle;
    private WithScrollGridView gvCollage;
    private TextView tvCollageTime;
    private CountdownView tvCollageTime2;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvPrice2;
    private ImageView ivZking;

    private List<DataBean> listCollageBean = new ArrayList<>();
    private OrderDetailsCollageAdapter collageAdapter;
    private View clYout;

    public ShareOrderBottomFrg(DataBean bean) {
        this.bean = bean;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fy_close:
                dismiss();
                break;
            case R.id.tv_confirm:
                String s = ImageUtils.viewSaveToImage(clYout, TimeUtils.getNowString());
                if (listener != null){
                    Bitmap bitmap = ImageUtils.view2Bitmap(clYout);
                    listener.click(bitmap);
                    dismiss();
                }
                break;
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.f_share_order;
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void click(Bitmap imgFile);
    }

    @Override
    public void initView(View view) {
        ivImg = view.findViewById(R.id.iv_img);
        view.findViewById(R.id.fy_close).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm).setOnClickListener(this);
        tvCollageTitle = view.findViewById(R.id.tv_collage_title);
        gvCollage = view.findViewById(R.id.gv_collage);
        tvCollageTime = view.findViewById(R.id.tv_collage_time);
        tvCollageTime2 = view.findViewById(R.id.tv_collage_time2);
        clYout = view.findViewById(R.id.cl_yout);
        tvName = view.findViewById(R.id.tv_name);
        tvPrice = view.findViewById(R.id.tv_price);
        tvPrice2 = view.findViewById(R.id.tv_price2);
        ivZking = view.findViewById(R.id.iv_zking);

        listCollageBean.clear();
        if (collageAdapter == null){
            collageAdapter = new OrderDetailsCollageAdapter(act, listCollageBean);
        }
        gvCollage.setAdapter(collageAdapter);

        int collageNum = bean.getCollageNum();
        List<DataBean> listUser = bean.getListUser();
        int collageNums = collageNum - listUser.size();
        setisGroupPurchase(bean);
        String collage_title = "还差" + "<font color='#FE2701'> " + collageNums + " </font>" + "赶快邀请好友来";
        tvCollageTitle.setText(Html.fromHtml(collage_title));
        tvCollageTime.setText("拼团剩余时间：");

        final DataBean orderBean = bean.getListOrderDetails().get(0);
        tvName.setText(orderBean.getName());
        tvPrice.setText(orderBean.getPrice() + "");
        tvPrice2.setText(orderBean.getOriginal_price() + "");

        ivZking.setImageResource(R.mipmap.collage_friends_share);
//        ivZking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ivZking.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                try {
//                    Bitmap bitmap = ZXingUtils.creatBarcode(CloudApi.SERVLET_URL + "product/productDetail?pid=" + bean.getId(), ivZking.getWidth());
//                    ivZking.setImageBitmap(bitmap);
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        ivImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivImg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewGroup.LayoutParams params = ivImg.getLayoutParams();
                params.height = ivImg.getWidth();
                ivImg.setLayoutParams(params);
                GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + orderBean.getShowImg(), ivImg);
            }
        });
    }

    //是否拼团单
    private void setisGroupPurchase(DataBean bean){
        List<DataBean> listUser = bean.getListUser();
        if (listUser != null && listUser.size() != 0){//是否拼团那种
            int collageNum = bean.getCollageNum();
            int collageNums = collageNum - listUser.size();
            if (collageNums > 3 && listUser.size() < 3){
                listCollageBean.addAll(listUser);
                for (int i = 0;i < 3 - listUser.size();i++){
                    listCollageBean.add(new DataBean());
                }
            }else if (collageNum > 3 && listUser.size() > 3){
                for (int i = 0;i < 3;i++){
                    listCollageBean.addAll(listUser);
                }
            }else if (collageNum < 3 && listUser.size() < collageNum){
                listCollageBean.addAll(listUser);
                for (int i = 0;i < (collageNum - listUser.size());i++){
                    listCollageBean.add(new DataBean());
                }
            }else {
                listCollageBean.addAll(listUser);
            }
            collageAdapter.notifyDataSetChanged();
//            mB.gpPendingDelivery.setVisibility(View.VISIBLE);
        }else {

        }
    }

}
