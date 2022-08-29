package com.dev.eventapp.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dev.eventapp.R
import com.dev.eventapp.databinding.ActivitySignInBinding
import com.dev.eventapp.utils.ResultStatus
import com.dev.eventapp.view_models.MemberViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val TAG = "SignInActivityDebug"
    }

    private val memberViewModel : MemberViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account : GoogleSignInAccount = task.result
                    applySignInAction(account)
                } else {
                    showToast("Google Sign In : ${task.exception!!.message.toString()}")
                }
            } catch (e : Exception) {
                showToast("Google Sign In : ${e.message.toString()}")
            }
        }
    }

    private fun applySignInAction(account: GoogleSignInAccount) {
        memberViewModel.signInWithGoogleAccount(account)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        memberViewModel.member.observe(this) { resultState ->
            when (resultState) {
                is ResultStatus.Success -> {
                    val user = resultState.data
                    navigateToMainActivity()
                    showToast("Welcome, ${user?.name}")
                }
                is ResultStatus.Error -> {
                    showToast("Welcome, ${resultState.message}")
                }
                else -> {
                    Log.d(TAG, "Else")
                }
            }
        }

        binding.btnSignInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }

        checkAuthentication()
    }

    private fun checkAuthentication() {
        if (Firebase.auth.currentUser != null) {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun signInWithGoogle() {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webClientId))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

        val googleSignInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(googleSignInIntent)
    }
}