package com.huanhong.mashineshop.activity

import android.content.Intent
import com.bumptech.glide.Glide
import com.huanhong.mashineshop.AppApplication
import com.huanhong.mashineshop.BaseActivity
import com.huanhong.mashineshop.R
import kotlinx.android.synthetic.main.activity_start_game.*

class StartGameActivity:BaseActivity(){
    override fun getContentViewId(): Int {
        return R.layout.activity_start_game

    }

    override fun onResume() {
        super.onResume()
        AppApplication.mediaPlayer.start()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@StartGameActivity,StartActivity::class.java))
    }

    override fun initView() {
        super.initView()

        btn_play.setOnClickListener {
            startActivity(Intent(this@StartGameActivity,MainActivity::class.java))
        }
    }
}