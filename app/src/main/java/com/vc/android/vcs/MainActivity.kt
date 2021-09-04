package com.vc.android.vcs

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.vc.android.vcs.DataClass.SliderItem
import com.vc.android.vcs.NavFragments.HomeNavFragment
import com.vc.android.vcs.adapter.SliderAdapterExample
import com.vc.android.vcs.adapter.ViewPagerAdapter
import com.vc.android.vcs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Main activity","main activity created")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        setUpTabs()

    }

        private fun setUpTabs() {
            val adapter = ViewPagerAdapter(supportFragmentManager)
            adapter.addFragment(HomeNavFragment(),"")
            adapter.addFragment(WishlistFragment(), "")
            adapter.addFragment(CartFragment(), "")
            adapter.addFragment(ProfileCreatedFragment(), "")

            binding.viewPager.adapter = adapter
            binding.viewPager.offscreenPageLimit = 3
            binding.tabs.setupWithViewPager(binding.viewPager)
            binding.tabs.getTabAt(0)!!.setIcon(R.drawable.home)
            binding.tabs.getTabAt(1)!!.setIcon(R.drawable.heart)
            binding.tabs.getTabAt(2)!!.setIcon(R.drawable.cart)
            binding.tabs.getTabAt(3)!!.setIcon(R.drawable.profile)


        }

}