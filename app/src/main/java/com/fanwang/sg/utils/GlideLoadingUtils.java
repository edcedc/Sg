package com.fanwang.sg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fanwang.sg.R;
import com.lcodecore.tkrefreshlayout.header.progresslayout.CircleImageView;

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Created by edison on 2018/4/19.
 */

public class GlideLoadingUtils {

    public static void load(Context act, Object url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.place_holder);
        Glide.with(act).load(url).apply(options).into(imageView);
    }

    public static void load(Context act, Object url, ImageView imageView, boolean isUser) {
        ld(act, url, imageView, isUser);
    }
    public static void load(Context act, Object url, CircleImageView imageView) {
        ld(act, url, imageView, false);
    }


    private static void ld(Context act, Object url, ImageView imageView, boolean isUser){
        RequestOptions options = new RequestOptions();
        if (isUser){
            options.placeholder(R.mipmap.place_holder_user);
        }else {
            options.placeholder(R.mipmap.place_holder);
        }
        Glide.with(act).load(url).apply(options).into(imageView);
    }

    public static void loadVideoScreenshot(final Context context, String uri, final ImageView imageView, long frameTimeMicros, final ImageView ivImg3) {
        final RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }
            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(context).load(uri).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                ivImg3.setImageDrawable(resource);
                return false;
            }
        }).into(imageView);
    }


   /* *//**
     * 设置监听请求接口
     *//*
    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<Drawable> requstlistener) {
        if (mContext != null) {
            Glide.with(mContext).load(path).apply(requestOptions).listener(requstlistener);
        } else {
            LogUtils.e("Picture loading failed,context is null");
        }
    }*/

}
