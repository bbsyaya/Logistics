package com.jintoufs.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.utils.FileUtil;
import com.jintoufs.utils.ImageUtils;
import com.jintoufs.utils.StringUtils;
import com.jintoufs.utils.TDevice;
import com.jintoufs.utils.UIHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("NewApi")
public class OperationPubFragment extends Fragment {

	public static final int ACTION_TYPE_PHOTO = 1;

	protected static final String FILE_PATH = "/storage/sdcard1/hongtai/Camera/";

	ImageView mIvImage;

	private String theLarge, theThumbnail;
	private File imgFile;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.obj != null) {
				// 显示图片
				Bitmap bm = (Bitmap) msg.obj;
				if (bm != null) {
					mIvImage.setImageBitmap(bm);
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void handleSubmit() {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_no_internet);
			return;
		}
		if (!AppContext.getInstance().isLogin()) {
			UIHelper.showLoginActivity(getActivity());
			return;
		}
		// TODO 发送消息
		getActivity().finish();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_operation_pub, container, false);
		mIvImage = (ImageView) view.findViewById(R.id.iv_img);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle bundle = getArguments();
		if (bundle != null) {
		}

		goToSelectPicture(ACTION_TYPE_PHOTO);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnIntent) {
		if (resultCode != Activity.RESULT_OK){
			this.getActivity().finish();
			return;
		}
		new Thread() {
			private String selectedImagePath;

			@Override
			public void run() {
				Bitmap bitmap = null;

				if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) {
					if (imageReturnIntent == null)
						return;
					Uri selectedImageUri = imageReturnIntent.getData();
					if (selectedImageUri != null) {
						selectedImagePath = ImageUtils.getImagePath(selectedImageUri, getActivity());
					}

					if (selectedImagePath != null) {
						theLarge = selectedImagePath;
					} else {
						bitmap = ImageUtils.loadPicasaImageFromGalley(selectedImageUri, getActivity());
					}

					String imaName = FileUtil.getFileName(theLarge);
					if (imaName != null)
						bitmap = ImageUtils.loadImgThumbnail(getActivity(), imaName, MediaStore.Images.Thumbnails.MICRO_KIND);
					if (bitmap == null && !StringUtils.isEmpty(theLarge))
						bitmap = ImageUtils.loadImgThumbnail(theLarge, 600, 800);
				} else if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
					// 拍摄图片
					if (bitmap == null && !StringUtils.isEmpty(theLarge)) {
						bitmap = ImageUtils.loadImgThumbnail(theLarge, 600, 800);
					}
				}

				if (bitmap != null) {// 存放照片的文件夹
					String savePath = FILE_PATH;
					File savedir = new File(savePath);
					if (!savedir.exists()) {
						savedir.mkdirs();
					}

					String largeFileName = FileUtil.getFileName(theLarge);
					String largeFilePath = savePath + largeFileName;
					// 判断是否已存在缩略图
					if (largeFileName.startsWith("thumb_") && new File(largeFilePath).exists()) {
						theThumbnail = largeFilePath;
						imgFile = new File(theThumbnail);
					} else {
						// 生成上传的800宽度图片
						String thumbFileName = "thumb_" + largeFileName;
						theThumbnail = savePath + thumbFileName;
						if (new File(theThumbnail).exists()) {
							imgFile = new File(theThumbnail);
						} else {
							try {
								// 压缩上传的图片
								ImageUtils.createImageThumbnail(getActivity(), theLarge, theThumbnail, 800, 80);
								imgFile = new File(theThumbnail);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

					Message msg = new Message();
					msg.what = 1;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	private void goToSelectPicture(int position) {
		Intent intent;
		switch (position) {
		case ACTION_TYPE_PHOTO:
			// 判断是否挂载了SD卡
			String savePath = "";
			savePath = FILE_PATH;
			File savedir = new File(savePath);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
			// 没有挂载SD卡，无法保存文件
			if (StringUtils.isEmpty(savePath)) {
				AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
				return;
			}

			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
			File out = new File(savePath, fileName);
			Uri uri = Uri.fromFile(out);

			theLarge = savePath + fileName;// 该照片的绝对路径

			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
