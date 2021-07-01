package com.example.drinkup.models;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkup.R;

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

    private View layout;
    private Context context;
    private String message;

    int duration = -1;

    private LayoutInflater inflater;

    public static ToastCustom makeText(Context context, int type, String message, int duration) {
        return new ToastCustom(context, type, message, duration);
    }

    public static ToastCustom makeText(Context context, int type, String message) {
        return new ToastCustom(context, type, message, LENGTH_SHORT);
    }

    private ToastCustom(Context context, int type, String message, int duration) {

        this.duration = duration;

        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.message = message;

        View convertView = inflater.inflate(R.layout.toast_layout, null);
        layout = convertView.findViewById(R.id.customtoast_layout);

        TextView text = layout.findViewById(R.id.message);
        text.setText(message);

        ImageView image = layout.findViewById(R.id.icon);

        if (type == TYPE_EMPTY) {
            ((ViewManager) image.getParent()).removeView(image);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_empty));
            text.setTextColor(convertView.getResources().getColor(R.color.black));
        }

        if (type == TYPE_INFO) {
            image.setImageResource(R.drawable.toast_ic_info);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_info));
        }

        if (type == TYPE_WARN) {
            image.setImageResource(R.drawable.toast_ic_warn);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_warn));
        }

        if (type == TYPE_ERROR) {
            image.setImageResource(R.drawable.toast_ic_error);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_error));
        }

        if (type == TYPE_SUCCESS) {
            image.setImageResource(R.drawable.toast_ic_success);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_confirm));
        }

    }

    @Deprecated
    public ToastCustom(Context context, int type, String message) {

        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.message = message;

        View convertView = inflater.inflate(R.layout.toast_layout, null);
        layout = convertView.findViewById(R.id.customtoast_layout);

        TextView text = layout.findViewById(R.id.message);
        text.setText(message);

        ImageView image = layout.findViewById(R.id.icon);

        if (type == TYPE_EMPTY) {
            ((ViewManager) image.getParent()).removeView(image);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_empty));
            text.setTextColor(convertView.getResources().getColor(R.color.black));
        }

        if (type == TYPE_INFO) {
            image.setImageResource(R.drawable.toast_ic_info);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_info));
        }

        if (type == TYPE_WARN) {
            image.setImageResource(R.drawable.toast_ic_warn);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_warn));
        }

        if (type == TYPE_ERROR) {
            image.setImageResource(R.drawable.toast_ic_error);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_error));
        }

        if (type == TYPE_SUCCESS) {
            image.setImageResource(R.drawable.toast_ic_success);
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.toast_confirm));
        }

    }

    public void setImage(int rDrawable) {
        ImageView image = layout.findViewById(R.id.icon);
        image.setImageResource(rDrawable);
    }

    public void setColor(int idRColor) {
        layout.setBackgroundColor(idRColor);
    }

    public void show() {
        this.show(this.duration == -1 ? Toast.LENGTH_SHORT : duration);
    }

    public void show(int duration) {
        show(Gravity.CENTER_VERTICAL, duration);
    }

    public void show(int gravity, int duration) {
        try {
            Toast toast = new Toast(context);
            toast.setGravity(gravity, 0, 0);
            toast.setDuration(duration);

            if (layout != null)
                toast.setView(layout);
            else
                toast.setText(message);

            toast.show();
        } catch (Exception e) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}
