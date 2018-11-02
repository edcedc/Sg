package com.fanwang.sg.view.bottomFrg;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseBottomSheetFrag;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.ImageUtils;
import com.fanwang.sg.utils.ZXingUtils;
import com.fanwang.sg.weight.StrikethroughTextView;
import com.google.zxing.WriterException;

/**
 * 作者：yc on 2018/10/22.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ShareGoodsBottomFrg extends BaseBottomSheetFrag {

    private ProductProductDetailBean bean;

    public void setDate(ProductProductDetailBean bean){
        this.bean = bean;
    }

    @Override
    public int bindLayout() {
        return R.layout.f_goods_share;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.fy_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final ImageView ivImg = view.findViewById(R.id.iv_img);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvPrice = view.findViewById(R.id.tv_price);
        StrikethroughTextView tvPrice2 = view.findViewById(R.id.tv_price2);
        final ImageView ivZking = view.findViewById(R.id.iv_zking);
        final View clYout = view.findViewById(R.id.cl_yout);
        tvPrice.setText(getString(R.string.monetary_symbol) + bean.getRealPrice());
        tvPrice2.setText(getString(R.string.monetary_symbol) + bean.getMarketPrice());
        tvName.setText(bean.getName());
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    Bitmap bitmap = ImageUtils.view2Bitmap(clYout);
                    listener.click(bitmap);
                    dismiss();
                }
            }
        });
        ivZking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivZking.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                try {
                    Bitmap bitmap = ZXingUtils.creatBarcode(CloudApi.SERVLET_URL + "product/productDetail?pid=" + bean.getId(), ivZking.getWidth());
                    ivZking.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        ivImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivImg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewGroup.LayoutParams params = ivImg.getLayoutParams();
                params.height = ivImg.getWidth();
                ivImg.setLayoutParams(params);
                GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), ivImg);
            }
        });
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void click(Bitmap imgFile);
    }

}
