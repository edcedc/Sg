package com.fanwang.sg.controller;

import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.rcloud.ui.ConversationActivity;
import com.fanwang.sg.rcloud.ui.SubConversationListActivity;
import com.fanwang.sg.view.AboutUsFrg;
import com.fanwang.sg.view.AddBankFrg;
import com.fanwang.sg.view.AddressFrg;
import com.fanwang.sg.view.AfterSaleGoodsFrg;
import com.fanwang.sg.view.CollageSuccessFrg;
import com.fanwang.sg.view.CollectionFrg;
import com.fanwang.sg.view.ConfirmationOrderFrg;
import com.fanwang.sg.view.DistributionFrg;
import com.fanwang.sg.view.EditAddressFrg;
import com.fanwang.sg.view.ForwardFrg;
import com.fanwang.sg.view.HelpCenterFrg;
import com.fanwang.sg.view.IntegralFrg;
import com.fanwang.sg.view.InvitationCollageFrg;
import com.fanwang.sg.view.LogisticsFrg;
import com.fanwang.sg.view.ManagementBankFrg;
import com.fanwang.sg.view.MessageDescFrg;
import com.fanwang.sg.view.MessageFrg;
import com.fanwang.sg.view.OrderFrg;
import com.fanwang.sg.view.OrderRefundFrg;
import com.fanwang.sg.view.ParticipateCollageFrg;
import com.fanwang.sg.view.PayStateFrg;
import com.fanwang.sg.view.PostersFrg;
import com.fanwang.sg.view.PresentRecordFrg;
import com.fanwang.sg.view.RefundStateFrg;
import com.fanwang.sg.view.ReturnGoodsFrg;
import com.fanwang.sg.view.act.RongcloudListAct;
import com.fanwang.sg.view.SetFrg;
import com.fanwang.sg.view.ShopFrg;
import com.fanwang.sg.view.UpdatePwdFrg;
import com.fanwang.sg.view.WalletFrg;
import com.fanwang.sg.view.act.ConfirmationOrderAct;
import com.fanwang.sg.view.act.GoodsDetailsAct;
import com.fanwang.sg.view.act.HtmlAct;
import com.fanwang.sg.view.act.LoginAct;
import com.fanwang.sg.view.act.MainActivity;
import com.fanwang.sg.view.act.OrderAct;
import com.fanwang.sg.view.act.OrderDetailsAct;
import com.fanwang.sg.view.act.SetAct;
import com.fanwang.sg.view.act.UpdateDataAct;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.view.BindPhoneFrg;
import com.fanwang.sg.view.ClassListFrg;
import com.fanwang.sg.view.Collagefrg;
import com.fanwang.sg.view.DiscountFrg;
import com.fanwang.sg.view.MainFrg;
import com.fanwang.sg.view.NewArrivalsFrg;
import com.fanwang.sg.view.SearchFrg;
import com.fanwang.sg.view.SecondKillFrg;
import com.fanwang.sg.view.UpdateNickFrg;
import com.fanwang.sg.view.act.WalletAct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/2/22.
 */

public final class UIHelper {

    private UIHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void startMainAct() {
        ActivityUtils.startActivity(MainActivity.class);
    }

