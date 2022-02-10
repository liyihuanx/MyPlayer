package com.liyihuanx.lib_player.base

import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @ClassName: IAbsPlayerEngine
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2022/2/8 21:11
 */
interface IAbsPlayerEngine {

    fun setPath(path: String, isPreload: Boolean = true, isAutoPlay: Boolean = false)

    fun openMedia()


    fun startPlay()

    fun getDuration(): Long

    fun onPause()

    fun onResume()

    fun getCurrentStatus(): Int

}