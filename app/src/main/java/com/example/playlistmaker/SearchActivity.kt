package com.example.playlistmaker
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var placeholderNetworkError: LinearLayout
    private lateinit var placeholderItemsNotFound: LinearLayout
    private lateinit var placeholderSearchHistory: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var recyclerSearchHistory: RecyclerView
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistory: SearchHistory

    private var tracks = ArrayList<Track>()
    private lateinit var track_adapter: TrackAdapter
    private lateinit var search_history_adapter: SearchHistoryAdapter
    private var searchField: String = ""

    @SuppressLint("WrongViewCast", "MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)


        val sharedPreferences = getSharedPreferences(App.PLAYLISTMAKER, MODE_PRIVATE)
        track_adapter = TrackAdapter(SearchHistory(sharedPreferences))
        searchHistory = SearchHistory(sharedPreferences)
        search_history_adapter = SearchHistoryAdapter(searchHistory)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val iTunesBaseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iTunesService = retrofit.create(ITunesApi::class.java)


        val displayMain = findViewById<ImageButton>(R.id.settings_back)
        displayMain.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.input_search)
        clearButton = findViewById(R.id.clear_search)
        recyclerViewTrack = findViewById(R.id.recyclerViewTrack)
        placeholderNetworkError = findViewById(R.id.network_error)
        placeholderItemsNotFound = findViewById(R.id.items_not_found)
        refreshButton = findViewById(R.id.refreshButton)
        placeholderSearchHistory = findViewById(R.id.searchHistory)
        recyclerSearchHistory = findViewById(R.id.recyclerSearchHistory)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)

        track_adapter.tracks = tracks
        recyclerViewTrack.adapter = track_adapter

        recyclerSearchHistory.adapter = search_history_adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showTracks()
                findTrack(iTunesService)
            }
            false
        }
        refreshButton.setOnClickListener{
            showTracks()
            findTrack(iTunesService)
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearSearchHistory()
            clearTracks()
        }


        clearButton.setOnClickListener {
            inputEditText.setText("")

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            search_history_adapter.notifyDataSetChanged()
            showHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchField = s.toString()
                clearButton.isVisible = clearButtonVisibility(s)
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    showHistory()
                } else clearTracks()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) showHistory() else clearTracks()
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
        if (searchHistory.getItemCount() == 0) clearTracks() else {
            search_history_adapter.notifyDataSetChanged()
            recyclerViewTrack.isVisible = false
            placeholderNetworkError.isVisible = false
            placeholderItemsNotFound.isVisible = false
            placeholderSearchHistory.isVisible = true
        }

    }
    private fun showTracks() {
        recyclerViewTrack.isVisible = true
        placeholderNetworkError.isVisible = false
        placeholderItemsNotFound.isVisible = false
        placeholderSearchHistory.isVisible = false
    }
    private fun showNetworkError() {
        recyclerViewTrack.isVisible = false
        placeholderNetworkError.isVisible = true
        placeholderItemsNotFound.isVisible = false
        placeholderSearchHistory.isVisible = false
    }
    private fun showItemsNoFound() {
        recyclerViewTrack.isVisible = false
        placeholderNetworkError.isVisible = false
        placeholderItemsNotFound.isVisible = true
        placeholderSearchHistory.isVisible = false
    }
    private fun clearTracks(){
        recyclerViewTrack.isVisible = false
        placeholderNetworkError.isVisible = false
        placeholderItemsNotFound.isVisible = false
        placeholderSearchHistory.isVisible = false
    }

    private fun findTrack(iTunesService:ITunesApi){
        iTunesService.searchTrack(inputEditText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            track_adapter.notifyDataSetChanged()
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
