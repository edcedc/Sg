package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;

/**
 * 作者：yc on 2018/9/22.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface PresentRecordContract {

    interface View extends IBaseView{

    }

    abstract class Presenter extends BasePresenter<View>{

    }

}
