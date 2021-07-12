package com.example.drinkup;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastCustom {

    // per usare il toast personalizzato
    // ToastCustom.makeText(getApplicationContext(),ToastCustom.TYPE," - messaggio - ").show();

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    public static final int TYPE_EMPTY = 0;
    public static final int TYPE_INFO = 1;
    public static final int TYPE_WARN = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_SUCCESS = 4;
    public static final int TYPE_REMOVE = 5;

    private View mLayout;
    private Context mContext;
    private String mMessage;

    int duration = -1;

    private LayoutInflater inflater;

    public static ToastCustom makeText(Context context, int type, String message, int duration) {
        return new ToastCustom(context, type, message, duration);
    }

    public static ToastCustom makeText(Context context, int type, String message) {
        Log.d("toast", "sono in makeText");
        return new ToastCustom(context, type, message, LENGTH_SHORT);
    }

    private ToastCustom(Context context, int type, String message, int duration) {

        this.duration = duration;

        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mMessage = message;

        View convertView = inflater.inflate(R.layout.toast_layout, null);
        mLayout = convertView.findViewById(R.id.customtoast_layout);

        TextView text = mLayout.findViewById(R.id.message);
        text.setText(message);

        ImageView image = mLayout.findViewById(R.id.icon);

        if (type == TYPE_EMPTY) {
            ((ViewManager) image.getParent()).removeView(image);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_empty));
            text.setTextColor(convertView.getResources().getColor(R.color.black));
        }

        if (type == TYPE_INFO) {
            image.setImageResource(R.drawable.toast_ic_info);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.red_dark));
        }

        if (type == TYPE_WARN) {
            image.setImageResource(R.drawable.toast_ic_warn);
            image.setColorFilter(R.color.black);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_warn_div));
            text.setTextColor(convertView.getResources().getColor(R.color.black));
        }

        if (type == TYPE_ERROR) {
            image.setImageResource(R.drawable.toast_ic_error);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_error));
        }

        if (type == TYPE_SUCCESS) {
            image.setImageResource(R.drawable.toast_ic_success);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.Orange));
        }

        if (type == TYPE_REMOVE) {
            image.setImageResource(R.drawable.toast_ic_remove);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.Orange));
        }

    }

    @Deprecated
    public ToastCustom(Context context, int type, String message) {

        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mMessage = message;

        View convertView = inflater.inflate(R.layout.toast_layout, null);
        mLayout = convertView.findViewById(R.id.customtoast_layout);

        TextView text = mLayout.findViewById(R.id.message);
        text.setText(message);

        ImageView image = mLayout.findViewById(R.id.icon);

        if (type == TYPE_EMPTY) {
            ((ViewManager) image.getParent()).removeView(image);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_empty));
            text.setTextColor(convertView.getResources().getColor(R.color.black));
        }

        if (type == TYPE_INFO) {
            image.setImageResource(R.drawable.toast_ic_info);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.red_dark));
        }

        if (type == TYPE_WARN) {
            image.setImageResource(R.drawable.toast_ic_warn);
            image.setColorFilter(R.color.black);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_warn_div));
            text.setTextColor(convertView.getResources().getColor(R.color.black));
        }

        if (type == TYPE_ERROR) {
            image.setImageResource(R.drawable.toast_ic_error);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_error));
        }

        if (type == TYPE_SUCCESS) {
            image.setImageResource(R.drawable.toast_ic_success);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.orange_dark));
        }

        if (type == TYPE_REMOVE) {
            image.setImageResource(R.drawable.toast_ic_remove);
            mLayout.setBackgroundColor(convertView.getResources().getColor(R.color.orange_dark));
        }

    }

    public void setImage(int rDrawable) {
        ImageView image = mLayout.findViewById(R.id.icon);
        image.setImageResource(rDrawable);
    }

    public void setColor(int idRColor) {
        mLayout.setBackgroundColor(idRColor);
    }

    public void show() {
        this.show(this.duration == -1 ? Toast.LENGTH_SHORT : duration);
    }

    public void show(int duration) {
        show(Gravity.CENTER_VERTICAL, duration);
    }

    public void show(int gravity, int duration) {
        try {
            Toast toast = new Toast(mContext);
            toast.setGravity(gravity, 0, 0);
            toast.setDuration(duration);

            if (mLayout != null)
                toast.setView(mLayout);
            else
                toast.setText(mMessage);

            toast.show();
        } catch (Exception e) {
            Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
        }
    }

}
