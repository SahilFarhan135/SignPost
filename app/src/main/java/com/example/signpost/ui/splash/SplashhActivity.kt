package com.example.signpost.ui.splash

import android.os.Bundle
import android.os.Handler
import com.example.signpost.R
import com.example.signpost.base.BaseActivity
import com.example.signpost.databinding.ActivitySplashBinding
import com.example.signpost.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashhActivity : BaseActivity<ActivitySplashBinding>() {

    override fun layoutId(): Int = R.layout.activity_splash
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            navigator.startActivity(MainActivity::class.java)
            finish()
        }, 2000)

    }

    override fun addObservers() = Unit

}





