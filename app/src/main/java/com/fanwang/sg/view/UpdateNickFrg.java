package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FUpdateNickBinding;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/3.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  修改昵称
 */

public class UpdateNickFrg extends BaseFragment<BasePresenter, FUpdateNickBinding> implements IBaseView{

    private String nickname;

    public static UpdateNickFrg newInstance() {
        Bundle args = new Bundle();
        UpdateNickFrg fragment = new UpdateNickFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        nickname = bundle.getString("nickname");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_update_nick;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.update_nick), getString(R.string.complete2));
        if (!nickname.equals("null")){
            mB.etEdit.setText(nickname);
        }
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        final String trim = mB.etEdit.getText().toString().trim();
        if (StringUtils.isEmpty(trim)){
            showToast(getString(R.string.error_nick_null));
            return;
        }
        CloudApi.userModifyUserInfo(null, trim, 0)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject object) {
                        if (object.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject userInfoObj = User.getInstance().getUserInfoObj();
                            try {
                                userInfoObj.put("nickname", trim);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            act.onBackPressed();
                        }
                        showToast(object.optString("desc"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        UpdateNickFrg.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

}
