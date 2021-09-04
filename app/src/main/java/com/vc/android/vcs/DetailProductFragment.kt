package com.vc.android.vcs

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.varunest.sparkbutton.SparkEventListener
import com.vc.android.vcs.DataClass.CartDC
import com.vc.android.vcs.DataClass.DetailProductInfo
import com.vc.android.vcs.DataClass.SliderItem
import com.vc.android.vcs.adapter.SliderAdapterExample
import com.vc.android.vcs.databinding.FragmentDetailProductBinding


class DetailProductFragment : Fragment() {
    lateinit var NewProduct: DetailProductInfo
    private var existingCodes: ArrayList<String> = arrayListOf()
    lateinit var adapter: SliderAdapterExample
    lateinit var binding: FragmentDetailProductBinding
    lateinit var productCode : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_product, container, false)
        binding.loader.visibility = View.VISIBLE
        binding.foldingCell.setOnClickListener {
            binding.foldingCell.toggle(false)
        }
        productCode = DetailProductFragmentArgs.fromBundle(requireArguments()).code


        val sliderView = binding.imageHomeItemCr
        adapter = SliderAdapterExample(requireContext())
        sliderView.setSliderAdapter(adapter)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SWAP) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView.setSliderTransformAnimation(SliderAnimations.GATETRANSFORMATION)
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        sliderView.setIndicatorSelectedColor(Color.WHITE)
        sliderView.setIndicatorUnselectedColor(Color.GRAY)
        sliderView.setScrollTimeInSec(5)
        sliderView.setAutoCycle(true)
        sliderView.startAutoCycle()


        val firestoreProductRef = Firebase.firestore.collection("Products")


        firestoreProductRef
            .whereEqualTo("code", productCode)
            .get().addOnSuccessListener { products ->
                NewProduct = DetailProductInfo("", "", "", "", "", "", arrayListOf())
                for (product in products) {
                    NewProduct = DetailProductInfo(
                        product.getString("category"),
                        product.getString("code"),
                        product.getString("name"),
                        product.getString("price"),
                        product.getString("desc"),
                        product.getString("stock"),
                        product.get("imageArray") as ArrayList<String>?
                    )
                }
                binding.dpName.text = NewProduct.Name
                binding.dpName1.text = NewProduct.Name
                binding.dpPrice.text = NewProduct.Price
                binding.dpPrice1.text = NewProduct.Price
                binding.dpDesc.text = NewProduct.Desc

                for (i in 0 until NewProduct.imageArray?.size!!) {
                    var NewSliderItem = SliderItem()
                    NewSliderItem.description = ""
                    NewSliderItem.imageUrl = NewProduct.imageArray!![i]
                    adapter.addItem(NewSliderItem)

                }
            }
        binding.dpAddToCart.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                Firebase.firestore.collection("Existing users")
                    .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}").collection("cart")
                    .get()
                    .addOnSuccessListener {codes->
                        for (code in codes)
                            existingCodes.add(code.getString("number").toString())
                        if (!existingCodes.contains(productCode)) {
                            val pcMap = mapOf("code" to productCode)
                            Firebase.firestore.collection("Existing users")
                                .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}").collection("cart")
                                .add(pcMap)
                                .addOnSuccessListener { documentReference ->
                                    //   Log.i("AUTH", documentReference.id)
                                    //  Log.i("AAUUTTHH", "here2")
                                }
                                .addOnFailureListener { e ->
                                    ///   Log.w("AUTH", "Error", e)
                                    //Log.i("AAUUTTHH", "here2 fail")
                                }
                        }

                    }
                firestoreProductRef.add(CartDC("${NewProduct.Code}"))
            } else {
                showDialog()
            }

        }
        binding.dpAddToCart1.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val pcMap = mapOf("code" to productCode)
                Firebase.firestore.collection("Existing users")
                    .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}").collection("cart")
                    .get()
                    .addOnSuccessListener {codes->
                        for (code in codes)
                            existingCodes.add(code.getString("number").toString())
                        if (!existingCodes.contains(productCode)) {
                            Firebase.firestore.collection("Existing users")
                                .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}").collection("cart")
                                .add(pcMap)
                                .addOnSuccessListener { documentReference ->
                                    //   Log.i("AUTH", documentReference.id)
                                    //  Log.i("AAUUTTHH", "here2")
                                }
                                .addOnFailureListener { e ->
                                    ///   Log.w("AUTH", "Error", e)
                                    //Log.i("AAUUTTHH", "here2 fail")
                                }
                        }

                    }
                firestoreProductRef.add(CartDC("${NewProduct.Code}"))
            } else {
                showDialog()
            }
        }
        binding.sparkButton.setEventListener(object : SparkEventListener {
            override fun onEvent(button: ImageView, buttonState: Boolean) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    if (buttonState) {
                        // Button is active
                        Firebase.firestore.collection("Existing users")
                            .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}").collection("wishlist")
                            .get()
                            .addOnSuccessListener { codes ->
                                for (code in codes)
                                    existingCodes.add(code.getString("number").toString())
                                if (!existingCodes.contains(productCode)) {
                                    val pcMap = hashMapOf("code" to productCode)
                                    Firebase.firestore.collection("Existing users")
                                        .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
                                        .collection("wishlist")
                                        .document("${productCode}").set(pcMap)
                                        .addOnSuccessListener { documentReference ->
                                            //   Log.i("AUTH", documentReference.id)
                                            //  Log.i("AAUUTTHH", "here2")
                                        }
                                        .addOnFailureListener { e ->
                                            ///   Log.w("AUTH", "Error", e)
                                            //Log.i("AAUUTTHH", "here2 fail")
                                        }
                                }
                            }
                    } else {
                        // Button is inactive
                        Firebase.firestore.collection("Existing users")
                            .document("${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
                            .collection("wishlist")
                            .document("${productCode}").delete().addOnSuccessListener {

                            }
                    }
                }
                else {
                    showDialog()
                }
            }

            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {

            }

            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {

            }


        })



        binding.loader.visibility = View.GONE
        return binding.root
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.sign_in_req_dialog)
        val sign_in_now_Btn = dialog.findViewById(R.id.sig_in_now_btn) as Button
        sign_in_now_Btn.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            dialog.dismiss()
        }
    }

}


