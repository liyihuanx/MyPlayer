package com.liyihuanx.lib_player.base

import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @author liyihuan
 * @date 2022/02/08
 * @Description 基础引擎类
 */
abstract class AbsPlayerEngine : IAbsPlayerEngine {
    abstract fun dealVideoPath(path: String)
    abstract fun setDisplayView(surfaceView: SurfaceView)
}