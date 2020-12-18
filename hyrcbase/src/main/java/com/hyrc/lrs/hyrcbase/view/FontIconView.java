package com.hyrc.lrs.hyrcbase.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.xuexiang.xui.widget.alpha.XUIAlphaTextView;

public class FontIconView extends XUIAlphaTextView {
    public FontIconView(Context context) {
        super(context);
        init(context);
    }

    public FontIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //设置字体图标
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        this.setTypeface(font);
    }
}
