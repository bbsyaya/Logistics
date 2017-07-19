package com.jintoufs.logstics.activites;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jintoufs.logstics.R;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.entity.Constants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class RectPhotoActivity extends BaseActivity implements SurfaceHolder.Callback {
	private static final String tag = "yan";
	private boolean isPreview = false;
	private SurfaceView mPreviewSV = null; // 棰勮SurfaceView
	private SurfaceHolder mySurfaceHolder = null;
	private ImageButton mPhotoImgBtn = null;
	private Camera myCamera = null;
	private Bitmap mBitmap = null;
	private AutoFocusCallback myAutoFocusCallback = null;
	private long m_TaskDetailId;
	private int m_Step;
	private String dataTake;
	private Display display;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏无标�?
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window myWindow = this.getWindow();
		myWindow.setFlags(flag, flag);

		setContentView(R.layout.activity_rect_photo);

		Bundle bundle = getIntent().getBundleExtra("BUNDLE_KEY_ARGS");
		m_TaskDetailId = bundle.getLong("TaskDetailId");
		m_Step = bundle.getInt("step");

		// 获取屏幕信息
		WindowManager mManger = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		display = mManger.getDefaultDisplay();

		// 初始化SurfaceView
		mPreviewSV = (SurfaceView) findViewById(R.id.previewSV);
		mySurfaceHolder = mPreviewSV.getHolder();
		mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);// translucent半�?�明
															// transparent�?
		mySurfaceHolder.addCallback(this);
		mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// 自动聚焦变量回调
		//myAutoFocusCallback = new AutoFocusCallback() {
        //
		//	public void onAutoFocus(boolean success, Camera camera) {
		//		// TODO Auto-generated method stub
		//		if (success)// success表示对焦成功
		//		{
		//			Log.i(tag, "myAutoFocusCallback: success...");
		//			// myCamera.setOneShotPreviewCallback(null);
		//		} else {
		//			// 未对焦成�?
		//			Log.i(tag, "myAutoFocusCallback:未对焦成�?");
        //
		//		}
		//	}
		//};

		mPhotoImgBtn = (ImageButton) findViewById(R.id.photoImgBtn);
		// 手动设置拍照ImageButton的大小为120×120,原图片大小是64×64
		LayoutParams lp = mPhotoImgBtn.getLayoutParams();
		lp.width = 120;
		lp.height = 120;
		mPhotoImgBtn.setLayoutParams(lp);
		mPhotoImgBtn.setOnClickListener(new PhotoOnClickListener());
		mPhotoImgBtn.setOnTouchListener(new MyOnTouchListener());
	}

	/* 下面三个是SurfaceHolder.Callback创建的回调函�? */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	// 当SurfaceView/预览界面的格式和大小发生改变时，该方法被调用
	{
		// TODO Auto-generated method stub
		Log.i(tag, "SurfaceHolder.Callback:surfaceChanged!");
		initCamera();

	}

	public void surfaceCreated(SurfaceHolder holder)
	// SurfaceView启动�?/初次实例化，预览界面被创建时，该方法被调�?
	{
		// TODO Auto-generated method stub

		try {
			myCamera = Camera.open();
			myCamera.setPreviewDisplay(mySurfaceHolder);
			Log.i(tag, "SurfaceHolder.Callback: surfaceCreated!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (null != myCamera) {
				myCamera.release();
				myCamera = null;
			}
			e.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	// �?毁时被调�?
	{
		// TODO Auto-generated method stub
		Log.i(tag, "SurfaceHolder.Callback锛歋urface Destroyed");
		if (null != myCamera) {
			myCamera.setPreviewCallback(null);
			myCamera.stopPreview();
			isPreview = false;
			myCamera.release();
			myCamera = null;
		}
	}

	// 初始化相�?
	public void initCamera() {
		if (isPreview) {
			myCamera.stopPreview();
		}
		if (null != myCamera) {
			Camera.Parameters myParam = myCamera.getParameters();
			myParam.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式

			// 设置大小和方向等参数
			//myParam.setPictureSize(1280, 720);
			//myParam.setPreviewSize(1280, 720);
            myParam.setPreviewSize(display.getWidth(), display.getHeight());
            List<Camera.Size> pszize = myParam.getSupportedPictureSizes();// 取得相机所支持多少图片大小的个数
            if (null != pszize && 0 < pszize.size()) {
                int height[] = new int[pszize.size()];// 声明一个数组
                Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                for (int i = 0; i < pszize.size(); i++) {
                    Camera.Size size = (Camera.Size) pszize.get(i);
                    int sizeheight = size.height;
                    int sizewidth = size.width;
                    height[i] = sizeheight;
                    map.put(sizeheight, sizewidth);

                }
                Arrays.sort(height);
                myParam.setPictureSize(map.get(height[1]), height[1]);
            }else {
                myParam.setPictureSize(display.getWidth(), display.getHeight());
            }
            myParam.set("rotation", 90);
			myCamera.setDisplayOrientation(90);
			//myParam.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			myCamera.setParameters(myParam);
			myCamera.startPreview();
			//myCamera.autoFocus(myAutoFocusCallback);
			isPreview = true;
		}
	}

	/* 为了实现拍照的快门声音及拍照保存照片�?要下面三个回调变�? */
	ShutterCallback myShutterCallback = new ShutterCallback()
	// 快门按下的回调，在这里我们可以设置类似播放�?�咔嚓�?�声之类的操作�?�默认的就是咔嚓�?
	{

		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i(tag, "myShutterCallback:onShutter...");

		}
	};
	PictureCallback myRawCallback = new PictureCallback()
	// 拍摄的未压缩原数据的回调,可以为null
	{

		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(tag, "myRawCallback:onPictureTaken...");

		}
	};
	PictureCallback myJpegCallback = new PictureCallback()
	// 对jpeg图像数据的回调处理，最重要的一个回调
	{

		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(tag, "myJpegCallback:onPictureTaken...");
			if (null != data) {
				mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位�?
				myCamera.stopPreview();
				isPreview = false;
				// 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation",
				// 90)失效。图片竟然不能旋转了，故这里要旋转下
				Matrix matrix = new Matrix();
				matrix.postRotate((float) 90.0);
				Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
						mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
				// 保存图片到sdcard
				if (null != rotaBitmap) {
					saveJpeg(rotaBitmap);
				}

				// 再次进入预览
				myCamera.startPreview();
				isPreview = true;
			}
			
		}
	};

	// 拍照按键的监�?
	public class PhotoOnClickListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isPreview && myCamera != null) {
				myCamera.takePicture(null, null, myJpegCallback);
			}
		}
	}

	/* 给定�?个Bitmap，进行保�? */
	public void saveJpeg(Bitmap bm) {
		String savePath = Constants.STORE_URL;
		File folder = new File(savePath);
		if (!folder.exists()) // 如果文件夹不存在则创�?
		{
			folder.mkdir(); 
		}

		switch (m_Step){
			case 1://撤防
				dataTake=String.valueOf(m_TaskDetailId)+"_withdrawImg";
				break;
			case 2://吞没卡检查
				dataTake=String.valueOf(m_TaskDetailId)+"_swallowedCardImg";
				break;
			case 4://检查机器
				dataTake=String.valueOf(m_TaskDetailId)+"_machineImg";
				break;
			case 6://关闭保险箱
				dataTake=String.valueOf(m_TaskDetailId)+"_closeDoorImg";
				break;
			case 9://安防
				dataTake=String.valueOf(m_TaskDetailId)+"_safetyProtectionImg";
				break;
			case 10://技防
				dataTake=String.valueOf(m_TaskDetailId)+"_technicalProtectionImg";
				break;
			case 11://卫生
				dataTake=String.valueOf(m_TaskDetailId)+"_sanitationImg";
				break;
			case 12://物業巡檢
				dataTake=String.valueOf(m_TaskDetailId)+"_inspectionImg";
				break;
			case 13://自拍存留照
				dataTake=String.valueOf(m_TaskDetailId)+"_photo";
				break;

		}
		String jpegName = savePath + dataTake + ".jpg";
		Log.i(tag, "saveJpeg:jpegName--" + jpegName);
		// File jpegFile = new File(jpegName);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jpegName));

			// //如果�?要改变大�?(默认的是�?960×�?1280),如改成宽600×�?800
			// Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bm.recycle();
			mBitmap.recycle();
			bos.flush();
			bos.close();
			Log.i(tag, "saveJpeg：存储完毕！");
			
			Intent intent=new Intent();
			intent.putExtra("imgFile", jpegName);
			setResult(1,intent);
			finish();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(tag, "saveJpeg:存储失败�?");
			e.printStackTrace();
		}
	}

	/* 为了使图片按钮按下和弹起状�?�不同，采用过滤颜色的方�?.按下的时候让图片颜色变淡 */
	public class MyOnTouchListener implements OnTouchListener {

		public final float[] BT_SELECTED = new float[] { 2, 0, 0, 0, 2, 0, 2,
				0, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 1, 0 };

		public final float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0, 0,
				1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_NOT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());

			}
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(myCamera != null && event.getRepeatCount() == 0 ) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_CAMERA: // 拍照键
				//case KeyEvent.ACTION_DOWN: // 手柄按键

					myCamera.takePicture(null, null, myJpegCallback);
					// camera.startPreview();必须写在onPictureTaken 方法内部，因为 takePicture 内部是另开了一条线程异步的完成保存照片等操作。
					// 虽然takePicture 方法完成了，但是并不代表其内部的工作全部完成，也不代表摄像头以及从上一次“拍照”任务中工作完毕
					break;
				default:
					break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed()
	// 无意中按返回键时要释放内�?
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(0);
		RectPhotoActivity.this.finish();
		
	}
}
