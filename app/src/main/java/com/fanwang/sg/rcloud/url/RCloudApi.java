package com.fanwang.sg.rcloud.url;

import com.fanwang.sg.callback.JsonConvert;
import com.lzy.okgo.OkGo;
import com.lzy.okrx2.adapter.ObservableBody;
import org.json.JSONObject;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/4.
 */

public class RCloudApi {

    public static final String SERVLET_URL = "http://api.cn.ronghub.com/";

    /**
     * 获取用户的token
     */
    public static final String getToken = SERVLET_URL + "user/getToken.json";

    public static Observable<JSONObject> getToken(String userId, String name, String portraitUri){
        return OkGo.<JSONObject>post(RCloudApi.getToken)
                .params("userId", userId)
                .params("name", name)
                .params("portraitUri", portraitUri)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 创建群
     */
    public static final String createGroup = SERVLET_URL + "group/create.json";

    /**
     * 加入群组
     */
    public static final String joinGroup = SERVLET_URL + "group/join.json";



}
