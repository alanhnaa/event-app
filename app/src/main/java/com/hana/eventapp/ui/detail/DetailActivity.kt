package com.hana.eventapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hana.eventapp.R
import com.hana.eventapp.data.response.Event
import com.hana.eventapp.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.SimpleTimeZone

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val eventID = intent.getIntExtra("EVENT_ID", -1)

        viewModel.getEvent(eventID.toString())

        viewModel.event.observe(this) {
            setDetail(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setDetail(event: Event) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(event.beginTime)
        val formattedDate = date?.let { outputFormat.format(it) }

        val inputTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputTimeFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        val timeStart = inputTimeFormat.parse(event.beginTime)
        val timeEnd = inputTimeFormat.parse(event.endTime)
        val formattedTimeStart = timeStart?.let { outputTimeFormat.format(it) }
        val formattedTimeEnd = timeEnd?.let { outputTimeFormat.format(it) }

        binding.tvName.text = event.name
        binding.owner.text = event.ownerName
        binding.quotaValue.text = event.quota.toString()
        binding.registrantValue.text = event.registrants.toString()
        binding.locationValue.text = event.cityName
        binding.timeValue.text = "$formattedDate $formattedTimeStart - $formattedTimeEnd"
        binding.desc.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        Glide.with(binding.ivCover.context)
            .load(event.mediaCover)
            .into(binding.ivCover)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }
    }
}