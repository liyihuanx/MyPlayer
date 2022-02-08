package com.liyihuanx.lib_player.engine

import com.liyihuanx.lib_player.base.AbsPlayerEngine
import com.liyihuanx.lib_player.ijk.IjkPlayerEngine
import com.liyihuanx.lib_player.media.MediaPlayerEngine

/**
 * @ClassName: EngineFactory
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2022/2/8 21:08
 */
object EngineFactory {

    fun createEngine(type: EngineType):AbsPlayerEngine{
        return when(type){
            EngineType.IJK_PLAYER -> IjkPlayerEngine()
            EngineType.MEDIA_PLAYER -> MediaPlayerEngine()
        }
    }
}