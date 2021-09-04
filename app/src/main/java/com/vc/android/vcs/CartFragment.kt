package com.vc.android.vcs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vc.android.vcs.DataClass.home_product_item
import com.vc.android.vcs.databinding.FragmentCartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CartFragment : Fragment() {
    lateinit var binding: FragmentCartBinding
    var productListArray = ArrayList<home_product_item>()
    var adapter= cartAdapter()
    var total = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        addData()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        val rv = binding.rvCart
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        rv.adapter = adapter

        return binding.root
    }
    private fun addData() {
        productListArray.clear()
        Firebase.firestore.collection("Existing users")
            .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
            .collection("cart").get().addOnSuccessListener {codes->
                for(code in codes){
                    Log.d("array12","${code.get("code")}")

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
                                    Log.d("array12","${NewProduct}")
                                    Log.d("array12","${productListArray}")
                                    total+= NewProduct.price!!.toInt()
                                }

                                adapter.updateUsers(productListArray)
                                binding.totalAmt.text = total.toString()
                            }
                    }
                }
            }

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
class cartAdapter: RecyclerView.Adapter<cartAdapter.cartViewHolder>() {
    var cartItemsArray : ArrayList<home_product_item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartAdapter.cartViewHolder {
        Log.d("array12","onCreateViewHolder was called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return cartAdapter.cartViewHolder(view)
    }

    override fun onBindViewHolder(holder: cartAdapter.cartViewHolder, position: Int) {
        Log.d("array12","onBind was called")
        var CurrentProduct = cartItemsArray[position]
        holder.name.text =CurrentProduct.name
        holder.price.text = CurrentProduct.price
        Glide.with(holder.itemView.context).load(CurrentProduct.image?.toUri()).circleCrop().into(holder.image)

    }

    override fun getItemCount(): Int {
        Log.d("array12","item count is ${cartItemsArray.size}")
        return cartItemsArray.size

    }

    fun updateUsers(Users: ArrayList<home_product_item>) {
        cartItemsArray.clear()
        cartItemsArray.addAll(Users)
        Log.d("array12","update users was called")
        notifyDataSetChanged()

    }
    class cartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.product_name_item_cart)
        val image = itemView.findViewById<ImageView>(R.id.product_image_item_cart)
        val price = itemView.findViewById<TextView>(R.id.product_name_price_item_cart)

    }
}