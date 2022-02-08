package com.liyihuanx.lib_player.media

import android.media.MediaPlayer

/**
 * @author liyihuan
 * @date 2022/02/08
 * @Description
 */
interface MediaPlayerListener :
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnVideoSizeChangedListener,
    MediaPlayer.OnTimedTextListener {
}