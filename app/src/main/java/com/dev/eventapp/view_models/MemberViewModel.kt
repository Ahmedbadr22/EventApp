package com.dev.eventapp.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.eventapp.models.Member
import com.dev.eventapp.repository.MemberRepository
import com.dev.eventapp.ui.SignInActivity
import com.dev.eventapp.utils.ResultStatus
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberViewModel : ViewModel() {

    private val memberRepository: MemberRepository by lazy {
        MemberRepository()
    }

    private var _member: MutableLiveData<ResultStatus<Member>> = MutableLiveData<ResultStatus<Member>>()
    val member: LiveData<ResultStatus<Member>>
        get() = _member


    fun signInWithGoogleAccount(account: GoogleSignInAccount) {
        viewModelScope.launch(Dispatchers.Main) {
            _member.postValue(memberRepository.signIn(account).value)
        }
    }
}