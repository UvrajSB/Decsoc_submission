package com.vc.android.vcs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cleveroad.fanlayoutmanager.FanLayoutManager
import com.cleveroad.fanlayoutmanager.FanLayoutManagerSettings
import com.github.florent37.viewanimator.ViewAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vc.android.vcs.DataClass.home_product_item
import com.vc.android.vcs.databinding.FragmentWishlistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WishlistFragment : Fragment() {
    var productListArray = ArrayList<home_product_item>()
    var adapter= WLAdapter()
    lateinit var binding: FragmentWishlistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addData()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, container, false)
        val rv = binding.wlRv
        val fanLayoutManagerSettings = FanLayoutManagerSettings
            .newBuilder(context)
            .withFanRadius(true)
            .withAngleItemBounce(5f)
            .withViewWidthDp(300f)
            .withViewHeightDp(600f)
            .build()
        rv.layoutManager = FanLayoutManager(requireContext(),fanLayoutManagerSettings)
//        rv.layoutManager = TurnLayoutManager(context,              // provide a context
//            TurnLayoutManager.Gravity.START,        // from which direction should the list items orbit?
//            TurnLayoutManager.Orientation.VERTICAL, // Is this a vertical or horizontal scroll?
//            50,               // The radius of the item rotation
//            0,                 // Extra offset distance
//            true)
//        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        rv.adapter = adapter

        return binding.root
    }

    private fun addData() {
        productListArray.clear()
        Firebase.firestore.collection("Existing users")
            .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
            .collection("wishlist").get().addOnSuccessListener {codes->
                for(code in codes){
                    Log.d("array11","${code.get("code")}")

                    GlobalScope.launch(Dispatchers.Main) {
                        val firestoreProductRef = Firebase.firestore.collection("Products")
                        var NewProduct : home_product_item
                        firestoreProductRef
                            .whereEqualTo("code",code.get("code"))
                            .get()
                            .addOnSuccessListener { products ->
                                for(product in products){
                                    var image_collection=product.get("imageArray") as ArrayList<String>?
                                    var display_image = image_collection?.get(0)
                                    NewProduct = home_product_item( product.getString("name"),product.getString("price"), display_image,product.getString("code"))
                                    productListArray.add(NewProduct)
                                    Log.d("array11","${NewProduct}")
                                    Log.d("array11","${productListArray}")
                                }
                                adapter.updateUsers(productListArray)
                            }
                    }
                }
            }

    }
}



class WLAdapter: RecyclerView.Adapter<WLAdapter.WlViewHolder>() {
    var wlItemsArray : ArrayList<home_product_item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WlViewHolder {
        Log.d("array11","onCreateViewHolder was called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wl,parent,false)
        return WlViewHolder(view)
    }

    override fun onBindViewHolder(holder: WlViewHolder, position: Int) {
        Log.d("array11","onBind was called")
        var CurrentProduct = wlItemsArray[position]
        holder.name.text =CurrentProduct.name
        holder.price.text = CurrentProduct.price
       Glide.with(holder.itemView.context).load(CurrentProduct.image?.toUri()).circleCrop().into(holder.image)

    }

    override fun getItemCount(): Int {
        Log.d("array11","item count is ${wlItemsArray.size}")
        return wlItemsArray.size

    }

    fun updateUsers(Users: ArrayList<home_product_item>) {
        wlItemsArray.clear()
        wlItemsArray.addAll(Users)
        Log.d("array11","update users was called")
        notifyDataSetChanged()

    }
    class WlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name_product_wl_item)
        val image = itemView.findViewById<ImageView>(R.id.item_image_wl)
        val price = itemView.findViewById<TextView>(R.id.price_product_wl_item)

    }
}