    /**
     *  秒杀抢购
     * @param root
     */
    public static void startSecondKillFrg(BaseFragment root) {
        SecondKillFrg frg = new SecondKillFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }
      /**
     *  拼团
     * @param root
     */
    public static void startCollagefrg(BaseFragment root) {
        Collagefrg frg = new Collagefrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  新品推荐
     * @param root
     */
    public static void startNewArrivalsFrg(BaseFragment root) {
        NewArrivalsFrg frg = new NewArrivalsFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  优惠特卖专区  满减 满增
     * @param root
     */
    public static void startDiscountFrg(BaseFragment root, int type, String id) {
        DiscountFrg frg = new DiscountFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("id", id);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  搜索
     * @param root
     */
    public static void startSearchFrg(BaseFragment root) {
        SearchFrg frg = new SearchFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  通用H5
     */
    public static void startHtmlAct(String id, String title){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("title", title);
        bundle.putInt("type", 1);
        ActivityUtils.startActivity(bundle, HtmlAct.class);
    }
    public static void startHtmlAct(String id, String title, String content, int type){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("type", type);
        ActivityUtils.startActivity(bundle, HtmlAct.class);
    }

    /**
     *  分类列表
     * @param root
     * @param title
     */
    public static void startClassListFrg(BaseFragment root, String title, String id, int type) {
        ClassListFrg frg = new ClassListFrg();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("id", id);
        frg.setArguments(bundle);
        if (type == 0){
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        }else {
            ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
        }
    }

    /**
     *  登录
     */
    public static void startLoginAct() {
        ActivityUtils.startActivity(LoginAct.class);
    }

    /**
     *  绑定手机
     * @param root
     */
    public static void startBindPhoneFrg(BaseFragment root, int type){
        BindPhoneFrg frg = BindPhoneFrg.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  我的资料
     */
    public static void startUpdateDataAct(){
        ActivityUtils.startActivity(UpdateDataAct.class);
    }

    /**
     *  修改昵称
     */
    public static void startUpdateNickFrg(BaseFragment root, String nickname){
        UpdateNickFrg frg = UpdateNickFrg.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("nickname", nickname);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  我的订单
     */
    public static void startOrderAct(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        ActivityUtils.startActivity(bundle, OrderAct.class);
    }

    public static void startWalletAct(){
        ActivityUtils.startActivity(WalletAct.class);
    }

    /**
     *  我的分销
     * @param root
     */
    public static void startDistributionFrg(BaseFragment root, int type) {
        DistributionFrg frg = new DistributionFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        if (type == 0){
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        }else {
            root.start(frg);
        }
    }

    /**
     *  生成海报
     */
    public static void startPostersFrg(BaseFragment root){
        PostersFrg frg = new PostersFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  提现
     */
    public static void startForwardFrg(BaseFragment root) {
        ForwardFrg frg = new ForwardFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  管理银行卡
     * @param root
     */
    public static void startManagementBankFrg(BaseFragment root) {
        ManagementBankFrg frg = new ManagementBankFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  添加银行卡
     * @param root
     */
    public static void startAddBankFrg(BaseFragment root) {
        AddBankFrg frg = new AddBankFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  提现记录  明细
     */
    public static void startPresentRecordFrg(BaseFragment root, int type) {
        PresentRecordFrg frg = new PresentRecordFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  我的收藏
     * @param root
     */
    public static void startCollectionFrg(BaseFragment root) {
        CollectionFrg frg = new CollectionFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  我的钱包
     */
    public static void startWalletFrg(BaseFragment root){
        WalletFrg frg = new WalletFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }


    /**
     *  我的积分
     * @param root
     */
    public static void startIntegralFrg(BaseFragment root) {
        IntegralFrg frg = new IntegralFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  退款/售后
     * @param root
     */
    public static void startReturnGoodsFrg(BaseFragment root) {
        ReturnGoodsFrg frg = new ReturnGoodsFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  设置
     */
    public static void startSetFrg(BaseFragment root){
        SetFrg frg = new SetFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }
    /**
     *  帮助中心
     */
    public static void startHelpCenterFrgFrg(BaseFragment root){
        HelpCenterFrg frg = new HelpCenterFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    public static void startSetAct() {
        ActivityUtils.startActivity(SetAct.class);
    }

    /**
     *  更改密码
     */
    public static void startUpdatePwdFrg(BaseFragment root) {
        UpdatePwdFrg frg = new UpdatePwdFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  关于我们
     * @param root
     */
    public static void startAboutUsFrg(BaseFragment root) {
        AboutUsFrg frg = new AboutUsFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  订单详情
     */
    public static void startOrderDetailsAct(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        ActivityUtils.startActivity(bundle, OrderDetailsAct.class);
    }
    public static void startOrderDetailsAct(String id, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("title", title);
        ActivityUtils.startActivity(bundle, OrderDetailsAct.class);
    }

     /**
     *  商品详情
      * @param id
      */
    public static void startGoodsDetailsAct(String id, int overdue, String endTime, String startTime) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("endTime", endTime);
        bundle.putString("startTime", startTime);
        bundle.putInt("overdue", overdue);
        ActivityUtils.startActivity(bundle, GoodsDetailsAct.class);
    }
    public static void startGoodsDetailsAct(String id, int collageType) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("collageType", collageType);
        ActivityUtils.startActivity(bundle, GoodsDetailsAct.class);
    }
    public static void startGoodsDetailsAct(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        ActivityUtils.startActivity(bundle, GoodsDetailsAct.class);
    }

    /**
     *  消息中心
     * @param root
     */
    public static void startMessageFrg(BaseFragment root){
        MessageFrg frg = new MessageFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  我的地址
     */
    public static void startAddressFrg(BaseFragment root, int type){
        AddressFrg frg = new AddressFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  编辑地址 新增
     */
    public static void startEditAddressFrg(BaseFragment root, int type){
        EditAddressFrg frg = new EditAddressFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }
    public static void startEditAddressFrg(BaseFragment root, int type, DataBean bean){
        EditAddressFrg frg = new EditAddressFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("bean", new Gson().toJson(bean));
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  进入店铺
     * @param root
     * @param businessId
     */

    public static void startShopFrg(BaseFragment root, String businessId, int type){
        ShopFrg frg = new ShopFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", businessId);
        frg.setArguments(bundle);
        if (type == 0) {
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        }else {
            root.start(frg);
        }
    }

    /**
     *  确认订单
     */
    public static void startConfirmationOrderFrg(BaseFragment root, String id, List<DataBean> listBean, int type2, int flag, String orderNo, int startType){
        ConfirmationOrderFrg frg = new ConfirmationOrderFrg();
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        String json = new Gson().toJson(listBean, type);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("list", json);
        bundle.putString("orderNo", orderNo);
        bundle.putInt("type", type2);
        bundle.putInt("flag", flag);
        frg.setArguments(bundle);
        if (startType == 0){
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        }else {
            root.start(frg);
        }
    }
    public static void startConfirmationOrderAct(String id, List<DataBean> listBean, int type2, int flag, String orderNo, int isCredited){
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        String json = new Gson().toJson(listBean, type);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("list", json);
        bundle.putString("orderNo", orderNo);
        bundle.putInt("type", type2);
        bundle.putInt("flag", flag);
        bundle.putInt("isCredited", isCredited);
        ActivityUtils.startActivity(bundle, ConfirmationOrderAct.class);
    }

    /**
     *  支付状态
     */
    public static void startPayStateFrg(BaseFragment root, int state){
        PayStateFrg frg = new PayStateFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  拼团成功
     */
    public static void startCollageSuccessFrg(BaseFragment root, String id){
        CollageSuccessFrg frg = new CollageSuccessFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  参与拼团
     */
    public static void startParticipateCollageFrg(BaseFragment root, ProductProductDetailBean submitOrderBean, int position, List<DataBean> listUser, List<DataBean> listAllSkuBena, String choice){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        String json = gson.toJson(listUser, type);
        String json1 = gson.toJson(listAllSkuBena, type);
        ParticipateCollageFrg frg = new ParticipateCollageFrg();
        Bundle bundle = new Bundle();
        bundle.putString("bean", gson.toJson(submitOrderBean));
        bundle.putInt("position", position);
        bundle.putString("list", json);
        bundle.putString("listAllSkuBena", json1);
        bundle.putString("choice", choice);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  消息详情
     */
    public static void startMessageDescFrg(BaseFragment root, DataBean bean) {
        MessageDescFrg frg = new MessageDescFrg();
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(bean));
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  退货退款
     */
    public static void startOrderRefundFrg(BaseFragment root, String id, int type2, List<DataBean> listOrderDetails, int state){
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        OrderRefundFrg frg = new OrderRefundFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("type", type2);
        bundle.putString("list", new Gson().toJson(listOrderDetails, type));
        bundle.putInt("state", state);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  选择售后商品
     */
    public static void startAfterSaleGoodsFrg(BaseFragment root, String id, List<DataBean> listOrderDetails, int state){
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        AfterSaleGoodsFrg frg = new AfterSaleGoodsFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("list", new Gson().toJson(listOrderDetails, type));
        bundle.putInt("state", state);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  选择退货退款
     */
    public static void startOrderRefundStateFrg(BaseFragment root, String id, List<DataBean> listOrderDetails, int state){
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        RefundStateFrg frg = new RefundStateFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("list", new Gson().toJson(listOrderDetails, type));
        bundle.putInt("state", state);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  物流
     */
    public static void startLogisticsFrg(BaseFragment root, String id, String logisticsCompany, String logisticsNo){
        LogisticsFrg frg = new LogisticsFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("logisticsCompany", logisticsCompany);
        bundle.putString("logisticsNo", logisticsNo);
        frg.setArguments(bundle);
        ((OrderFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  邀请拼团
     */
    public static void startInvitationCollageFrg(BaseFragment root, DataBean bean, int type){
        InvitationCollageFrg frg = new InvitationCollageFrg();
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(bean));
        frg.setArguments(bundle);
        if (type == 0){
            ((OrderFrg) root.getParentFragment()).startBrotherFragment(frg);
        }else {
            root.start(frg);
        }
    }

    /**
     *  融云会话列表
     */
    public static void startRongcloudListAct(){
        ActivityUtils.startActivity(SubConversationListActivity.class);
    }

    /**
     *  客服
     */
    public static void startCustomerServiceAct(){
        ActivityUtils.startActivity(ConversationActivity.class);
    }

}