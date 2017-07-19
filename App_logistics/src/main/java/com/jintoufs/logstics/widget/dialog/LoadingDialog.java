package com.jintoufs.logstics.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jintoufs.logstics.R;

public class LoadingDialog extends Dialog {
	private Context context;
	private String msg;

	public LoadingDialog(Context context, String msg) {
		super(context);
		this.context = context;
		this.msg = msg;
	}

	/**
	 * progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public Dialog createLoadingDialog() {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

		loadingDialog.setCancelable(true);
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		return loadingDialog;
	}

	public void updateDialogMsg(String msg) {

	}
}
