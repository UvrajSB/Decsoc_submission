package com.vc.android.vcs

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.util.DataUtils
import com.vc.android.vcs.databinding.ActivitySplashBinding

@Suppress("Deprecation")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        val animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.alpha)
        val animationLtoR = AnimationUtils.loadAnimation(this,R.anim.left_to_right)
        val animationRtoL = AnimationUtils.loadAnimation(this,R.anim.right_to_left)
        val animationTtoB = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom)
        val animationBtoT = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top)
        val animationZoom = AnimationUtils.loadAnimation(this,R.anim.zoom_out)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding.logoC.startAnimation(animationFadeOut)
        binding.logoV.startAnimation(animationFadeOut)
        binding.leaf.startAnimation(animationTtoB)
        binding.logoSubtext.startAnimation(animationZoom)


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}