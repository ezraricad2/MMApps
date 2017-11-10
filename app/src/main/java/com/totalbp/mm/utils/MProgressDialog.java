
package com.totalbp.mm.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.totalbp.mm.R;

public class MProgressDialog extends Dialog {

    private static MProgressDialog mProgressDialog;

    private Context context;

    private LayoutInflater inflater;

    public MProgressDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
        initUI();
    }

    public MProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initUI();
    }

    private void initUI() {
        inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_dialog, null);
        setContentView(v);
    }

    public static void showProgressDialog(Context context) {
        showProgressDialog(context, true);
    }

    public static void showProgressDialog(Context context, boolean isCancelable) {
        showProgressDialog(context, isCancelable, null);
    }

    public static void showProgressDialog(Context context, boolean isCancelable,
                                          OnCancelListener listener) {
        mProgressDialog = new MProgressDialog(context);
        if (mProgressDialog != null) {
            Activity activity = mProgressDialog.getOwnerActivity();
            if (activity != null && !activity.isFinishing()) {
                mProgressDialog.show();
            }
        }
        mProgressDialog.setCancelable(isCancelable);
        if (listener != null) {
            mProgressDialog.setOnCancelListener(listener);
        }
        mProgressDialog.setCanceledOnTouchOutside(false);//Mengatur tepi tidak dapat menunjuk
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            Activity activity = mProgressDialog.getOwnerActivity();
            if (activity != null && !activity.isFinishing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
