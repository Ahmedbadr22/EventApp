package com.dev.eventapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.eventapp.R
import com.dev.eventapp.databinding.FragmentAddEventBinding
import com.dev.eventapp.models.Event
import com.dev.eventapp.utils.ResultStatus
import com.dev.eventapp.view_models.EventViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AddEventFragment : Fragment() {
    private lateinit var binding: FragmentAddEventBinding
    private val fireAuth = Firebase.auth
    private val eventViewModel : EventViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEventBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackToList.setOnClickListener {
            findNavController().navigate(R.id.action_addEventFragment_to_listEventsFragment)
        }

        binding.btnAddEvent.setOnClickListener {
            val title = binding.editTextEventTitle.text.toString()
            val description = binding.editTextEventDescription.text.toString()
            val date = binding.editTextEventDate.text.toString()
            val instructorName = binding.editTextEventInstructorName.text.toString()
            val userUid = fireAuth.uid.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty() && instructorName.isNotEmpty() && userUid.isNotEmpty()) {
                val event = Event(title, description, date, userUid, instructorName)
                eventViewModel.addEvent(event)
            }
        }


        eventViewModel.event.observe(viewLifecycleOwner) { resultStatus ->
            when (resultStatus) {
                is ResultStatus.Success -> {
                    Toast.makeText(view.context, "Event Added Successfully", Toast.LENGTH_SHORT).show()
                }
                is ResultStatus.Error -> {
                    Toast.makeText(view.context, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(view.context, "Else", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}