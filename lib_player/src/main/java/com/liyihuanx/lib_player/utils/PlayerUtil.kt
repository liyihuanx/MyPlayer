package com.liyihuanx.lib_player.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import java.lang.StringBuilder
import java.util.*

/**
 * @author liyihuan
 * @date 2022/02/09
 * @Description
 */
object PlayerUtil {

    /**
     * 将毫秒数格式化为"##:##"的时间
     *
     * @param milliseconds 毫秒数
     * @return ##:##
     */
    fun formatTime(milliseconds: Long): String {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00"
        }
        val totalSeconds = milliseconds / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val stringBuilder = StringBuilder()
        val mFormatter = Formatter(stringBuilder, Locale.getDefault())
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }


    /**
     * 得到设备屏幕的宽度
     */
    fun getScreenWidth(): Int {
        return getScreenSize()[0]
    }

    /**
     * 得到设备屏幕的高度
     */
    fun getScreenHeight(): Int {
        return getScreenSize()[1]
    }

    /**
     * 获取屏幕尺寸
     *
     * @return 数组 0：宽度， 1：高度
     */
    private fun getScreenSize(): IntArray {
        val displayMetrics = getDisplayMetrics()
        return intArrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @return mDisplayMetrics
     */
    private fun getDisplayMetrics(): DisplayMetrics {
        return Resources.getSystem().displayMetrics
    }

}