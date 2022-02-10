package com.liyihuanx.lib_player.media

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.TimedText
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.liyihuanx.lib_player.base.AbsPlayerEngine

/**
 * @author liyihuan
 * @date 2022/02/08
 * @Description
 */
class MediaPlayerEngine : AbsPlayerEngine() {


    private val mMediaPlayer: MediaPlayer by lazy {
        MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setOnPreparedListener(allMediaPlayerListener)
            setOnCompletionListener(allMediaPlayerListener)
            setOnErrorListener(allMediaPlayerListener)
            setOnInfoListener(allMediaPlayerListener)
            setOnBufferingUpdateListener(allMediaPlayerListener)

        }
    }



    override fun setDisplayView(surfaceView: SurfaceView) {
        // 设置视图
        mMediaPlayer.setDisplay(surfaceView.holder)
    }

    override var playMethod: () -> Unit = {

    }

    override fun setPath(path: String, isPreload: Boolean, isAutoPlay: Boolean) {

    }


    override fun openMedia() {

    }

    override fun startPlay() {

    }

    override fun getDuration(): Long {
        return mMediaPlayer.duration.toLong()
    }

    override fun onPause() {

    }

    override fun onResume() {

    }


    private val allMediaPlayerListener = object : MediaPlayerListener {
        override fun onPrepared(p0: MediaPlayer?) {
            mMediaPlayer.start()
            Log.d("QWER", "native - onPrepared: ")
        }

        override fun onCompletion(p0: MediaPlayer?) {
            Log.d("QWER", "native - onCompletion: ")
        }

        override fun onBufferingUpdate(p0: MediaPlayer?, p1: Int) {
            Log.d("QWER", "native - onBufferingUpdate: $p1")
        }

        override fun onSeekComplete(p0: MediaPlayer?) {
            Log.d("QWER", "native - onSeekComplete: ")
        }

        override fun onInfo(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
            Log.d("QWER", "native - onInfo: ")
            return true
        }

        override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
            Log.d("QWER", "native - onError: $p1 --$p2")
            return true
        }

        override fun onVideoSizeChanged(p0: MediaPlayer?, p1: Int, p2: Int) {
            Log.d("QWER", "native - onVideoSizeChanged: ")
        }

        override fun onTimedText(p0: MediaPlayer?, p1: TimedText?) {
            Log.d("QWER", "native - onTimedText: ")
        }

    }




}