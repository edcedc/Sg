package com.fanwang.sg.rcloud;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.base.User;
import com.fanwang.sg.mar.MyApplication;
import com.fanwang.sg.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.helper.StringUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIMClientWrapper;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;

/**
 * Created by Administrator on 2017/8/26.
 */

public class RCloudTool {

    /**
     * 融云参数
     */
    public static final String RC_APP_KEY = Constants.RY_APPKEY;
    public static final String RC_APP_SECRET = Constants.RY_SECRET;

    //0是私聊 1是客户 界面
    public static int RC_TYPE = 0;

    public static void setRcType(int rcType) {
        RC_TYPE = rcType;
    }

    public static int getRcType() {
        return RC_TYPE;
    }

    /**
     * 融云客服ID
     */
    public static  String RC_APP_CUSTOMER = "";

    public static List<UserInfo> userList = new ArrayList<UserInfo>();
    public static List<Group> groupList = new ArrayList<Group>();

    private RCloudTool() {
    }

    private static RCloudTool instance;

    public static synchronized RCloudTool getInstance() {
        if (instance == null) {
            instance = new RCloudTool();
        }
        return instance;
    }

    public void connect(String token) {
        LogUtils.e(token);
        String packageName = AppUtils.getAppPackageName();
        String processName = ProcessUtils.getCurrentProcessName(); // 当前的进程名
        if (packageName.equals(processName)) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                            2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtils.e("onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
//                    Intent intent = new Intent(context, ChatListAct.class);
//                    context.startActivity(intent);
                    LogUtils.e("-------------------------融云连接成功------------------------------");
                    enterMenu();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.e("融云连接失败\n" + errorCode.getMessage());
                }
            });
        }
    }


    /**
     * 设置接收未读消息的监听器。
     *
     * @param listener          接收未读消息消息的监听器。
     *  接收指定会话类型的未读消息数。
     */
    public void setOnReceiveUnreadCountChangedListener(RongIM.OnReceiveUnreadCountChangedListener listener) {
        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(listener, Conversation.ConversationType.PRIVATE);
    }

    private void enterMenu() {
        RongIMClientWrapper.setOnReceiveMessageListener(new MyReceiveMessageListener());//设置消息接收监听器。
        setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
            @Override
            public void onMessageIncreased(int count) {
//                LogUtils.e("未读聊天记录count=" + count);
            }
        });
        //扩展功能自定义
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        //查找默认已注册模块
        for (IExtensionModule module : moduleList) {
            if (module instanceof DefaultExtensionModule) {
                defaultModule = module;
                break;
            }
        }
        if (defaultModule != null) {
            //移除已注册的默认模块，替换成自定义模块
            RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
            RongExtensionManager.getInstance().registerExtensionModule(new SealExtensionModule());
        }
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        });
        // 声明消息监听器，建议在 Application 中声明为成员变量。
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(final Message message, int arg1) {
                String targetId = message.getTargetId();
                /*List<OneGreetingsBean> been = DataSupport.where("userId = ?", targetId).find(OneGreetingsBean.class);
                if (been.size() == 0) {
                    OneGreetingsBean bean = new OneGreetingsBean();
                    bean.setUserId(targetId);
                    bean.setOne(true);
                    bean.save();
                }*/
                return false;
            }
        });
    }

    /**
     * 收到消息的处理。
     *
     * 收到的消息实体。
     * 剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
        @Override
        public boolean onReceived(Message message, int left) {
            Conversation.ConversationType conversationType = message.getConversationType();
            String senderUserId = message.getSenderUserId();
            LogUtils.e("senderUserId---------" + senderUserId);
            if (conversationType == Conversation.ConversationType.PRIVATE) {
//                LogUtils.e("这是单聊的消息------"+senderUserId);
            }
            return false;
        }
    }

    /**
     * 根据user_id去查找用户信息
     *
     * @param user_id 发送消息的用户id
     */
    private UserInfo findUserInfo(String user_id) {
        if (user_id.equals("sys")) {
            return new UserInfo(user_id, "系统消息", Uri.parse("http://static.plupros.com/TabsPro/server/r2/img/hao123.png"));
        }

        return null;
    }

    private void getRyService() {
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus status) {
                if (ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT == status) {
                    LogUtils.e("被顶号");
                }
                LogUtils.e("网络状态已经变化");
            }
        });


    }

    /**
     * 注册用户时的请求头
     *
     * @return
     */
    public static Map<String, String> getHeader() {
        String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳，从 1970 年 1 月 1 日 0 点 0 分 0 秒开始到现在的秒数。
        String Nonce = String.valueOf(Math.floor(Math.random() * 1000000));//随机数，无长度限制。
        String Signature = RCloudTool.sha1(RC_APP_SECRET + Nonce + Timestamp);//数据签名。
        Map<String, String> header = new HashMap();
        header.put("App-Key", RC_APP_KEY);
        header.put("Timestamp", Timestamp);
        header.put("Nonce", Nonce);
        header.put("Signature", Signature);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        return header;
    }

    /**
     * 注册融云用户时的参数
     *
     * @param userId
     * @param name
     * @param portraitUri
     * @return
     */
    public static Map<String, String> getParams(String userId, String name, String portraitUri) {
        Map<String, String> params = new HashMap();
        params.put("userId", userId);
        params.put("name", name);
        params.put("portraitUri", portraitUri);
        return params;
    }


    /**
     * 融云创建群组的参数
     *
     * @param userId    用户id
     * @param groupId   群组id
     * @param groupName 群组名
     * @return
     */
    public static Map<String, String> getGroupParams(String userId, String groupId, String groupName) {
        Map<String, String> params = new HashMap();
        params.put("userId", userId);
        params.put("groupId", groupId);
        params.put("groupName", groupName);
        return params;
    }


    /**
     * 获取到用户信息的时候，初始化融云用户
     *
     * @param context
     * @param id
     * @param name
     * @param portraitUri
     * @param token
     */
    public void InitUser(Context context, String id, String name, String portraitUri, String token) {
        if (StringUtils.isEmpty(id))
            id = "";
        if (StringUtils.isEmpty(name))
            name = "未设置";
        if (StringUtils.isEmpty(token))
            token = "";
//        if (!StringUtils.isEmpty(portraitUri) && !StringUtils.isEmpty(id) && !StringUtils.isEmpty(name)) {
        UserInfo info = null;
        if (!StringUtils.isEmpty(portraitUri)) {
            info = new UserInfo(id, name, Uri.parse(portraitUri));
        } else {
            info = new UserInfo(id, name, Uri.parse(""));
        }
            if (info != null) {
                RongIM.getInstance().refreshUserInfoCache(info);
                RongIM.getInstance().setCurrentUserInfo(info);
                RongIM.getInstance().setMessageAttachedUserInfo(true);
                RongContext.getInstance().setUserInfoAttachedState(true);
                RCloudTool.getInstance().addUserInfo(info);
                RCloudTool.getInstance().connect(token);
            }
//        }
    }

    public static void setUserInfoProvider(final String id, final String name, final String portraitUri) {
        //getSearchableWord
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(portraitUri)) {
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                @Override
                public UserInfo getUserInfo(String userId) {
                    //这里可能获取接口，进行批量设置头像，名字，id
                    return new UserInfo(id, name, Uri.parse(portraitUri));
//                return findUserById(userId);
                    // 根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                }

            }, true);
        }
    }


    public static String sha1(String data) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(data.getBytes());
            byte[] bits = md.digest();
            for (int i = 0; i < bits.length; i++) {
                int a = bits[i];
                if (a < 0) a += 256;
                if (a < 16) buf.append("0");
                buf.append(Integer.toHexString(a));
            }
        } catch (Exception e) {

        }
        return buf.toString();
    }

    /**
     * 融云添加管理用户
     *
     * @param info
     */
    public void addUserInfo(UserInfo info) {
        int len = userList.size();
        for (int i = 0; i < len; i++) {
            UserInfo info_ = userList.get(i);
            if (info_ != null && info.getUserId().equals(info_.getUserId())) {
                return;
            }
        }
        userList.add(info);
    }

    /**
     * 融云根据用户的id获取用户信息
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfo(String userId) {
        System.out.println("userId:" + userId);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        int len = userList.size();
        for (int i = 0; i < len; i++) {
            UserInfo info = userList.get(i);
            if (info != null && info.getUserId().equals(userId)) {
                return info;
            }
        }
        return null;
    }


    /**
     * 融云添加管理群组
     *
     * @param group
     */
    public void addGroup(Group group) {
        int len = groupList.size();
        for (int i = 0; i < len; i++) {
            Group group_ = groupList.get(i);
            if (group_ != null && group.getId().equals(group_.getId())) {
                return;
            }
        }
        groupList.add(group);
    }

    /**
     * 融云根据用户的id获取用户信息
     *
     * @param groupId
     * @return
     */
    public Group getGroup(String groupId) {
        System.out.println("userId:" + groupId);
        if (StringUtils.isEmpty(groupId)) {
            return null;
        }
        int len = groupList.size();
        for (int i = 0; i < len; i++) {
            Group group = groupList.get(i);
            if (group != null && group.getId().equals(groupId)) {
                return group;
            }
        }
        return null;
    }

    /**
     * 拉黑
     *
     * @param userId
     */
    public void addToBlacklist(String userId) {
        RongIM.getInstance().addToBlacklist(userId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                LogUtils.e("拉黑成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e(errorCode.getMessage().toString());
            }
        });
    }

    /**
     * 取消拉黑
     *
     * @param userId
     */
    public void removeFromBlacklist(String userId) {
        RongIM.getInstance().removeFromBlacklist(userId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                LogUtils.e("取消拉黑成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e(errorCode.getMessage().toString());
            }
        });
    }

    /**
     * 监听发送状态
     *
     * @param listener
     */
    public void OnSendMessageListener(RongIM.OnSendMessageListener listener) {
        RongIM.getInstance().setSendMessageListener(listener);
    }

    /**
     * 根据会话类型，清除目标 Id 的消息未读状态，回调方式获取清除是否成功。
     *
     *  会话类型。不支持传入 ConversationType.CHATROOM。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
     * @param callback         清除是否成功的回调。
     */
    public void clearMessagesUnreadStatus(final String targetId, final RongIMClient.ResultCallback<Boolean> callback) {
        RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, targetId, callback);
    }

    /**
     * 发送消息
     *
     *  PRIVATE 不知道为什么第一次发送成功我方不刷新页面
     * @param userId
     * @param textMessage
     * @param callback
     */
    public void ISendMessageCallback(String userId, TextMessage textMessage, IRongCallback.ISendMessageCallback callback) {
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, userId, textMessage, null, null, callback);
    }

    /**
     * 发送消息
     *
     * @param myMessage
     * @param callback
     */
    public void sendMessage(Message myMessage, IRongCallback.ISendMessageCallback callback) {
        RongIM.getInstance().sendMessage(myMessage, "我也不知道干嘛的，但文档要要填写", null, callback);
    }

    /**
     * 退出登录
     */
    public void RCloundExit() {
        RongIM.getInstance().getRongIMClient().logout();
    }

    /**
     * 调用客服
     *
     * @param id 客服ID
     */
    public void SCustomService(Activity act, String id) {
        if (RongIM.getInstance() == null)return;
        //首先需要构造使用客服者的用户信息
        CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
        CSCustomServiceInfo csInfo = csBuilder.nickName("融云").build();
        /**
         * 启动客户服聊天界面。
         *
         * @param context           应用上下文。
         * @param customerServiceId 要与之聊天的客服 Id。
         * @param title             聊天的标题，如果传入空值，则默认显示与之聊天的客服名称。
         * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
         */
        RongIM.getInstance().startCustomerServiceChat(act, id, "在线客服", csInfo);
    }

    /**
     * 修改 ConversationListFragment 列表
     *
     * @param callback
     */
    public void getConversationList(RongIMClient.ResultCallback<List<Conversation>> callback) {
        RongIM.getInstance().getConversationList(callback);
    }

    /**
     * 获取对方监听状态
     *
     * @param listener
     */
    public static void setTypingStatusListener(RongIMClient.TypingStatusListener listener) {
        RongIMClient.setTypingStatusListener(listener);
    }

    /**
     * 刷新用户缓存数据。
     *
     * @param userInfo 需要更新的用户缓存数据。
     */
    public static void refreshUserInfoCache(UserInfo userInfo) {
        RongIM.getInstance().refreshUserInfoCache(userInfo);
        RongIMClient.setRecallMessageListener(new RongIMClient.RecallMessageListener() {
            @Override
            public void onMessageRecalled(int messageId, RecallNotificationMessage recallNotificationMessage) {
                //根据 recallNotificationMessage 的内容进行界面刷新
            }
        });
    }
}
