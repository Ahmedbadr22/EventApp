package com.dev.eventapp.services


import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object FirebaseServices {
    private const val webClientId = "818807726330-3ftcgukm0boqv35322v7udusurv2e0fj.apps.googleusercontent.com"

    val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(webClientId)
        .requestEmail()
        .build()


}