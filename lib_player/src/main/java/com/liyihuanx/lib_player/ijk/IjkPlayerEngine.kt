package com.liyihuanx.lib_player.ijk

import android.media.AudioManager
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.liyihuanx.lib_player.base.AbsPlayerEngine
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
    private val mIMediaPlayer: IjkMediaPlayer by lazy {
        IjkMediaPlayer().apply {

//            setOption(1, "analyzemaxduration", 100L)
//            setOption(1, "probesize", 10240L)
//            setOption(1, "flush_packets", 1L)
//            setOption(4, "packet-buffering", 0L)
//            setOption(4, "framedrop", 1L)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
//            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

            //开启硬解码
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)

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

    fun setDisplay(holder: SurfaceHolder) {
        // 设置视图
        mIMediaPlayer.setDisplay(holder)
    }


    override fun dealVideoPath(path: String) {
        mIMediaPlayer.dataSource = path
        mIMediaPlayer.prepareAsync()
    }

    override fun setDisplayView(surfaceView: SurfaceView) {
        mIMediaPlayer.setDisplay(surfaceView.holder)
    }

    fun play(mPath: String) {
        // 重置
//        mIMediaPlayer.stop()
//        mIMediaPlayer.reset()

    }





    /**
     * ijk视频播放的回调
     */
    private val allIjkPlayListener = object : IjkPlayerListener {
        override fun onPrepared(p0: IMediaPlayer?) {
            Log.d("QWER", "onPrepared: ")

        }

        override fun onCompletion(p0: IMediaPlayer?) {
            Log.d("QWER", "onPrepared: ")
        }

        override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
            Log.d("QWER", "onPrepared: ")
        }

        override fun onSeekComplete(p0: IMediaPlayer?) {
            Log.d("QWER", "onPrepared: ")
        }

        override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {
            Log.d("QWER", "onPrepared: ")
        }

        override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
            Log.d("QWER", "onError: $p1 --- $p2")
            return true
        }

        override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
            Log.d("QWER", "onInfo: $p1 --- $p2")
            return true
        }

        override fun onTimedText(p0: IMediaPlayer?, p1: IjkTimedText?) {
            Log.d("QWER", "onPrepared: ")
        }

    }




}