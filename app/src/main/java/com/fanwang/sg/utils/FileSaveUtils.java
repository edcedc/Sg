package com.fanwang.sg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

/**
 * 作者：yc on 2018/10/22.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class FileSaveUtils {

    /**
     *  保存本地指定路径加刷新图库
     * @param act
     * @param imgBitmap
     */
    public static void save(Context act, Bitmap imgBitmap){
        boolean orExistsDir = FileUtils.createOrExistsDir(Constants.mainPath);
        if (orExistsDir){
            String imgPath = Constants.imgUrl;
            String fileName = imgPath + System.currentTimeMillis() + ".png";
            boolean save = com.blankj.utilcode.util.ImageUtils.save(imgBitmap, fileName, Bitmap.CompressFormat.PNG, true);
            if (save){
                MediaScannerConnection.scanFile(act,
                        new String[]{fileName},
                        new String[]{"image/png"},
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                LogUtils.e("onScanCompleted"+path);
                            }
                        });
            }
        }
    }

}
