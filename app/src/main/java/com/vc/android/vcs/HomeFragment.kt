package com.vc.android.vcs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.github.florent37.viewanimator.ViewAnimator
import com.github.florent37.viewanimator.ViewAnimator.animate
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController.ClickListener
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.vc.android.vcs.DataClass.SliderItem
import com.vc.android.vcs.DataClass.home_category_item
import com.vc.android.vcs.DataClass.home_product_item
import com.vc.android.vcs.adapter.SliderAdapterExample
import com.vc.android.vcs.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    var adapter1: Home_category_adapter? = null
    lateinit var adapter2: SliderAdapterExample
    var categoryList = ArrayList<home_category_item>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        binding.collapsingToolbarLayout.title = "Vivid Collection"
        binding.collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.VCtext1)
        binding.collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.VCtext2)
        binding.collapsingToolbarLayout.setExpandedTitleMargin(90,0,0,20)



        categoryList.add(home_category_item("Earrings", R.drawable.earrings))
        categoryList.add(home_category_item("Necklaces and Sets", R.drawable.necklace1))
        categoryList.add(home_category_item("Bracelets and Bangles", R.drawable.bandb))
        categoryList.add(home_category_item("Rings and Nose pins", R.drawable.rings_and_nosepins))
        categoryList.add(home_category_item("Anklets", R.drawable.anklets))
        categoryList.add(home_category_item("Scrunchies and others", R.drawable.scrunchies_and_other))

        adapter1 = Home_category_adapter(categoryList, requireContext())
        binding.homeGridViewCategories.adapter = adapter1
        binding.homeGridViewCategories.setOnItemClickListener { adapterView, view, position, id ->
            Log.d("categoryId","${id.toInt()}")
            view.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductListFragment(id.toInt()))
        }

        val sliderView = binding.imageSlider


        adapter2 = SliderAdapterExample(requireContext())
        sliderView.setSliderAdapter(adapter2)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SWAP) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView.setSliderTransformAnimation(SliderAnimations.SPINNERTRANSFORMATION)
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        sliderView.setIndicatorSelectedColor(Color.WHITE)
        sliderView.setIndicatorUnselectedColor(Color.GRAY)
        sliderView.setScrollTimeInSec(5)
        sliderView.setAutoCycle(true)
        sliderView.startAutoCycle()

        val sliderItem1 = SliderItem()
        sliderItem1.description = ""
        sliderItem1.imageUrl = "https://www.hazoorilaljewellers.com/wp-content/uploads/2020/03/2020-03-04-1.jpg"

        val sliderItem2 = SliderItem()
        sliderItem2.description = ""
        sliderItem2.imageUrl = "https://www.zerokaata.com/zerokaata-studio/wp-content/uploads/2018/06/Untitled-design-7-1-1.jpg"

        val sliderItem3 = SliderItem()
        sliderItem3.description = ""
        sliderItem3.imageUrl = "https://assets.ajio.com/medias/sys_master/root/20201021/nTKn/5f8f3a42aeb269d563e74c55/e2o_gold-toned_&_green_textured_danglers_with_push-back_closure.jpg"

        val sliderItem4 = SliderItem()
        sliderItem4.description = ""
        sliderItem4.imageUrl = "https://www.swabhimannjwellery.in/media/catalog/product/cache/1acf12f51ed577332bfed0512c10c899/w/p/wpilkda15_1_1.jpg"


        adapter2.addItem(sliderItem2)
        adapter2.addItem(sliderItem1)
        adapter2.addItem(sliderItem3)
        adapter2.addItem(sliderItem4)
        sliderView.setOnIndicatorClickListener(DrawController.ClickListener {
            Log.i(
                "GGG",
                "onIndicatorClicked: " + sliderView.getCurrentPagePosition()
            )
        })

        return binding.root
    }



}










    class Home_category_adapter: BaseAdapter {
        var categoryList = ArrayList<home_category_item>()
        var context: Context? = null

        constructor(categoryList: ArrayList<home_category_item>, context: Context?) : super() {
            this.context = context
            this.categoryList = categoryList
        }


        override fun getCount(): Int {
            return 6
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var category = this.categoryList[position]
            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var categoryView = inflater.inflate(R.layout.home_categories_item,null)
            categoryView.findViewById<ImageView>(R.id.category_image).setImageResource(category.image!!)
            categoryView.findViewById<TextView>(R.id.category_name).text = category.name!!
            ViewAnimator
                .animate(categoryView.findViewById<ImageView>(R.id.category_image))
                .translationY((-100.0).toFloat(), 0F)
                .alpha(0F,1F)
                .andAnimate(categoryView.findViewById<TextView>(R.id.category_name))
                .dp().translationX((-100.0).toFloat(), 0F)
                .decelerate()
                .duration(1000)
                .thenAnimate(categoryView.findViewById<ImageView>(R.id.category_image))
                .scale(1f, 0.5f, 1f)
                .accelerate()
                .duration(1000)
                .start()



            return categoryView
        }
    }


