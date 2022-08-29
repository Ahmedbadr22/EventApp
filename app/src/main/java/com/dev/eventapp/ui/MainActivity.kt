package com.dev.eventapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.eventapp.databinding.ActivityMainBinding
import com.dev.eventapp.services.FirebaseServices
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleSignInClient = GoogleSignIn.getClient(this, FirebaseServices.googleSignInOption)

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            runBlocking {
                googleSignInClient.signOut().await()
            }
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}