package com.fanwang.sg.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.event.PayInEvent;
import com.fanwang.sg.utils.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	private TextView tvText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
//		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		tvText = (TextView) findViewById(R.id.tv_text);
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APPID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		/*if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
			Logger.e("onPayFinish, errCode = " + resp.errCode);
			tvText.setText("支付成功");
			finish();
			EventBus.getDefault().post(new PayInEvent());
		}*/
		switch (resp.errCode){
			case 0://支付成功
				LogUtils.e("onPayFinish, errCode = " + resp.errCode);
				tvText.setText("支付成功");
				ToastUtils.showShort( "支付成功");
				EventBus.getDefault().post(new PayInEvent(true));
				break;
			case -1:
				//支付失败
				ToastUtils.showShort( "支付失败");
				break;
			case -2:
				//支付取消
				ToastUtils.showShort( "支付取消");
				break;
		}
		finish();
		/*if (resp.errCode == 0){
			startAct();
		}else {
			ToastTool.showShortMsg(this, "支付失败");
			finish();
		}*/
	}

}