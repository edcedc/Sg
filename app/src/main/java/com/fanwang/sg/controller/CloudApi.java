package com.fanwang.sg.controller;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseListBean;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.bean.ProductSearchProdBean;
import com.fanwang.sg.callback.JsonConvert;
import com.fanwang.sg.callback.NewsCallback;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.cache.ShareSessionIdCache;
import com.fanwang.sg.utils.cache.SharedAccount;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okrx2.adapter.ObservableBody;
import com.lzy.okrx2.adapter.ObservableResponse;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CloudApi {


    private static final String url =
//            "10.0.0.200:8081/luxury_shopping/" ;
//            "47.106.217.107/";
            "shegouapp.com/";

    public static final String SERVLET_IMG_URL = "http://" +
            url ;

    public static final String SERVLET_URL = SERVLET_IMG_URL + "api/";


    public static final String TEST_URL = ""; //测试

    private static final String TAG = "CloudApi";

    private CloudApi() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     *  生成海报出去
     */
    public static final String share = SERVLET_URL + "share?id=";


    /**
     *  获取微信登陆返回值
     */
    public static Observable<JSONObject> wxLogin(String openid, String access_token){
        return OkGo.<JSONObject>get("https://api.weixin.qq.com/sns/userinfo")
                .params("access_token", access_token)
                .params("openid", openid)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.15微信登陆  接口(好)
     */
    public static Observable<JSONObject> userWeChatId(String head, String nick, String weChatId, int sex){
        return OkGo.<JSONObject>post(SERVLET_URL + "user/appWxLogin")
                .params("head", head)
                .params("nickname", nick)
                .params("weichatId", weChatId)
                .params("sex", sex)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  获取微信登陆返回值
     */
    public static Observable<JSONObject> wxAccess_token(String code ){
        return OkGo.<JSONObject>post("https://api.weixin.qq.com/sns/oauth2/access_token")
                .params("appid", Constants.WX_APPID)
                .params("secret", Constants.WX_SECRER)
                .params("code", code)
                .params("grant_type", "authorization_code")
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 通用获取验证码
     */
    public static Observable<Response<BaseResponseBean>> userGetRegisterCode(String url, String phone) {
        return OkGo.<BaseResponseBean>post(SERVLET_URL + url)
                .params("mobile", phone)
                .converter(new JsonConvert<BaseResponseBean>() {
                })
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.1.9用户基本信息  接口(好)
     */
    public static Observable<JSONObject> userInformation(String userId) {
        PostRequest<JSONObject> post = OkGo.post(SERVLET_URL + "user/information");
        if (!StringUtils.isEmpty(userId)) {
            post.params("byUserId", userId);
        }
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.2.1首页（好）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userMain() {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/main")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.11.1消息列表
     */
    public static final String userMessagesList = "user/messagesList";
    /**
     *  3.11.12我的收藏
     */
    public static final String userMyCollection = "user/myCollection";
    /**
     *  3.11.5提现记录
     */
    public static final String userWithDrawRecord = "user/withDrawRecord";
    /**
     *  积分商品列表
     */
    public static final String userCreditsProd = "user/creditsProd";
    /**
     *  搜索推荐商品
     */
    public static final String productProdRecommend = "product/prodRecommend";
    /**
     *  3.10.11退货列表（新）
     */
    public static final String orderPageRefund = "order/pageRefund";
    /**
     *  分销界面的列表
     */
    public static final String shareDistribution = "share/Distribution";
    /**
     *  明细
     */
    public static final String userDetailed = "user/detailed";

    /**
     * 通用list数据
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> list(int pageNumber, String url) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + url)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 分销界面的列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> shareDistribution(int pageNumber) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "share/Distribution")
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 通用list 2
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> list2(String url) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + url)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.12.1地址列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> userAddressList() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "user/addressList")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 搜索商品
     */
    public static Observable<Response<BaseResponseBean<List<ProductSearchProdBean>>>> productSearchProd(String text) {
        return OkGo.<BaseResponseBean<List<ProductSearchProdBean>>>get(SERVLET_URL + "product/searchProd")
                .params("keyword", text)
                .converter(new NewsCallback<BaseResponseBean<List<ProductSearchProdBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<ProductSearchProdBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<ProductSearchProdBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.3.3新品专区根据分类获取商品信息接口（好）
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> activityNewCategoryProduct(int pageNumber, String cid) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "activity/newCategoryProduct")
                .params("cid", cid)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 分类 列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> categoryGetProductByCatTo(int pageNumber, String cid) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "category/getProductByCatTo")
                .params("cid", cid)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.10.9订单列表（新）
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> orderDetailSpellGroupSuccess(int pageNumber, int type) {
        PostRequest<BaseResponseBean<BaseListBean<DataBean>>> post = OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "order/pageOrder");
            if (type != -1){
                post.params("type", type);
            }
        return post
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     *  登录获取验证码
     */
    public static final String userGetCode = "user/getCode";
    /**
     *  绑定手机获取验证码
     */
    public static final String userGetBindingCode = "user/getBindingCode";

    /**
     * 3.1.2获取验证码接口（好）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userGetCode(String mobile, String url) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + url)
                .params("mobile", mobile)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("type", 1)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }
    /**
     * 3.1.8获取绑定手机验证码接口（好）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userGetBindingCode(String mobile) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/getBindingCode")
                .params("mobile", mobile)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  登录
     */
    public static final String userLogin = "user/login";
    /**
     *  绑定手机号码
     */
    public static final String userBindingobile = "user/bindingobile";

    /**
     * 3.1.1登陆接口
     * 0验证码登录 1微信授权登录 2QQ授权登录
     * 微信和qq登录未完善
     */
    public static Observable<JSONObject> userLogin(String mobile, String code, String url) {
        return OkGo.<JSONObject>post(SERVLET_URL + url)
                .params("mobile", mobile)
                .params("code", code)
                .params("type", 0)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.5.2服务协议（好）
     */
    public static final String appServiceAgreement = SERVLET_URL + "app/serviceAgreement";


    /**
     * 3.5.4	分销或积分规则（新）
     */
    public static final String ruleGet = SERVLET_URL + "rule/get" + "?type=1001";

    /**
     * 3.5.4	分销或积分规则（新）
     */
    public static final String ruleGet2 = SERVLET_URL + "rule/get" + "?type=1002";

    /**
     * 统一H5
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> html(String url) {
        return OkGo.<BaseResponseBean<DataBean>>get(url)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.5.3帮助中心
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> appHelpCenter() {
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "app/helpCenter")
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.6.1添加银行卡
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> bankcardAddBankCard(String name, String cardNum, String bankName, String branchName) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "bankcard/addBankCard")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("name", name)
                .params("cardNum", cardNum)
                .params("bankName", bankName)
                .params("branchName", branchName)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.3.1超值拼团（好）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> activityCollage(int type) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "activity/collage")
                .params("type", type)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.8.5购物车批量移入收藏（新）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> cartMvecollect(int type, String collect_ids) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "cart/movecollect")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("type", type)
                .params("collect_ids", collect_ids)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.8.2删除购物车
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> cartDelCart(String cart_ids) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "cart/delCart")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("cart_ids", cart_ids)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 加入购物车
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userAddCart(String pid, String skuId, int num) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/addCart")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("pid", pid)
                .params("skuId", skuId)
                .params("num", num)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.4.3根据选中规格查询库存（好）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> productSearchPriceAndStock(String pid, String sku) {
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "product/searchPriceAndStock")
                .params("pid", pid)
                .params("sku", sku)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.12.2设置默认地址
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userSetDefaultAddress(String aid) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/setDefaultAddress")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("aid", aid)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.4.1商品详情（好）
     */
    public static Observable<Response<BaseResponseBean<ProductProductDetailBean>>> productProductDetail(String id) {
        return OkGo.<BaseResponseBean<ProductProductDetailBean>>post(SERVLET_URL + "product/productDetail")
                .params("pid", id)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<ProductProductDetailBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<ProductProductDetailBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<ProductProductDetailBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.6.3设置默认银行卡
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> bankcardSetDefaultCard(String cardId) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "bankcard/setDefaultCard")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("cardId", cardId)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }



    /**
     * 3.12.1	添加地址列表  更新地址
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userAddAddress(String rec_name, String rec_mobile,
                                                                                  String rec_address, String del_address, String id,
                                                                                  String sheng, String shi, String qu) {
        String url;
        if (!StringUtils.isEmpty(id)){
            url = "user/updateAddress";
        }else {
            url = "user/addAddress";
        }
        PostRequest<BaseResponseBean<DataBean>> post = OkGo.post(SERVLET_URL + url);
        if (!StringUtils.isEmpty(id)){
            post.params("id", id);
        }
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("rec_name", rec_name)
                .params("rec_mobile", rec_mobile)
                .params("rec_address", rec_address)
                .params("del_address", del_address)
                .params("sheng", sheng)
                .params("shi", shi)
                .params("qu", qu)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.4.2商品所有sku(好)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> productAllSku(String pid) {
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "product/allSku")
                .params("pid", pid)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.6.4删除银行卡
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> bankcardDelCard(String cardId) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "bankcard/delCard")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("cardId", cardId)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

     /**
     * 3.7.1店铺首页logo信息和推荐商品
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> businessMain(String bid) {
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "business/main")
                .params("bid", bid)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

     /**
     * 3.11.8收藏商品或是店铺
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userCollect(String id, int type) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/collect")
                .params("id", id)
                .params("type", type)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

     /**
     * 3.11.3提现页面
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userWithDraw() {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/withDraw")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

     /**
     * 3.8.4修改sku
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> cartModifyCartSku(String id, String skuid) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "cart/modifyCartSku")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", id)
                .params("skuid", skuid)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


     /**
     * 3.10.4	订单详细（新）
      */
    public static Observable<Response<BaseResponseBean<DataBean>>> orderDetail(String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "order/detail")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


     /**
     *  3.10.5待付款（新）
      */
    public static Observable<JSONObject> orderPayment(String id, int type) {
        return OkGo.<JSONObject>post(SERVLET_URL + "order/payment")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", id)
                .params("type", type)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }


     /**
     *  3.12.3删除地址
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userDelAddress(String aid) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/delAddress")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("aid", aid)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


     /**
     *   退款详情id
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> orderShutDown(String aid) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "order/shutDown")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", aid)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }



     /**
     *  3.8.3修改购物车数量
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> cartModifyCartNum(int number, String skuid, String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "cart/modifyCartNum")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("skuid", skuid)
                .params("number", number)
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

     /**
     * 3.11.4提现接口
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userWithDrawMoney(String money, String withDrawMoney, String bankCardId) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/withDrawMoney")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("money", money)
                .params("wmoney", withDrawMoney)
                .params("bankCardId", bankCardId)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


     /**
     * 3.10.1提交订单
     */
    public static Observable<JSONObject> orderSubmitOrder(String addressId, int payment, String sku_num,
                                                          int type, int flag, int coj, String orderNo, String id, int isCredited) {
        PostRequest<JSONObject> post = OkGo.post(SERVLET_URL + "order/submitOrder");
        if (flag != -1 ){
            post.params("flag", flag);
        }
        if (!StringUtils.isEmpty(orderNo)){
            post.params("orderNo", orderNo);
        }
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("addressId", addressId)
//                .params("payment", payment)
                .params("sku_num", sku_num)
                .params("type", type)
                .params("coj", coj)
                .params("productId", id)
                .params("cartId", id)
                .params("credit", isCredited)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

     /**
     * 3.11.10个人信息接口(我的资料)
     */
    public static Observable<JSONObject> userUserInfo() {
        return OkGo.<JSONObject>post(SERVLET_URL + "user/userInfo")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.11.11修改个人信息（我的资料）
     */
    public static Observable<JSONObject> userModifyUserInfo(String head, String nickname, int sex) {
        PostRequest<JSONObject> post = OkGo.<JSONObject>post(SERVLET_URL + "user/modifyUserInfo");
        if (!StringUtils.isEmpty(head)){
            post.params("head", new File(head));
        }
        if (!StringUtils.isEmpty(nickname)){
            post.params("nickname", nickname);
        }
        if (sex != 0){
            post.params("sex", sex);
        }
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 秒杀活动商品
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> activitySecKillProd(String aid) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "activity/secKillProd")
                .params("aid", aid)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }



    /**
     * 3.11.5提现记录
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> userWithDrawRecord() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "user/withDrawRecord")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }



    /**
     * 3.10.2拼团成功（新）
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> orderDetailGroupBookingSuccess(String id) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "order/detailGroupBookingSuccess")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("orderId", id)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }



    /**
     * 3.8.1购物车列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> cartCartList() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "cart/cartList")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     *  分类右边
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> categoryGetSecondCategoryByParent(String cid) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "category/getSecondCategoryByParent")
                .params("catOneId", cid)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 秒杀活动列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> activitySecKillTime() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "activity/secKillTime")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.3.2新品专区获取分类（好）
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> activityNewProductCategory() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "activity/newProductCategory")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 分类
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> categoryCatOneList() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "category/catOneList")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.6.2银行卡列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> bankcardCardList() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "bankcard/cardList")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.3.4优惠满赠满减活动Banner图（好）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> activitySpecialAreaBanner(String activityId) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "activity/specialAreaBanner")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("activityId", activityId)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取未读消息
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userUnread() {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/unread")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.10.8确认收货（新）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> orderConfirmReceipt(String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "order/confirmReceipt")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  售后详情
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> orderShowRefund(String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "order/showRefund")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  生成海报
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> appAboutUs() {
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "app/aboutUs")
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 查看物流
     */
    public static Observable<JSONObject> ordershowExpressInfo(String id) {
        return OkGo.<JSONObject>post(SERVLET_URL + "order/showExpressInfo")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", id)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }



    /**
     * 3.7.2店铺的商品列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> businessBusinessProduct(int pagerNumber, String bid) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "business/page")
                .params("pageNumber", pagerNumber)
                .params("pageSize", Constants.pageSize)
                .params("bid", bid)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.3.5优惠满赠满减数据接口(好)
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> activitySpecialArea(int pagerNumber, String activityId) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "activity/specialArea")
                .params("pageNumber", pagerNumber)
                .params("pageSize", Constants.pageSize)
                .params("activityId", activityId)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.10.7申请退款（新）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> orderrefund(String id, String content, List<LocalMedia> localMediaList) {
        PostRequest<BaseResponseBean<DataBean>> post = OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "order/refund");
        if (localMediaList.size() != 0){
            for (int i = 0;i < localMediaList.size();i++){
                LocalMedia media = localMediaList.get(i);
                String compressPath = media.getCompressPath();
                post.params("image" + (i + 1), compressPath);
            }
        }
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("id", id)
                .params("content", content)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.10.9申请退货（新）
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> orderSalesReturn(String orderId, List<LocalMedia> localMediaList, String content, String jsonOrderDetails, int state,
                                                                                    int cargoType, String refundAmount, String logisticsOrder) {
        PostRequest<BaseResponseBean<DataBean>> post = OkGo.post(SERVLET_URL + "order/salesReturn");
        if (localMediaList.size() != 0){
            for (int i = 0;i < localMediaList.size();i++){
                LocalMedia media = localMediaList.get(i);
                String compressPath = media.getCompressPath();
                post.params("image" + (i + 1), new File(compressPath));
            }
        }
        if (!StringUtils.isEmpty(logisticsOrder)){
            post.params("logisticsOrder", logisticsOrder);
        }
        if (!StringUtils.isEmpty(jsonOrderDetails)){
            post.params("jsonOrderDetails", jsonOrderDetails);
        }
        if (!StringUtils.isEmpty(refundAmount)){
            post.params("refundAmount", refundAmount);
        }
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("orderId", orderId)
                .params("content", content)
                .params("state", state)
                .params("cargoType", cargoType)
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


}