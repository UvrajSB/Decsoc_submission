package com.vc.android.vcs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

@Suppress("Deprecation")
class ProfileFragment : Fragment() {
    companion object {
        private const val RC_Sign = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createLoginUI()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    private fun createLoginUI() {
        val providers = arrayListOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.TwitterBuilder().build()
            )

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setTheme(R.style.LoginTheme).setLogo(R.drawable.logo).setAvailableProviders(providers)
                .build(), RC_Sign
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_Sign) {
            var response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentSpace, ProfileCreatedFragment())
                    //addToBackStack(null)
                    commit()
                }
                Log.d("sign in","succesfull")

            } else {
                if (response == null) {

                }
                if (response?.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    return
                }
                if (response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
//                    Toast.makeText(this, response?.error?.errorCode.toString(), Toast.LENGTH_LONG)
                }
                return
            }
        }
    }
}