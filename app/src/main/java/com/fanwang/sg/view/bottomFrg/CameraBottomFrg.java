package com.fanwang.sg.view.bottomFrg;

import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseBottomSheetFrag;


/**
 * 作者：yc on 2018/8/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  打开相册或相机
 */

public class CameraBottomFrg extends BaseBottomSheetFrag implements View.OnClickListener{

    @Override
    public int bindLayout() {
        return R.layout.p_camera;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_camera).setOnClickListener(this);
        view.findViewById(R.id.tv_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
             case R.id.tv_camera:
                if (listener != null){
                    listener.camera();
                }
                break;
             case R.id.tv_photo:
                 if (listener != null){
                     listener.photo();
                 }
                break;

        }
    }

    private onCameraListener listener;
    public void setCameraListener(onCameraListener listener){
        this.listener = listener;
    }

    public interface onCameraListener{
        void camera();
        void photo();
    }


}
