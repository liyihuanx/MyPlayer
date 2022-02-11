package com.liyihuanx.lib_player.ijk

import android.media.AudioManager
import android.util.Log
import android.view.SurfaceView
import com.liyihuanx.lib_player.base.AbsPlayerEngine
import com.liyihuanx.lib_player.status.VideoStatus
import com.liyihuanx.lib_player.widget.PlayerSurfaceView
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.IjkTimedText

/**
 * @author liyihuan
 * @date 2022/02/08
 * @Description
 */
class IjkPlayerEngine : AbsPlayerEngine() {


    /**
     * ijk播放器
     */
    private val ijkMediaPlayer: IjkMediaPlayer by lazy {
        IjkMediaPlayer().apply {
//            http://superdanny.link/2017/05/09/iOS-IJKPlayer/
//            https://www.jianshu.com/p/843c86a9e9ad?from=singlemessage
//            setOption(1, "analyzemaxduration", 100L)
//            setOption(1, "probesize", 10240L)
//            setOption(1, "flush_packets", 1L)
//            setOption(4, "packet-buffering", 0L)
//            setOption(4, "framedrop", 1L)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

            // reset之后，参数会失效
//            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "skip_loop_filter", 1)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "http-detect-range-support", 48)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
//            setVolume(1.0f, 1.0f)

            // 设置播放前的最大探测时间
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzeduration",1);
            // 播放前的探测Size，默认是1M, 改小一点会出画面更快
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024 * 5L);

            // 开启硬解码
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)


