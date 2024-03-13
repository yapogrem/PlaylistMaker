package com.example.playlistmaker
import androidx.core.view.isVisible
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
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
    private lateinit var trackAdapter: RecyclerView
    private lateinit var placeholderNetworkError: LinearLayout
    private lateinit var placeholderItemsNotFound: LinearLayout
    private lateinit var placeholder: LinearLayout
    private lateinit var refreshButton: Button


    private var tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()
    private var searchText: String = ""
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

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
        trackAdapter = findViewById(R.id.recyclerTrack)
        placeholderNetworkError = findViewById(R.id.network_error)
        placeholderItemsNotFound = findViewById(R.id.items_not_found)
        refreshButton = findViewById(R.id.refreshButton)


        adapter.tracks = tracks

        trackAdapter.adapter = adapter

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

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            clearTracks()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                clearButton.isVisible = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchText", searchText)
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("searchText") ?: ""
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return s?.isNotBlank() == true
    }

    private fun showTracks() {
        trackAdapter.visibility = View.VISIBLE
        placeholderNetworkError.visibility = View.GONE
        placeholderItemsNotFound.visibility = View.GONE
    }
    private fun showNetworkError() {
        trackAdapter.visibility = View.GONE
        placeholderNetworkError.visibility = View.VISIBLE
        placeholderItemsNotFound.visibility = View.GONE
    }
    private fun showItemsNoFound() {
        trackAdapter.visibility = View.GONE
        placeholderNetworkError.visibility = View.GONE
        placeholderItemsNotFound.visibility = View.VISIBLE
    }
    private fun clearTracks(){
        trackAdapter.visibility = View.GONE
        placeholderNetworkError.visibility = View.GONE
        placeholderItemsNotFound.visibility = View.GONE
    }

    private fun findTrack(iTunesService:ITunesApi){
        iTunesService.searchTrack(inputEditText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
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
