package com.liyihuanx.lib_player.ijk

import android.media.AudioManager
import android.util.Log
import android.view.SurfaceView
import com.liyihuanx.lib_player.base.AbsPlayerEngine
import com.liyihuanx.lib_player.status.VideoStatus
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
//            https://www.jianshu.com/p/843c86a9e9ad?from=singlemessage
//            setOption(1, "analyzemaxduration", 100L)
//            setOption(1, "probesize", 10240L)
//            setOption(1, "flush_packets", 1L)
//            setOption(4, "packet-buffering", 0L)
//            setOption(4, "framedrop", 1L)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

            //开启硬解码
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)

            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

            // 所有监听
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setOnPreparedListener(allIjkPlayListener)
            setOnCompletionListener(allIjkPlayListener)
            setOnErrorListener(allIjkPlayListener)
            setOnInfoListener(allIjkPlayListener)
            setOnBufferingUpdateListener(allIjkPlayListener)
            setOnVideoSizeChangedListener(allIjkPlayListener)
            setOnTimedTextListener(allIjkPlayListener)
        }


    }


    override fun dealVideoPath(path: String) {
        ijkMediaPlayer.dataSource = path
        ijkMediaPlayer.prepareAsync()
    }

    override fun setDisplayView(surfaceView: SurfaceView) {
        ijkMediaPlayer.setDisplay(surfaceView.holder)
    }

    override fun getDuration(): Long {
        return ijkMediaPlayer.duration
    }



    /**
     * ijk视频播放的回调
     */
    private val allIjkPlayListener = object : IjkPlayerListener {
        override fun onPrepared(p0: IMediaPlayer?) {
            statusDispatch.onStatusChange(VideoStatus.STATE_PREPARED)
        }

        override fun onCompletion(p0: IMediaPlayer?) {
            statusDispatch.onStatusChange(VideoStatus.STATE_COMPLETED)
        }

        override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
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
            toHandleInfo(what,extra)

            return false
        }

        override fun onTimedText(p0: IMediaPlayer?, p1: IjkTimedText?) {

        }

    }

    private fun toHandleInfo(what: Int, extra: Int) {
        when (what) {
            // 视频开始渲染
            IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                statusDispatch.onStatusChange(VideoStatus.STATE_PLAYING)
            }
            // 缓冲
            IMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                Log.d("QWER", "info - 缓冲: ")
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
        }
    }

}