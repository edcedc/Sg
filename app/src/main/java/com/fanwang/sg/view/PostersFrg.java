package com.fanwang.sg.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FPostersBinding;
import com.fanwang.sg.presenter.PostersPresenter;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.ZXingUtils;
import com.fanwang.sg.view.bottomFrg.ShareBottomFrg;
import com.fanwang.sg.view.impl.PostersContract;
import com.google.zxing.WriterException;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  生成宣传海报
 */

public class PostersFrg extends BaseFragment<PostersPresenter, FPostersBinding> implements PostersContract.View{

    private ShareBottomFrg shareBottomFrg;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_posters;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.create_posters2));
        if (shareBottomFrg == null) {
            shareBottomFrg = new ShareBottomFrg();
        }
        mPresenter.onAppAboutUs();
        mB.ivImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mB.ivZking.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                try {
                    Bitmap bitmap = ZXingUtils.creatBarcode(CloudApi.share + User.getInstance().getUserId(),mB.ivImg.getWidth());
                    mB.ivZking.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        mB.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shareBottomFrg != null && !shareBottomFrg.isShowing()) {
//                            shareBottomFrg.setViewLayout(mB.layout);
                    shareBottomFrg.setImgBitmap(null);
                    shareBottomFrg.setShareUrl(CloudApi.share);
                    shareBottomFrg.show(getChildFragmentManager(), "dialog");
                }
            }
        });
    }

    @Override
    public void appAboutUsSucces(DataBean bean) {
        String distribution_img = bean.getDistribution_img();
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + distribution_img, mB.ivImg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shareBottomFrg != null && shareBottomFrg.isShowing()) {
            shareBottomFrg.dismiss();
        }
    }

}
