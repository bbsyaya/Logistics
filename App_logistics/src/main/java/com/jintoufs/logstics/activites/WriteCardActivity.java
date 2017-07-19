package com.jintoufs.logstics.activites;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseRfidActivity;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.reader.WriteCardInfo;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class WriteCardActivity extends BaseRfidActivity {
	private AppContext appContext;
	private TextView m_txtCardNum;
	private Button m_btnWriteCard;
	private long lastPressTime = 0;
	private String m_CardNum = "";
	private String m_Rfid = "";
	private int m_SiteId = -1;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = AppContext.getInstance();
		user = appContext.getLoginUser();
		setContentView(R.layout.activity_write_card);

		m_txtCardNum = (TextView) findViewById(R.id.txtCardNum);
		m_btnWriteCard = (Button) findViewById(R.id.btnOK);

		m_SiteId = getIntent().getIntExtra("siteId", -1);
		InitWriteCard();
	}

	protected void onResume() {
		super.onResume();
		showWaitDialog();
		Api.isCashboxExists(user.getKeyStr(), mExistCheckHandler);
		if (mReader != null) {
			mReader.StartWait();
		}
	}

	protected void onPause() {
		super.onPause();

		if (mReader != null && mReader.IsAlive()) {
			mReader.signOut();
		}
	}

	private final Handler showWriteCardResultHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int status = msg.getData().getInt("status");
			if (status < 0) {
				String msgInfo = msg.getData().getString("info");
				AppContext.showToast(msgInfo);
				m_btnWriteCard.setEnabled(true);
				return;
			}

			Api.AddCashbox(user.getKeyStr(),m_Rfid, m_CardNum, m_SiteId, mSubmitHandler);
		}
	};

	private final AsyncHttpResponseHandler mExistCheckHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				hideWaitDialog();
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					m_txtCardNum.setText(ajaxMsg.getDatas().toString());
					m_CardNum=ajaxMsg.getDatas().toString();
				} else {
					showToast(ajaxMsg.getMessage(), 0, 1);
				}
			} catch (Exception e) {
				hideWaitDialog();
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

	private final AsyncHttpResponseHandler mSubmitHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				hideWaitDialog();
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					SubmitCardCallback(true);
				} else {
					SubmitCardCallback(false);
				}
			} catch (Exception e) {
				hideWaitDialog();
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

	@SuppressLint("NewApi")
	public void btnOK_OnClick(View view) {
		appContext.setWriteCardInfo(0, "");
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				WriteCardInfo wci = appContext.getWriteCardInfo();
				if (wci.getStatus() > 0 || wci.getStatus() < 0) {
					timer.cancel();
					Bundle bundle = new Bundle();
					bundle.putInt("status", wci.getStatus());
					bundle.putString("info", wci.getInfo());
					Message msg = new Message();
					msg.setData(bundle);
					showWriteCardResultHandler.sendMessage(msg);
				}
			}
		}, 0, 200);

		m_Rfid = WriteCard(m_CardNum);
	}

	private void SubmitCardCallback(boolean isSucc) {
		if (!isSucc) {
			m_btnWriteCard.setEnabled(true);
			AppContext.showToast("写卡失败");
			return;
		}

		m_btnWriteCard.setEnabled(true);
		AppContext.showToast("写卡成功");
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (event.getDownTime() - lastPressTime < 300) {
			lastPressTime = event.getDownTime();
			return true;
		}

		lastPressTime = event.getDownTime();

		if (keyCode == KeyEvent.KEYCODE_FOCUS && event.getRepeatCount() == 0) {
			btnOK_OnClick(null);
			return true;
		}

		return false;
	}
}