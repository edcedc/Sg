package com.fanwang.sg.rcloud.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseActivity;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.callback.NewsCallback;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.rcloud.RCloudTool;
import com.fanwang.sg.utils.AndroidBugWorkaround;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.sofia.Sofia;

import java.util.Collection;
import java.util.Iterator;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 作者：yc on 2018/10/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ConversationActivity extends AppCompatActivity implements RongIM.ConversationBehaviorListener{

    private final String TextTypingTitle = "对方正在输入...";
    private final String VoiceTypingTitle = "对方正在讲话...";

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;

    private String userId;
    private String sName;
    private String userHead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_customer_service);
        findViewById(R.id.status_view).setVisibility(View.GONE);
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        mAppCompatActivity.setSupportActionBar(toolbar);
        mAppCompatActivity.getSupportActionBar().setTitle("客服");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RongContext.getInstance().setConversationBehaviorListener(this);
        //targetId:单聊即对方ID，群聊即群组ID
        userId = getIntent().getData().getQueryParameter("targetId");
        //获取昵称
        sName = getIntent().getData().getQueryParameter("title");
        //获取头像
        userHead = getIntent().getData().getQueryParameter("head");

        LogUtils.e(userId, sName, userHead);

        RCloudTool.setUserInfoProvider(User.getInstance().getUserId(), "xxx", "http://ww3.sinaimg.cn/large/006XNEY7gy1fwsd6mz2jfj30j20v5gsy.jpg");

        RCloudTool.getInstance().OnSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                if (StringUtils.isEmpty(userHead)){
                    userHead = "http://ww3.sinaimg.cn/large/0073tLPGgy1fwsc616ye3j30kq0vajvy.jpg";
                }else {
                    userHead = CloudApi.SERVLET_IMG_URL + userHead;
                }
                RCloudTool.setUserInfoProvider(User.getInstance().getUserId(), "xxx", "http://ww3.sinaimg.cn/large/006XNEY7gy1fwsd6mz2jfj30j20v5gsy.jpg");
                RCloudTool.getInstance().refreshUserInfoCache(new UserInfo(User.getInstance().getUserId(), "xxx", Uri.parse("http://ww3.sinaimg.cn/large/006XNEY7gy1fwsd6mz2jfj30j20v5gsy.jpg")));
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        });
    }



    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }



}
