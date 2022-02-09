package com.liyihuanx.lib_player.base

import android.view.SurfaceHolder
import android.view.SurfaceView
import com.liyihuanx.lib_player.status.IVideoStatusListener
import com.liyihuanx.lib_player.status.VideoStatus

/**
 * @author liyihuan
 * @date 2022/02/08
 * @Description 基础引擎类
 */
abstract class AbsPlayerEngine : IAbsPlayerEngine {


    protected var mCurrentState = VideoStatus.STATE_IDLE

    /**
     * 状态分发
     */
    private val videoStatusListener = ArrayList<IVideoStatusListener>()

    fun addVideoStatusListener(listener: IVideoStatusListener) {
        if (!videoStatusListener.contains(listener)) {
            videoStatusListener.add(listener)
        }
    }

    fun removeVideoStatusListener(listener: IVideoStatusListener) {
        videoStatusListener.remove(listener)
    }

    /**
     * 为了统一管理分发方法 forEach
     */
    val statusDispatch = object : IVideoStatusListener {
        override fun onStatusChange(status: Int) {
            // 保存一份状态值
            mCurrentState = status
            // 分发状态
            videoStatusListener.forEach {
                it.onStatusChange(status)
            }
        }
    }



    abstract fun dealVideoPath(path: String)
    abstract fun setDisplayView(surfaceView: SurfaceView)

}