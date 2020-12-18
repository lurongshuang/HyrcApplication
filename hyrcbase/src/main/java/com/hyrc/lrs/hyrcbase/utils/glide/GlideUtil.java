package com.hyrc.lrs.hyrcbase.utils.glide;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.hyrc.lrs.hyrcbase.R;
import com.hyrc.lrs.hyrcbase.utils.DensityUtil;

public class GlideUtil {

    private static GlideUtil instance;

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {
        if (instance == null) {
            instance = new GlideUtil();
        }
        return instance;
    }


    /**
     * 显示随机的图片(每日推荐)
     *
     * @param imgNumber 有几张图片要显示,对应默认图
     * @param imageUrl  显示图片的url
     * @param imageView 对应图片控件
     */
    public static void displayRandom(int imgNumber, String imageUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(getMusicDefaultPic(imgNumber))
                .error(getMusicDefaultPic(imgNumber))
                .transition(DrawableTransitionOptions.withCrossFade(1500))
                .into(imageView);
    }

    private static int getMusicDefaultPic(int imgNumber) {
        return R.drawable.shape_bg_loading;
    }

    /**
     * 将gif图转换为静态图
     */
    public static void displayGif(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .override(DensityUtil.dip2px(imageView.getContext(), 60), DensityUtil.dip2px(imageView.getContext(), 80))
                .placeholder(R.drawable.shape_bg_loading)
                .error(R.drawable.shape_bg_loading)
                .into(imageView);
    }


    private static int getDefaultPic(int type) {
        return R.drawable.shape_bg_loading;
    }


    /**
     * 加载固定宽高图片
     */
    public static void imageUrl(ImageView imageView, String url, int imageWidthDp, int imageHeightDp) {
        Glide.with(imageView.getContext())
                .load(url)
                .override(DensityUtil.dip2px(imageView.getContext(), imageWidthDp), DensityUtil.dip2px(imageView.getContext(), imageHeightDp))
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .placeholder(getMusicDefaultPic(4))
                .centerCrop()
                .error(getDefaultPic(0))
                .into(imageView);
    }
}
