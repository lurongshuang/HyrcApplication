package com.hyrc.lrs.hyrcbase.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hyrc.lrs.hyrcbase.R;

public class LoadBaseDialog extends Dialog {
    public LoadBaseDialog(@NonNull Context context) {
        super(context, R.style.TransparentDialog);
    }

    private TextView tvTipMess;
    private String titleName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.load_indicator_view);
        tvTipMess = findViewById(R.id.tvTipMess);
        setCanceledOnTouchOutside(false);
        if (titleName.isEmpty()) {
            tvTipMess.setVisibility(View.GONE);
        } else {
            tvTipMess.setVisibility(View.VISIBLE);
            tvTipMess.setText(titleName);
        }
    }

    public LoadBaseDialog setTitle(String text) {
        titleName = text;
        tvTipMess.setText(titleName);
        return this;
    }
}
