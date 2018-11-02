package com.fanwang.sg.view.act;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseActivity;
import com.fanwang.sg.base.BaseFragment;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 作者：yc on 2018/10/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class RongcloudListAct extends BaseActivity implements RongIM.ConversationBehaviorListener{

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected void initView() {
        setTitle("客服");

        RongContext.getInstance().setConversationBehaviorListener(this);

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_rong_list;
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
