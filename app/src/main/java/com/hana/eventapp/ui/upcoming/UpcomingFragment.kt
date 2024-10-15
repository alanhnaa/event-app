package com.hana.eventapp.ui.upcoming

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hana.eventapp.databinding.FragmentUpcomingBinding
import com.hana.eventapp.ui.detail.DetailActivity

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UpcomingAdapter
    private lateinit var viewModel: UpcomingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        // Setup RecyclerView and Adapter
        adapter = UpcomingAdapter { event ->
            navigateToDetail(event.id.toString())
        }
        binding.rvUpcoming.apply {
            adapter = this@UpcomingFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)  // Optimize performance
        }

        // Observe ViewModel LiveData
        viewModel.events.observe(viewLifecycleOwner) { events ->
            Log.d("UpcomingFragment", "Number of events: ${events?.size}")
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // Check network and load events
        if (isNetworkAvailable(requireContext())) {
            viewModel.getUpcomingEvents()
        } else {
            showLoading(false)
            binding.noInternet.visibility = View.VISIBLE
        }
    }

    private fun navigateToDetail(eventId: String) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }

    @SuppressLint("NewApi")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
