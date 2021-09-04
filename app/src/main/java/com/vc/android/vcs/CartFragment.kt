package com.vc.android.vcs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.florent37.viewanimator.ViewAnimator
import com.vc.android.vcs.databinding.FragmentCartBinding



class CartFragment : Fragment() {
    lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)



        return binding.root
    }



    override fun onResume() {
        super.onResume()
        ViewAnimator
            .animate(binding.cartText2)
            .alpha(1f,0f)
            .duration(1L)
            .thenAnimate(binding.bag1)
            .translationY(800f, 0f)
            .alpha(0.5f,1f)
            .andAnimate(binding.bag1)
            .andAnimate(binding.cartText1)
            .bounceOut()
            .alpha(1f,0.2f)
            .accelerate()
            .duration(500)
            .thenAnimate(binding.bag1)
            .bounce()
            .duration(2000)
            .thenAnimate(binding.cartText2)
            .bounceIn()
            .andAnimate(binding.cartText2)
            .alpha(0f,1f)
            .duration(2000)
            .start()
    }
}