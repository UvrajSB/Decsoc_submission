package com.vc.android.vcs

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private var existingUsers: ArrayList<String> = arrayListOf()
    companion object {
        private const val RC_Sign = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if(FirebaseAuth.getInstance().currentUser != null){
            Log.d("ifcondn","working")
            startActivity(Intent(this,MainActivity::class.java).putExtra("key","1"))
        }
        createLoginUI()
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
                addNewUser()
                Log.d("GoingToUCF","$user.toString().subSequence(3,13)")

                startActivity(Intent(this,MainActivity::class.java).putExtra("key","1"))

            } else {
                if (response == null) {

                }
                if (response?.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    return
                }
                if (response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, response?.error?.errorCode.toString(), Toast.LENGTH_LONG)
                }
                return
            }
        }
    }
    private fun addNewUser(){
        var user_id = FirebaseAuth.getInstance().currentUser?.uid.toString()
        Log.d("MyId","$user_id")
        val user = hashMapOf(
            "user_id" to user_id
        )
        db.collection("Existing users")
            .get()
            .addOnSuccessListener { user_ids ->
                for (user_id in user_ids)
                    existingUsers.add(user_id.getString("user_id").toString())
                if (!existingUsers.contains(user_id)) {
                    db.collection("Existing users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.i("AUTH", documentReference.id)
                            Log.i("AAUUTTHH", "here2")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AUTH", "Error", e)
                            Log.i("AAUUTTHH", "here2 fail")
                        }
                }
            }
    }
}