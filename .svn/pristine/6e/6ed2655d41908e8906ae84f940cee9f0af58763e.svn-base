package com.jintoufs.base;

import java.io.File;
import java.io.IOException;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android_serialport_api.SerialPort;

import com.AndroidVersions;
import com.fntech.m10a.gpio.M10A_GPIO;
import com.jintoufs.AppContext;
import com.jintoufs.reader.model.InventoryBuffer;
import com.jintoufs.reader.model.ReaderSetting;
import com.jintoufs.reader.server.ReaderBase;
import com.jintoufs.reader.server.ReaderHelper;
import com.jintoufs.utils.M10_GPIO;

/**
 * RFID 功能相关界面
 */
public abstract class BaseRfidActivity extends BaseActivity {
	public SerialPort mSerialPort = null;

	public ReaderHelper mReaderHelper;
	public ReaderBase mReader;
	public static ReaderSetting m_curReaderSetting;
	public static InventoryBuffer m_curInventoryBuffer;

	@Override
	protected void onDestroy() {
		stopInventory();
		EndWork();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	protected void set() {
		// Loger.disk_log("调试", "开始盘询，设置天线", "M10_U8");
		
		PrepareWork();
		try {
			mReaderHelper = ReaderHelper.getDefaultHelper();
			mReaderHelper.setReader(mSerialPort.getInputStream(), mSerialPort.getOutputStream());
			mReader = mReaderHelper.getReader();
			m_curReaderSetting = mReaderHelper.getCurReaderSetting();
			m_curInventoryBuffer = mReaderHelper.getCurInventoryBuffer();
			m_curInventoryBuffer.clearTagMap();
			m_curInventoryBuffer.clearInventoryResult();
			m_curInventoryBuffer.clearInventoryRealResult();
			//set();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		m_curInventoryBuffer.clearInventoryPar();

		m_curInventoryBuffer.bLoopCustomizedSession = true;

		m_curInventoryBuffer.btSession = (byte) (getSessionState() & 0xFF);
		m_curInventoryBuffer.btTarget = (byte) (getFlagState() & 0xFF);

		m_curInventoryBuffer.lAntenna.add((byte) 0x01);

		m_curInventoryBuffer.bLoopInventoryReal = true;

		m_curInventoryBuffer.btRepeat = (byte) 1;

		m_curInventoryBuffer.clearInventoryRealResult();
		mReaderHelper.setInventoryFlag(true);
		mReaderHelper.clearInventoryTotal();

		byte btWorkAntenna = m_curInventoryBuffer.lAntenna.get(m_curInventoryBuffer.nIndexAntenna);
		if (btWorkAntenna < 0)
			btWorkAntenna = 1;

		mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);
		// Loger.disk_log("调试", "开始盘询，天线设置完毕", "M10_U8");
		m_curReaderSetting.btWorkAntenna = btWorkAntenna;
		
		if (mReader != null) {
			if (!mReader.IsAlive())
				mReader.StartWait();
		}
	}
	
	protected void set(byte outPower) {
		// Loger.disk_log("调试", "开始盘询，设置天线", "M10_U8");
		
		PrepareWork();
		try {
			mReaderHelper = ReaderHelper.getDefaultHelper();
			mReaderHelper.setReader(mSerialPort.getInputStream(), mSerialPort.getOutputStream());
			mReader = mReaderHelper.getReader();
			m_curReaderSetting = mReaderHelper.getCurReaderSetting();
			m_curInventoryBuffer = mReaderHelper.getCurInventoryBuffer();
			m_curInventoryBuffer.clearTagMap();
			m_curInventoryBuffer.clearInventoryResult();
			m_curInventoryBuffer.clearInventoryRealResult();
			//set();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		m_curInventoryBuffer.clearInventoryPar();

		m_curInventoryBuffer.bLoopCustomizedSession = true;

		m_curInventoryBuffer.btSession = (byte) (getSessionState() & 0xFF);
		m_curInventoryBuffer.btTarget = (byte) (getFlagState() & 0xFF);

		m_curInventoryBuffer.lAntenna.add((byte) 0x01);

		m_curInventoryBuffer.bLoopInventoryReal = true;

		m_curInventoryBuffer.btRepeat = (byte) 1;

		m_curInventoryBuffer.clearInventoryRealResult();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mReader.setOutputPower(m_curReaderSetting.btReadId, outPower);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mReaderHelper.setInventoryFlag(true);
		mReaderHelper.clearInventoryTotal();

		byte btWorkAntenna = m_curInventoryBuffer.lAntenna.get(m_curInventoryBuffer.nIndexAntenna);
		if (btWorkAntenna < 0)
			btWorkAntenna = 1;

		mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);
		// Loger.disk_log("调试", "开始盘询，天线设置完毕", "M10_U8");
		m_curReaderSetting.btWorkAntenna = btWorkAntenna;
		
		if (mReader != null) {
			if (!mReader.IsAlive())
				mReader.StartWait();
		}
	}

	protected void stopInventory() {
		if(mReaderHelper!=null){
			mReaderHelper.setInventoryFlag(false);
		}
		
		if(mReader!=null){
			if(mReader.IsAlive()){
				mReader.signOut();
				
			}
			mReader.clearBuffer();
			mReader=null;
		}
		
		
		if(m_curInventoryBuffer!=null){
			m_curInventoryBuffer.bLoopInventory = false;
			m_curInventoryBuffer.bLoopInventoryReal = false;
			m_curInventoryBuffer.bLoopInventory = false;
			m_curInventoryBuffer.bLoopInventoryReal = false;
			m_curInventoryBuffer.clearTagMap();
			m_curInventoryBuffer.clearInventoryResult();
			m_curInventoryBuffer.clearInventoryRealResult();
			
			if(m_curInventoryBuffer.lsTagList!=null){
				m_curInventoryBuffer.lsTagList.clear();
			}
		}
		
		EndWork();
	}

	public int getSessionState() {
		int state = AppContext.get("_session", 0);
		return state;
	}

	public int getFlagState() {
		int state = AppContext.get("_flag", 0);
		return state;
	}

	public final void setViewEnable(final View view, final boolean en) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.setEnabled(en);
			}
		});
	}

	/**
	 * 模块上电，打开串口
	 */
	public void PrepareWork() {
		if (android.os.Build.VERSION.RELEASE.equals(AndroidVersions.V_4_0_3)) {
			M10_GPIO.R1000_PowerOn(); // 模块上电
			try {
				mSerialPort = new SerialPort(new File("dev/ttySAC2"), 115200, 0);
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (android.os.Build.VERSION.RELEASE.equals(AndroidVersions.V_5_1_1)) {
			M10A_GPIO.PowerOn(); // 模块上电
			try {
				M10A_GPIO._uhf_SwitchSerialPort();
				mSerialPort = new SerialPort(new File("/dev/ttyHSL0"), 115200, 0);
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(), "程序版本有误，请联系技术支持人员！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 模块下电，关闭串口
	 */
	public void EndWork() {
		if(mSerialPort!=null){
			mSerialPort.close();
			mSerialPort=null;
		}
		
		if (android.os.Build.VERSION.RELEASE.equals(AndroidVersions.V_4_0_3)) {
			M10_GPIO.R1000_PowerOFF();
		} else if (android.os.Build.VERSION.RELEASE.equals(AndroidVersions.V_5_1_1)) {
			M10A_GPIO.PowerOff();
		}
	}
}