            // 关闭加载完视频后直接播放，需要手动播放
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)

            setAudioStreamType(AudioManager.STREAM_MUSIC)


            // 所有监听
            setOnPreparedListener(allIjkPlayListener)
            setOnCompletionListener(allIjkPlayListener)
            setOnErrorListener(allIjkPlayListener)
            setOnInfoListener(allIjkPlayListener)
            setOnBufferingUpdateListener(allIjkPlayListener)
            setOnVideoSizeChangedListener(allIjkPlayListener)
            setOnTimedTextListener(allIjkPlayListener)
        }

    }

    private var tempSurfaceView: PlayerSurfaceView? = null

    private var isUsePreLoad: Boolean = false
    private var isUseAutoPlay: Boolean = false

    private var mPath: String = ""


    override fun setDisplayView(surfaceView: PlayerSurfaceView) {
        // 设置承载视频播放的布局
//        ijkMediaPlayer.setDisplay(surfaceView.holder)
        // 保存一份复用
        tempSurfaceView = surfaceView
    }

    override var playMethod: (() -> Unit) = {
        ijkMediaPlayer.setDisplay(tempSurfaceView!!.holder)
        ijkMediaPlayer.start()
        statusDispatch.onStatusChange(VideoStatus.STATE_PLAYING)
    }

    override fun setPath(path: String,cover: String, isPreload: Boolean, isAutoPlay: Boolean) {
        mPath = path
        // 加载完是否播放
        isUseAutoPlay = isAutoPlay
        // 预加载
        isUsePreLoad = isPreload
        if (isPreload) {
            // 打开视频配置，会走到onPrepared回调，再去播放
            openMedia()
        }
    }

    override fun openMedia() {
        if (isUsePreLoad) {
            statusDispatch.onStatusChange(VideoStatus.STATE_PRELOADING)
        } else {
            // 这里是不预加载的，如果连自动播放都不要，就会出现点一次，开始加载，但是还不能播放，需要再点一次才能播放
            isUseAutoPlay = true
            statusDispatch.onStatusChange(VideoStatus.STATE_PREPARING)
        }
        ijkMediaPlayer.stop()
        ijkMediaPlayer.dataSource = mPath
        ijkMediaPlayer.setDisplay(tempSurfaceView!!.holder)
        ijkMediaPlayer.prepareAsync()

    }


    override fun startPlay() {
        // 1. 预加载好了
        if (mCurrentState == VideoStatus.STATE_PRELOADED_WAITING || mCurrentState == VideoStatus.STATE_PREPARED) {
            Log.d("QWER", "startPlay: 预加载-可以播放了")
            playMethod.invoke()
            return
        }
        // 2.还在加载中
        if (mCurrentState == VideoStatus.STATE_PRELOADING) {
            Log.d("QWER", "startPlay: 预加载-还在准备")
            return
        }
        // 3.普通播放
        playMethod.invoke()
    }

    override fun getDuration(): Long {
        return ijkMediaPlayer.duration
    }

    override fun getCurrentDuration(): Long {
        return ijkMediaPlayer.currentPosition
    }

    override fun getVideoHeight(): Int {
        return ijkMediaPlayer.videoHeight
    }

    override fun getVideoWidth(): Int {
        return ijkMediaPlayer.videoWidth
    }

    /**
     * ijk视频播放的回调
     * 1.准备好，不一定就是会播放
     */
    private val allIjkPlayListener = object : IjkPlayerListener {
        override fun onPrepared(p0: IMediaPlayer?) {
            if (mCurrentState == VideoStatus.STATE_PAUSED || mCurrentState == VideoStatus.STATE_STOP) {
                ijkMediaPlayer.stop()
                return
            }

            if (isUsePreLoad) {
                // 预加载完了，等待播放
                statusDispatch.onStatusChange(VideoStatus.STATE_PRELOADED_WAITING)
            } else {
                // 正常的准备完成，等待播放
                statusDispatch.onStatusChange(VideoStatus.STATE_PREPARED)
            }

            // 自动播放
            if (isUseAutoPlay) {
                playMethod.invoke()
            }

        }

        override fun onCompletion(p0: IMediaPlayer?) {
            statusDispatch.onStatusChange(VideoStatus.STATE_COMPLETED)
        }

        override fun onBufferingUpdate(p0: IMediaPlayer?, bufferNum: Int) {
            // 缓冲的值到不了100，有时候98，有时99
            mCurrentBufferPercentage = if (bufferNum > 95) {
                100
            } else {
                bufferNum
            }
        }

        override fun onSeekComplete(p0: IMediaPlayer?) {
        }

        override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {
        }

        override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
            statusDispatch.onStatusChange(VideoStatus.STATE_ERROR)

            Log.d("QWER", "onError: $p1 --- $p2")
            return false
        }

        override fun onInfo(p0: IMediaPlayer?, what: Int, extra: Int): Boolean {
            toHandleInfo(what, extra)

            return false
        }

        override fun onTimedText(p0: IMediaPlayer?, p1: IjkTimedText?) {

        }

    }

    private fun toHandleInfo(what: Int, extra: Int) {
        when (what) {
            // 视频开始渲染
            IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                Log.d("QWER", "info - 视频渲染: ")
            }
            // 播放的缓冲
            IMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                // 缓冲中暂停了
                if (mCurrentState == VideoStatus.STATE_PAUSED || mCurrentState == VideoStatus.STATE_BUFFERING_PAUSED) {
                    statusDispatch.onStatusChange(VideoStatus.STATE_BUFFERING_PAUSED)
                    Log.d("QWER", "info - 暂停缓冲: ")
                } else {
                    statusDispatch.onStatusChange(VideoStatus.STATE_BUFFERING_PLAYING)
                    Log.d("QWER", "info - 边播放边缓冲: ")
                }
            }
            // 缓冲完成继续播放
            IMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                // 填充缓冲区后，IMediaPlayer恢复播放/暂停
                if (mCurrentState == VideoStatus.STATE_BUFFERING_PLAYING) {
                    statusDispatch.onStatusChange(VideoStatus.STATE_PLAYING)
                    Log.d("QWER", "info - 边播放边缓冲结束，播放中: ")

                }
                if (mCurrentState == VideoStatus.STATE_BUFFERING_PAUSED) {
                    statusDispatch.onStatusChange(VideoStatus.STATE_PAUSED)
                    Log.d("QWER", "info - 暂停缓冲冲结束，暂停了，要手动继续播放的: ")
                }
            }
        }
    }

    override fun onPause() {
        if (mCurrentState == VideoStatus.STATE_PLAYING) {
            ijkMediaPlayer.pause()
            statusDispatch.onStatusChange(VideoStatus.STATE_PAUSED)
        }
    }

    override fun onResume() {
        if (mCurrentState == VideoStatus.STATE_PAUSED || mCurrentState == VideoStatus.STATE_STOP) {
            ijkMediaPlayer.start()
            statusDispatch.onStatusChange(VideoStatus.STATE_PLAYING)
        } else if (mCurrentState == VideoStatus.STATE_BUFFERING_PAUSED) {
            ijkMediaPlayer.start()
            statusDispatch.onStatusChange(VideoStatus.STATE_BUFFERING_PLAYING)
        } else if (mCurrentState == VideoStatus.STATE_COMPLETED || mCurrentState == VideoStatus.STATE_ERROR) {
            // reset后，setOption的参数也会重置，会导致一些问题，咋办呢
            ijkMediaPlayer.reset()
            openMedia()
        }
    }

}