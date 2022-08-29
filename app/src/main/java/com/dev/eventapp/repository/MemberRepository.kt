package com.dev.eventapp.repository

import androidx.lifecycle.MutableLiveData
import com.dev.eventapp.models.Member
import com.dev.eventapp.utils.ResultStatus
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MemberRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    private val memberCollection: CollectionReference by lazy {
        db.collection("member")
    }

    private suspend fun addMember(member: Member): MutableLiveData<ResultStatus<Member>> =
        withContext(Dispatchers.IO) {
            val memberResultState = MutableLiveData<ResultStatus<Member>>()
            memberCollection.add(member)
                .addOnSuccessListener {
                    memberResultState.value = ResultStatus.Success(member)
                }
                .addOnFailureListener { exception ->
                    memberResultState.value = ResultStatus.Error(message = exception.message.toString())
                }.await()

            memberResultState
        }

    suspend fun signIn(account: GoogleSignInAccount): MutableLiveData<ResultStatus<Member>> =
        withContext(Dispatchers.IO) {
            val member = MutableLiveData<ResultStatus<Member>>()
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credentials)
                .addOnSuccessListener { authResult ->
                    val memberUser = authResult.user
                    if (memberUser != null) {
                        val newMember = Member(
                            uid = memberUser.uid,
                            name = memberUser.displayName,
                            phoneNumber = memberUser.phoneNumber,
                            email = memberUser.email,
                            photoUrl = memberUser.photoUrl.toString()
                        )
                        val isNewUser = authResult.additionalUserInfo!!.isNewUser
                        if (isNewUser) {
                            CoroutineScope(Dispatchers.IO).launch {
                                addMember(newMember)
                            }
                        }
                        member.value = ResultStatus.Success(newMember)
                    }
                }
                .addOnFailureListener { exception ->
                    member.value = ResultStatus.Error(message = exception.message.toString())
                }.await()

            member
        }
}