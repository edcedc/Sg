package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;

/**
 * 作者：yc on 2018/9/3.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface UpdateDataContract {

    interface View extends IBaseView{

        void onUpdateHeadSuccess(String path);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void updateHead(String path);

        public abstract void updateSex(int sex);
    }

}
