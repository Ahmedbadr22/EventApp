package com.dev.eventapp.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.eventapp.models.Event
import com.dev.eventapp.repository.EventRepository
import com.dev.eventapp.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val eventRepository: EventRepository by lazy {
        EventRepository()
    }

    private val _events: MutableLiveData<ResultStatus<List<Event>>> =
        MutableLiveData<ResultStatus<List<Event>>>()
    val events: LiveData<ResultStatus<List<Event>>>
        get() = _events

    private val _event: MutableLiveData<ResultStatus<Event>> =
        MutableLiveData<ResultStatus<Event>>()
    val event: LiveData<ResultStatus<Event>>
        get() = _event


    fun listEvents() {
        viewModelScope.launch(Dispatchers.Main) {
            _events.postValue(eventRepository.listEvents().value)
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch(Dispatchers.Main) {
            _event.postValue(eventRepository.addEvent(event).value)
        }
    }
}