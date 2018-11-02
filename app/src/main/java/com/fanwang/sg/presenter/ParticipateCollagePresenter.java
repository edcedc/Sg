package com.fanwang.sg.presenter;

import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.view.impl.ParticipateCollageContract;

import java.util.List;

/**
 * 作者：yc on 2018/9/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ParticipateCollagePresenter extends ParticipateCollageContract.Presenter {

    @Override
    public void onSubmitOrder(BaseFragment root, int type, String id, List<DataBean> listBean, int secKillState, int flag, String orderNo) {
        switch (type) {//预防后期改
            case 1001://普通商品
                UIHelper.startConfirmationOrderFrg(root, id, listBean, type, flag, orderNo, 1);
                break;
            case 1002://抢购商品
                UIHelper.startConfirmationOrderFrg(root, id, listBean, type, flag, orderNo, 1);
                break;
            case 1003://拼团商品
                UIHelper.startConfirmationOrderFrg(root, id, listBean, type, flag, orderNo, 1);
                break;
        }
    }
}
