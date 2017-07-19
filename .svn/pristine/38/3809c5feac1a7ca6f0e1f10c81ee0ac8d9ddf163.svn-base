package com.jintoufs.logstics.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.activites.OperationDetailActivity;
import com.jintoufs.logstics.activites.RectPhotoActivity;
import com.jintoufs.logstics.utils.TDevice;
import com.jintoufs.logstics.utils.UIHelper;

import static com.jintoufs.logstics.utils.UIHelper.BUNDLE_KEY_ARGS;

@SuppressLint("NewApi")
public class OperationPubFragment extends Fragment {

    public static final int ACTION_TYPE_PHOTO = 1;

    //protected static final String FILE_PATH = "/storage/sdcard1/hongtai/Camera/";
    protected static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";//获取SD卡根目录 ";

    private ImageView mIvImage;
    /*
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
	*/

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operation_pub, container, false);
        mIvImage = (ImageView) view.findViewById(R.id.iv_img);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

		Bundle bundle=getActivity().getIntent().getBundleExtra("BUNDLE_KEY_ARGS");
        if (checkCameraHardWare(getActivity())){
            Intent intent=new Intent(getActivity(),RectPhotoActivity.class);
            intent.putExtra(BUNDLE_KEY_ARGS, bundle);
            startActivityForResult(intent,0);
        }else{
            AppContext.showToastShort("没有相机存在");
        }

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnIntent) {
        //		if(requestCode!=0){
        //			this.getActivity().finish();
        //			return;
        //		}

        if (resultCode == ACTION_TYPE_PHOTO) {
            String imgFileName=imageReturnIntent.getStringExtra("imgFile");
            ((OperationDetailActivity) this.getActivity()).SetImgPath(imgFileName);
            //mIvImage.setImageBitmap(ImageUtils.getBitmapByPath(imgFileName));
            setPicToImageView(mIvImage, imgFileName);
        } else {
            this.getActivity().finish();
        }
    }

    private void setPicToImageView(ImageView imageView, String imageFile){
        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();
        BitmapFactory.Options opts = new BitmapFactory.Options();

        //设置这个，只得到Bitmap的属性信息放入opts，而不把Bitmap加载到内存中
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, opts);

        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        //取最大的比例，保证整个图片的长或者宽必定在该屏幕中可以显示得下
        int scale = Math.max(imageViewWidth / bitmapWidth, imageViewHeight / bitmapHeight);

        //缩放的比例
        opts.inSampleSize = scale;
        //内存不足时可被回收
        opts.inPurgeable = true;
        //设置为false,表示不仅Bitmap的属性，也要加载bitmap
        opts.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile, opts);
        imageView.setImageBitmap(bitmap);
    }

    /*检测相机是否存在*/
    private boolean checkCameraHardWare(Context context){
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    /*
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
    */
    @Override
    public void onResume() {
        super.onResume();
    }
}
