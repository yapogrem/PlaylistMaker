package com.example.playlistmaker.search.ui
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ui.player.Debounce
import com.example.playlistmaker.PLAYLIST_MAKER
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.SearchHistoryAdapter
import com.example.playlistmaker.TrackAdapter
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.models.Track
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindBinding
    private lateinit var searchHistory: SearchHistory
    private var tracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private var searchField: String = ""

    @SuppressLint("WrongViewCast", "MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchRunnable = Runnable { findTrack() }
        val debounce = Debounce()
        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        trackAdapter = TrackAdapter(searchHistory)
        searchHistoryAdapter = SearchHistoryAdapter(searchHistory)
        val inputEditText = binding.inputSearch
        trackAdapter.tracks = tracks
        binding.recyclerViewTrack.adapter = trackAdapter
        binding.recyclerSearchHistory.adapter = searchHistoryAdapter

        binding.searchBack.setOnClickListener {
            finish()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack()
            }
            false
        }
        binding.refreshButton.setOnClickListener{
            findTrack()
        }
        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clearSearchHistory()
            showEmptyTracks()
        }
        binding.clearSearch.setOnClickListener {
            inputEditText.setText("")

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            debounce.canselSearchDebounce(searchRunnable)
            showHistory()
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty()!= true){
                    debounce.searchDebounce(searchRunnable)
                }
                searchField = s.toString()
                binding.clearSearch.isVisible = clearButtonVisibility(s)
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    searchHistoryAdapter.notifyDataSetChanged()
                    showHistory()
                } else
                    showEmptyTracks()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) showHistory() else showEmptyTracks()
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchField)
        showHistory()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchField", searchField)
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchField = savedInstanceState.getString("searchField") ?: ""
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return s?.isNotBlank() == true
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        searchHistoryAdapter.notifyDataSetChanged()
        if (searchHistory.getItemCount() == 0) showEmptyTracks() else {
            searchHistoryAdapter.notifyDataSetChanged()
            binding.recyclerViewTrack.isVisible = false
            binding.networkError.isVisible = false
            binding.itemsNotFound.isVisible = false
            binding.searchHistory.isVisible = true
            binding.findProgressBar.isVisible = false
        }

    }
    private fun showTracks() {
        binding.recyclerViewTrack.isVisible = true
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = false
    }
    private fun showNetworkError() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = true
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = false
    }
    private fun showItemsNoFound() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = true
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = false
    }

    private fun showEmptyTracks() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = true
        binding.findProgressBar.isVisible = false
    }

    private fun showProgressBar() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = true
    }

    private fun findTrack() {
        showProgressBar()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val iTunesBaseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iTunesService = retrofit.create(ITunesApi::class.java)


        iTunesService.searchTrack(binding.inputSearch.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            response.body()?.results?.forEach { println(it)  }
                            tracks.addAll(response.body()?.results!!.map { Track.mapped(it) })
                            trackAdapter.notifyDataSetChanged()
                            showTracks()
                        }
                        if (tracks.isEmpty()) {
                            showItemsNoFound()
                        }
                    }
                }
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showNetworkError()
                }
            })
    }
}
