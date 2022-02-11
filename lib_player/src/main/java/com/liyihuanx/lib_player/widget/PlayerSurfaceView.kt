package com.liyihuanx.lib_player.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import com.liyihuanx.lib_player.utils.PlayerUtil
import kotlin.math.ceil
import kotlin.math.round

/**
 * @author liyihuan
 * @date 2022/02/11
 * @Description
 */
class PlayerSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : SurfaceView(context, attrs, defStyleAttr) {


    private var videoHeight: Int = 0
    private var videoWidth: Int = 0
    private var ratio = 0.0f

    fun setSize(videoWidth: Int, videoHeight: Int) {

        this.videoWidth = videoWidth
        this.videoHeight = videoHeight
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 需要设置的宽高
        var measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        var measureHeight = MeasureSpec.getSize(heightMeasureSpec)


        if (videoWidth > 0 && videoHeight > 0) {
            // 情况1：小于屏幕宽度，那么长度就要放大对应的比例
            if (videoWidth < PlayerUtil.getScreenWidth()) {
                ratio = PlayerUtil.getScreenWidth().toFloat() / videoWidth.toFloat()

            }
            // 放大的比例是ceil向上取整好，还是round四舍五入好？
            measureHeight = (videoHeight * round(ratio.toDouble())).toInt()

            // 如果放大了比例，但是高度大于屏幕高度了
            if (measureHeight > PlayerUtil.getScreenHeight()) {
                measureHeight = PlayerUtil.getScreenHeight()
            }


        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY))

    }


}