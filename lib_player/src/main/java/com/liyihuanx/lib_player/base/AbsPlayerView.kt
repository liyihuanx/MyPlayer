package com.liyihuanx.lib_player.base

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.liyihuanx.lib_player.R
import com.liyihuanx.lib_player.engine.EngineFactory
import com.liyihuanx.lib_player.engine.EngineType
import com.liyihuanx.lib_player.status.IVideoStatusListener
import com.liyihuanx.lib_player.status.VideoStatus
import com.liyihuanx.lib_player.widget.CoverImgView
import com.liyihuanx.lib_player.widget.PlayerSurfaceView

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
) : FrameLayout(context, attrs, defStyleAttr), IAbsPlayerView, IVideoStatusListener {

    /**
     * wrap content 高度下的 宽高比
     */
    companion object {
        private const val DEFAULT_HEIGHT_RATIO = (36F / 64)
    }


    // 创建SurfaceView
    private val mSurfaceView: PlayerSurfaceView

    // 封面图片
    private val mCoverImage: CoverImgView
    private var mCoverPath: String = ""

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
//        mSurfaceView = SurfaceView(context).also {
//            it.holder.addCallback(surfaceCallback)
//
//            val layoutParams =
//                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
//            it.layoutParams = layoutParams
//            this.addView(it)
//        }

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
        // 创建承载布局
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_surface_container, null, false)
        this.addView(view, params)

        mSurfaceView = view.findViewById(R.id.playSurfaceView)
        mSurfaceView.holder.addCallback(surfaceCallback)
        mCoverImage = view.findViewById(R.id.ivCover)

        // 两者绑定
        mPlayerEngine.setDisplayView(mSurfaceView)
        mPlayerEngine.addVideoStatusListener(this)

    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mPlayerEngine.addVideoStatusListener(this)

    }


    override fun setPath(path: String, cover: String, isPreload: Boolean, isAutoPlay: Boolean) {
        mCoverPath = cover
        mPlayerEngine.setPath(path, cover, isPreload, isAutoPlay)
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

    override fun getCurrentDuration(): Long {
        return mPlayerEngine.getCurrentDuration()
    }

    override fun getCurrentBufferPercentage(): Int {
        return mPlayerEngine.getCurrentBufferPercentage()
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


    // 设置视频封面
    private fun showCoverImage(path: String) {
        if (path.isEmpty()) {
            return
        }
        mCoverImage.setImageDrawable(null)
        mCoverImage.visibility = View.VISIBLE
        Glide.with(mCoverImage.context)
            .asBitmap()
            .load(Uri.parse(path))
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Log.d("QWER", "coverImg w " + resource.width + "     h " + resource.height)
                    mCoverImage.setImageBitmap(resource)
                }
            })
        mCoverImage.setSize(mPlayerEngine.getVideoWidth(), mPlayerEngine.getVideoHeight())
    }

    private fun hideCoverImage() {
        mCoverImage.visibility = View.GONE
    }

    override fun onStatusChange(status: Int) {
        when (status) {
            VideoStatus.STATE_PREPARED, VideoStatus.STATE_PRELOADED_WAITING -> {
//                showCoverImage(mCoverPath)
                mSurfaceView.setSize(mPlayerEngine.getVideoWidth(), mPlayerEngine.getVideoHeight())
            }

            VideoStatus.STATE_PLAYING -> {
//                hideCoverImage()
            }
        }
    }
}