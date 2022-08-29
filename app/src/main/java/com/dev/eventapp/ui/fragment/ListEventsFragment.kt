package com.dev.eventapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.eventapp.R
import com.dev.eventapp.adapters.EventAdapter
import com.dev.eventapp.databinding.FragmentListEventsBinding
import com.dev.eventapp.utils.ResultStatus
import com.dev.eventapp.view_models.EventViewModel

class ListEventsFragment : Fragment() {
    private lateinit var binding: FragmentListEventsBinding
    private val eventViewModel : EventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListEventsBinding.inflate(layoutInflater, container, false)
        eventViewModel.listEvents()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddEvent.setOnClickListener {
            findNavController().navigate(R.id.action_listEventsFragment_to_addEventFragment)
        }

        eventViewModel.events.observe(viewLifecycleOwner) { resultStatus ->
            when (resultStatus) {
                is ResultStatus.Success -> {
                    val events = resultStatus.data
                    binding.progressBarLoading.visibility = View.GONE

                    if(events!!.isEmpty()){
                        binding.tvNoItemsYet.visibility = View.VISIBLE
                    }else{
                        binding.rcvEvents.apply {
                            adapter = EventAdapter(events)
                            layoutManager = LinearLayoutManager(view.context)
                            hasFixedSize()
                        }

                        if (binding.tvNoItemsYet.isVisible) binding.tvNoItemsYet.visibility = View.GONE
                    }
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