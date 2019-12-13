package com.click.progress_library.view;


import android.content.res.Resources;

/**
 * @author：jhonjson
 * @date：2019/12/13 10:16 AM
 * @describe：工具类
 */
public class DpDonversionPxUtils {

    /**
     * px 转 dp
     */
    public static int px2dip(int pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * dp 转 px
     */
    public static float dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }
}
