
package com.ustcinfo.mobile.platform.core.ui.widget;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.core.R;

public class MAlertDialog {

    private Context context;

    private String title;

    private String content;

    private AlertDialog alertDialog;

    private OnClickListener confrimClickListener;

    private OnClickListener cancelClickListener;

    private Button btn_confirm;

    private Button btn_cancel;

    private LinearLayout btnLayout;

    private boolean isShowing;

    private LinearLayout dialogView;

    /**
     * Constructor
     *
     * @param context
     */
    public MAlertDialog(Context context) {
        new MAlertDialog(context, true, true);
    }

    public MAlertDialog(Context context, boolean canelableTouchOutSide, boolean cancelable) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(this.context).create();
        alertDialog.setCancelable(cancelable);
        alertDialog.setCanceledOnTouchOutside(canelableTouchOutSide);
        alertDialog.show();

        alertDialog.setContentView(R.layout.layout_alert_dialog);
        btn_confirm = (Button) alertDialog.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
        btnLayout = (LinearLayout) alertDialog.findViewById(R.id.btn_view);
        dialogView = (LinearLayout) alertDialog.findViewById(R.id.all_view);
    }

    public MAlertDialog setDialogCanceledOnTouchOutside(boolean cancelable) {
        alertDialog.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public MAlertDialog setDialogCancelable(boolean cancelable) {
        alertDialog.setCancelable(cancelable);
        return this;
    }

    /**
     * set title for alert dialog
     *
     * @param title
     */
    public MAlertDialog setTitle(String title) {
        if(!TextUtils.isEmpty(title))
             this.title = title;
        return this;
    }

    /**
     * set content for alert dialog
     *
     * @param text
     */
    public MAlertDialog setContent(String text) {
        if(!TextUtils.isEmpty(text))
            this.content = text;
        return this;
    }

    public void show() {
        TextView tv_title = (TextView) alertDialog.findViewById(R.id.alert_title);
        TextView tv_content = (TextView) alertDialog.findViewById(R.id.alert_content);

        if (TextUtils.isEmpty(title)) {
            tv_title.setText("");
        } else {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }

        if (TextUtils.isEmpty(content)) {
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
        }
        isShowing = true;
    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * The layout of button whether or not shown default :Visible
     */
    public MAlertDialog setClickable(boolean able) {
        if (!able)
            btnLayout.setVisibility(View.GONE);
        return this;
    }

    /**
     * The view of cancel whether or not shown default :Visible
     */
    public MAlertDialog setCancelable(boolean able) {
        if (!able)
            btn_cancel.setVisibility(View.GONE);
        return this;
    }

    /**
     * The view of Confirm whether or not shown default :Visible
     */
    public MAlertDialog setConfirmable(boolean able) {
        if (!able)
            btn_confirm.setVisibility(View.GONE);
        return this;
    }

    /**
     * set listener for the confirm button
     *
     * @param cancelClickListener
     */
    public MAlertDialog setCancelClickListener(OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
        btn_cancel.setOnClickListener(this.cancelClickListener);
        return this;
    }



    /**
     * set listener for the cancel button
     *
     * @param confrimClickListener
     */
    public MAlertDialog setConfirmClickListener(OnClickListener confrimClickListener) {
        this.confrimClickListener = confrimClickListener;
        btn_confirm.setOnClickListener(this.confrimClickListener);
        return this;
    }

    public MAlertDialog setOnCancelListener(OnCancelListener cancelListener) {
        alertDialog.setOnCancelListener(cancelListener);
        return this;
    }

    /**
     * dismiss the alert dialog
     */
    public void cancel() {
        alertDialog.cancel();
    }

    public MAlertDialog setView(View view) {
        dialogView.removeAllViews();
        dialogView.addView(view);
        return this;
    }

    public void setView(View view, LayoutParams params) {
        dialogView.addView(view, params);
    }

    public void dismiss() {
        isShowing = false;
        alertDialog.dismiss();

    }

}
