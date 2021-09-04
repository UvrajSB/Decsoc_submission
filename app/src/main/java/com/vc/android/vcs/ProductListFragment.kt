package com.vc.android.vcs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ramotion.foldingcell.FoldingCell
import com.vc.android.vcs.DataClass.home_category_item
import com.vc.android.vcs.DataClass.home_product_item
import com.vc.android.vcs.databinding.ActivitySplashBinding
import com.vc.android.vcs.databinding.FragmentProductListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {
    lateinit var binding: FragmentProductListBinding
    var productListArray = ArrayList<home_product_item>()
    var adapter: ProductListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
        initView()
        addData()
        Log.d("productList","$productListArray")
        return binding.root
    }
    private fun initView(){
        adapter = ProductListAdapter(productListArray, requireContext())
        binding.homeGridViewProducts.adapter = adapter
        binding.homeGridViewProducts.setOnItemClickListener { parent, view, position, id ->
            val currentCode: String? = productListArray[position].code
//            val extras = FragmentNavigatorExtras(
//                view.findViewById<Carousel>(R.id.image_home_item) to "imageView"
//            )
//            findNavController().navigate(R.id.action_productListFragment_to_detailProductFragment,null, null, extras)}
            view.findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToDetailProductFragment(currentCode!!))
        }
    }
    private fun addData(){

        GlobalScope.launch(Dispatchers.Main) {
            var desired_category : String = "Products"
            productListArray.clear()
            var category_id = ProductListFragmentArgs.fromBundle(requireArguments()).categoryId
            when(category_id){
                0 -> desired_category = "Earrings"
                1 -> desired_category = "Necklaces and Sets"
                2 -> desired_category = "Bracelets and Bangle"
                3 -> desired_category = "Rings and Nosepins"
                4 -> desired_category = "Anklets"
                5 -> desired_category = "Scrunchies and Others"
                else -> "Products"
            }
            val firestoreRV = Firebase.firestore.collection("$desired_category")
            var NewProduct : home_product_item

            firestoreRV
//                .orderBy("date")
                .get()
                .addOnSuccessListener { products ->
                    val loader = binding.loader
                    loader.visibility = View.VISIBLE
                    for(product in products){
                        var image_collection=product.get("imageArray") as ArrayList<String>?
                        var display_image = image_collection?.get(0)
                        NewProduct = home_product_item( product.getString("name"),product.getString("price"), display_image,product.getString("code"))
                        productListArray.add(NewProduct)
                        adapter!!.notifyDataSetChanged()
                        Log.d("productList","${NewProduct}")
                    }
                    loader.visibility = View.GONE
                }
        }
        return

    }
}


class ProductListAdapter: BaseAdapter {

    var productList = ArrayList<home_product_item>()
    var context: Context? = null

    constructor(productList: ArrayList<home_product_item>, context: Context?) : super() {
        this.context = context
        this.productList = productList
    }

    override fun getCount(): Int {
        return productList.size
        Log.d("count","${productList.size}")
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun data(){

    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        Log.d("PlA","PlA called")
        var product = this.productList[position]
        var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var productView = inflater.inflate(R.layout.home_item_rv,null)

        Glide.with(context!!).load(product.image?.toUri()).into(productView.findViewById<ImageView>(R.id.image_home_item))

        productView.findViewById<TextView>(R.id.price_home_item).text = product.price!!
        productView.findViewById<TextView>(R.id.name_home_item).text = product.name!!
        return productView
    }
}
