package com.hana.eventapp.ui.finished

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
import com.hana.eventapp.databinding.FragmentFinishedBinding
import com.hana.eventapp.ui.detail.DetailActivity

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private lateinit var adapter: FinishedAdapter
    private lateinit var viewModel: FinishedViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[FinishedViewModel::class.java]

        adapter = FinishedAdapter {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", it.id)
            startActivity(intent)
        }

        binding.rvFinished.adapter = adapter
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())

        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        if (isNetworkAvailable(requireContext())) {
            viewModel.getFinishedEvents()
        } else {
            showLoading(false)
            binding.noInternet.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @SuppressLint("NewApi")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
