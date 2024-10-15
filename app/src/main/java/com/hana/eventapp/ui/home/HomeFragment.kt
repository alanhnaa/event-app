package com.hana.eventapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hana.eventapp.databinding.FragmentHomeBinding
import com.hana.eventapp.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!  // Properti binding valid antara onCreateView dan onDestroyView

    private lateinit var adapter: HomeUpcomingAdapter
    private lateinit var adapterFinished: HomeFinishedAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Inisialisasi adapter untuk upcoming events
        adapter = HomeUpcomingAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        // Inisialisasi adapter untuk finished events
        adapterFinished = HomeFinishedAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        // Setup RecyclerView untuk upcoming events
        binding.rvHomeUpcoming.apply {
            adapter = this@HomeFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        // Setup RecyclerView untuk finished events
        binding.rvHomeFinished.apply {
            adapter = this@HomeFragment.adapterFinished
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        // Observe LiveData dari ViewModel
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            adapterFinished.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // Cek koneksi internet dan ambil data
        if (isNetworkAvailable(requireContext())) {
            showLoading(true)
            viewModel.getHomeUpcomingEvents()
            viewModel.getHomeFinishedEvents()
        } else {
            showLoading(false)
            binding.noInternet.visibility = View.VISIBLE
        }
    }

    // Fungsi untuk menampilkan atau menyembunyikan progress bar
    private fun showLoading(isLoading: Boolean) {
        binding.pbHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Fungsi untuk memeriksa konektivitas jaringan
    @SuppressLint("NewApi")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
