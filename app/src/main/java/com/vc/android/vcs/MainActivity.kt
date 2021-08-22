package com.vc.android.vcs

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
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
        binding.swipeBtn.setOnStateChangeListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:07453820041"))
            startActivity(intent)
        }

        var ct = findViewById<View>(R.id.collapsing_toolbar_layout) as CollapsingToolbarLayout
        ct.title = "VC"
        ct.setCollapsedTitleTextAppearance(R.style.VCtext)
        ct.setExpandedTitleTextAppearance(R.style.VCtext)


        val homeFragment = HomeNavFragment()
        val profileFragment = ProfileFragment()
        val wishlistFragment = WishlistFragment()
        val cartFragment = CartFragment()
        makeCurrentFragment(homeFragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.wishlist -> makeCurrentFragment(wishlistFragment)
                R.id.cart -> makeCurrentFragment(cartFragment)
                R.id.profile -> makeCurrentFragment(profileFragment)
            }
            true
        }



        var badge1 = binding.bottomNavigation.getOrCreateBadge(R.id.cart)
        var badge2 = binding.bottomNavigation.getOrCreateBadge(R.id.wishlist)
        badge1.isVisible = true
        badge2.isVisible = true
// An icon only badge will be displayed unless a number is set:
        badge1.number = 99
        badge2.number = 10

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragmentSpace,fragment)
            commit()
        }




}