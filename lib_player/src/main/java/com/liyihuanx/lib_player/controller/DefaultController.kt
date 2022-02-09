package com.liyihuanx.lib_player.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.liyihuanx.lib_player.R
import com.liyihuanx.lib_player.base.AbsController
import com.liyihuanx.lib_player.status.VideoStatus
import com.liyihuanx.lib_player.utils.PlayerUtil

/**
 * @author liyihuan
 * @date 2022/02/09
 * @Description
 */
class DefaultController(context: Context) : AbsController(context), View.OnClickListener {

    private var tvTotalTime: TextView? = null
    private var btnPause: Button? = null
    private var btnResume: Button? = null



    override fun getCoverLayout(): Int {
        return R.layout.layout_default_controller
    }

    override fun initView(rootView: View) {
        tvTotalTime = rootView.findViewById<TextView>(R.id.tvTotalTime)
        btnPause = rootView.findViewById<Button>(R.id.btnPause)
        btnResume = rootView.findViewById<Button>(R.id.btnResume)


        btnPause?.setOnClickListener(this)
        btnResume?.setOnClickListener(this)

    }

    override fun onStatusChange(status: Int) {
        when (status) {
            VideoStatus.STATE_PREPARED -> {
                tvTotalTime?.text = "总时长：${PlayerUtil.formatTime(mPlayer!!.getDuration())}"
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0) {
            btnPause -> {
                mPlayer!!.onPause()
            }
            btnResume -> {
                mPlayer!!.onResume()
            }
        }
    }
}