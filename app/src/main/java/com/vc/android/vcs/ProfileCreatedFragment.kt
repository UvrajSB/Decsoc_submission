package com.vc.android.vcs

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth

class ProfileCreatedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_profile_created, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val paybtn :Button = view.findViewById(R.id.pay_btn)
        paybtn.setOnClickListener{
            val intent = Intent(requireActivity(), PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if(FirebaseAuth.getInstance().currentUser == null){
            Log.d("PCF","if condn is working")
            // We've won!  Navigate to the gameWonFragment.
            startActivity(Intent(requireActivity(),LoginActivity::class.java))
        }
    }
}