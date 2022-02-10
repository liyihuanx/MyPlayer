package com.liyihuanx.lib_player.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import com.liyihuanx.lib_player.R
import com.liyihuanx.lib_player.engine.EngineFactory
import com.liyihuanx.lib_player.engine.EngineType

/**
 * @ClassName: AbsPlayerView
 * @Description: 基础播放器view
 * @Author: liyihuan
 * @Date: 2022/2/8 20:55
 */
open class AbsPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), IAbsPlayerView {

    // 创建SurfaceView
    private val mSurfaceView: SurfaceView

    private val surfaceCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(p0: SurfaceHolder) {

        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            controller?.let {
                mPlayerEngine.removeVideoStatusListener(it)
                it.detach()
            }
        }
    }

    /**
     * 播放引擎与类型
     */
    var mPlayerEngine: AbsPlayerEngine

    private var engineType = EngineType.IJK_PLAYER

    var controller: AbsController? = null
        set(value) {
            field = value

            if (value != null) {
                mPlayerEngine.addVideoStatusListener(value)
                value.attach(this)
                // 添加视频播放器的布局
                this.addView(value.getView())
            }


        }

    init {
        // 添加视频播放布局
        mSurfaceView = SurfaceView(context).also {
            it.holder.addCallback(surfaceCallback)

            val layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
            it.layoutParams = layoutParams
            this.addView(it)
        }

        // 可做判断决定添不添加controller
//        this.addView(DefaultController(context).getView())

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.customVideo)
            typedArray.apply {
                val type = getInt(R.styleable.customVideo_engine, EngineType.IJK_PLAYER.type)
                when (type) {
                    EngineType.MEDIA_PLAYER.type -> {
                        engineType = EngineType.MEDIA_PLAYER
                    }

                    EngineType.IJK_PLAYER.type -> {
                        engineType = EngineType.IJK_PLAYER
                    }
                }

//                defaultHeightRatio = getFloat(R.styleable.happyVideo_heightRatio, 0f)
//                isFromLastPosition =
//                    getBoolean(R.styleable.happyVideo_isFromLastPosition, false)
//                loop = getBoolean(R.styleable.happyVideo_loop, false)
//                cache = getBoolean(R.styleable.happyVideo_isUseCache, false)
//                autoChangeOrientation =
//                    getBoolean(R.styleable.happyVideo_autoChangeOrientation, false)
//                isFirstFrameAsCover = getBoolean(R.styleable.happyVideo_isFirstFrameAsCover, false)
//                centerCropError = getFloat(R.styleable.happyVideo_centerCropError, 0f)
            }

            typedArray.recycle()
        }

        // 创建引擎
        mPlayerEngine = EngineFactory.createEngine(engineType)

        mPlayerEngine.setDisplayView(mSurfaceView)
    }



    override fun setPath(path: String, isPreload: Boolean, isAutoPlay: Boolean) {
        mPlayerEngine.setPath(path, isPreload, isAutoPlay)
    }

    override fun openMedia() {
        mPlayerEngine.openMedia()
    }



    override fun startPlay() {
        mPlayerEngine.startPlay()
    }

    // 要写一个方法给外界，然后再去调用引擎的方法
    override fun getDuration(): Long {
        return mPlayerEngine.getDuration()
    }

    override fun onPause() {
        mPlayerEngine.onPause()
    }

    override fun onResume() {
        mPlayerEngine.onResume()
    }

    override fun getCurrentStatus(): Int {
        return mPlayerEngine.getCurrentStatus()
    }
}