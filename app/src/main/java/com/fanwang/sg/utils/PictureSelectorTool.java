package com.fanwang.sg.utils;

import android.app.Activity;
import android.content.Context;

import com.fanwang.sg.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by yc on 2018/1/23.
 *  https://github.com/LuckSiege/PictureSelector  不会配置看这里
 */

public class PictureSelectorTool {

    public static void PictureMediaType(Activity act, List<LocalMedia> list, int position){
        if (list.size() <= 0)return;
        LocalMedia media = list.get(position);
        String pictureType = media.getPictureType();
        int mediaType = PictureMimeType.pictureToVideo(pictureType);
        switch (mediaType) {
            case 1:
                // 预览图片 可自定长按保存路径
                //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                PictureSelector.create(act).themeStyle(R.style.picture_default_style).openExternalPreview(position, list);
                break;
            case 2:
                // 预览视频
                PictureSelector.create(act).externalPictureVideo(media.getPath());
                break;
            case 3:
                // 预览音频
                PictureSelector.create(act).externalPictureAudio(media.getPath());
                break;
        }
    }

    public static void PictureSelectorImage(Activity act, List<LocalMedia> selectionMedi, int requst){
        PictureSelectorImage(act, selectionMedi, 9, requst, false, false);
    }
    public static void PictureSelectorImage(Activity act, int requst){
        PictureSelectorImage(act, null, 1, requst, true, false);
    }
    public static void PictureSelectorImage(Activity act, int requst, boolean circleDimmedLayer){
        PictureSelectorImage(act, null, 1, requst, true, circleDimmedLayer);
    }
    public static void PictureSelectorImage(Activity act, int requst, boolean enableCrop, boolean circleDimmedLayer){
        PictureSelectorImage(act, null, 1, requst, enableCrop, circleDimmedLayer);
    }


    private static void PictureSelectorImage(Activity act, List<LocalMedia> selectionMedi, int number, int requst, boolean enableCrop, boolean circleDimmedLayer){
        PictureSelector.create(act)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(number)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .enableCrop(enableCrop)// 是否裁剪 true or false
                .circleDimmedLayer(circleDimmedLayer)// 是否圆形裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片 true or false
                .withAspectRatio(1, 1)
//                .compressSavePath(getPath())//压缩图片保存地址
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(selectionMedi)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(150)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(requst);//结果回调onActivityResult code
    }

    public static void PictureSelectorVideo(Activity act, int request){
        SelectorVideo(act, request);
    }
     public static void PictureSelectorVideo(Activity act){
        SelectorVideo(act, PictureConfig.CHOOSE_REQUEST);
    }

    private static void SelectorVideo(Activity act, int request){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(act)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageSpanCount(4)// 每行显示个数 int
                .isCamera(true)
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .compress(true)// 是否压缩 true or false
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(180)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(5)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .forResult(request);//结果回调onActivityResult code
    }

    /**
     *  直接拍照
     * @param act
     * @param request
     */
    public static void photo(Context act, int request, boolean circleDimmedLayer){
        p(act, request, circleDimmedLayer);
    }
     public static void photo(Context act, int request){
        p(act, request, false);
    }

    private static void p(Context act, int request, boolean circleDimmedLayer){
        PictureSelector.create((Activity) act)
                .openCamera(PictureMimeType.ofImage())
                .enableCrop(true)// 是否裁剪 true or false
                .circleDimmedLayer(circleDimmedLayer)
                .compress(true)// 是否压缩 true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(150)// 小于100kb的图片不压缩
                .forResult(request);
    }

}
