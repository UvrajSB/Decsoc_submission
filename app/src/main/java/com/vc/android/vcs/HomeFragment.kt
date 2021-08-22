package com.vc.android.vcs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.vc.android.vcs.DataClass.home_category_item

import com.vc.android.vcs.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    var adapter : Home_category_adapter? = null
    var categoryList = ArrayList<home_category_item>()

















    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
//        binding.earingsCv.setOnClickListener{
//            view?.findNavController()?.navigate(R.id.action_homeFragment_to_productListFragment)
//        }
        categoryList.add(home_category_item("Earrings",R.drawable.earrings))
        categoryList.add(home_category_item("Necklaces and Sets",R.drawable.necklace))
        categoryList.add(home_category_item("Bracelets and Bangles",R.drawable.bandb))
        categoryList.add(home_category_item("Rings and Nose pins",R.drawable.rings_and_nosepins))
        categoryList.add(home_category_item("Anklets",R.drawable.anklets))
        categoryList.add(home_category_item("Scrunchies and others",R.drawable.scrunchies_and_other))


        adapter = Home_category_adapter(categoryList,requireContext())
        binding.homeGridView.adapter =adapter
        binding.homeGridView.setOnItemClickListener { adapterView, view, position, id ->
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_productListFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
            return categoryView
        }
    }
}