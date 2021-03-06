package com.liyihuanx.lib_player

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import android.view.Gravity
import com.liyihuanx.lib_player.ijk.IjkPlayerEngine


/**
 * @author liyihuan
 * @date 2022/02/08
 * @Description
 */
class ExamplePlayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    // 创建SurfaceView
    private val mSurfaceView: SurfaceView

    private val mMediaPlayer: IjkPlayerEngine

    private val surfaceCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(p0: SurfaceHolder) {
            Log.d("QWER", "surfaceCreated: ")

        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            Log.d("QWER", "surfaceChanged: ")

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            Log.d("QWER", "surfaceDestroyed: ")
        }

    }


    // 初始化
    init {
        // 收集自定义属性
        initStyledAttributes(context)

        mSurfaceView = SurfaceView(context).also {
            it.holder.addCallback(surfaceCallback)

            val layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
            it.layoutParams = layoutParams
            this.addView(it)
        }

        mMediaPlayer = IjkPlayerEngine()

    }


    /**
     * 自定义属性
     */
    private fun initStyledAttributes(context: Context) {


    }


}