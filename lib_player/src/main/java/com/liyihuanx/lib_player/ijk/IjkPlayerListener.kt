package com.liyihuanx.lib_player.ijk

import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * @author liyihuan
 * @date 2022/02/08
 * @Description
 */
interface IjkPlayerListener :
    IMediaPlayer.OnPreparedListener,
    IMediaPlayer.OnCompletionListener,
    IMediaPlayer.OnBufferingUpdateListener,
    IMediaPlayer.OnSeekCompleteListener,
    IMediaPlayer.OnVideoSizeChangedListener,
    IMediaPlayer.OnErrorListener,
    IMediaPlayer.OnInfoListener,
    IMediaPlayer.OnTimedTextListener {
}