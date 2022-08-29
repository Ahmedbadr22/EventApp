package com.dev.eventapp.repository

import androidx.lifecycle.MutableLiveData
import com.dev.eventapp.models.Event
import com.dev.eventapp.models.Member
import com.dev.eventapp.utils.ResultStatus
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val eventCollection: CollectionReference by lazy {
        db.collection("event")
    }

    suspend fun addEvent(event: Event): MutableLiveData<ResultStatus<Event>> =
        withContext(Dispatchers.IO) {
            val resultStatus = MutableLiveData<ResultStatus<Event>>()
            eventCollection.add(event)
                .addOnSuccessListener {
                    resultStatus.value = ResultStatus.Success(event)
                }
                .addOnFailureListener { exception ->
                    val errorMessage = exception.message.toString()
                    resultStatus.value = ResultStatus.Error(message = errorMessage)
                }.await()

            resultStatus
        }

    suspend fun listEvents(): MutableLiveData<ResultStatus<List<Event>>> =
        withContext(Dispatchers.IO) {
            val resultStateList = MutableLiveData<ResultStatus<List<Event>>>()

            eventCollection.get()
                .addOnSuccessListener {
                    val events = it.toObjects<Event>()
                    resultStateList.value = ResultStatus.Success(events)
                }
                .addOnFailureListener { exception ->
                    val errorMessage = exception.message.toString()
                    resultStateList.value = ResultStatus.Error(message = errorMessage)
                }.await()

            resultStateList
        }
}