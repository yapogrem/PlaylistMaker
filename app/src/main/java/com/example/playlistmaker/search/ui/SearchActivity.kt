package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.search.data.Debounce
import com.example.playlistmaker.search.ui.model.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: ActivityFindBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private var searchField: String = ""

    @SuppressLint("WrongViewCast", "MissingInflatedId", "NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel.getScreenStateLiveData().observe(this) {
            render(it)
        }
        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val debounce = Debounce(searchViewModel)
        trackAdapter = TrackAdapter(searchViewModel)
        searchHistoryAdapter = SearchHistoryAdapter(searchViewModel)
        val inputEditText = binding.inputSearch
        binding.recyclerViewTrack.adapter = trackAdapter
        binding.recyclerSearchHistory.adapter = searchHistoryAdapter

        binding.searchBack.setOnClickListener {
            finish()
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack(binding.inputSearch.text.toString())
            }
            false
        }
        binding.refreshButton.setOnClickListener{
            findTrack(binding.inputSearch.text.toString())
        }
        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearSearchHistory()
            showEmptyScreen()
        }
        binding.clearSearch.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            debounce.canselSearchDebounce()
            showHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                showEmptyTracks()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty()!= true){
                    showProgressBar()
                    debounce.searchDebounce(binding.inputSearch.text.toString())
                }
                searchField = s.toString()
                binding.clearSearch.isVisible = clearButtonVisibility(s)
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    searchHistoryAdapter.notifyDataSetChanged()
                    showHistory()
                } else
                    showEmptyScreen()
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
        if (searchViewModel.getCountHistory() == 0) showEmptyScreen() else {
            searchHistoryAdapter.notifyDataSetChanged()
            binding.recyclerViewTrack.isVisible = false
            binding.networkError.isVisible = false
            binding.itemsNotFound.isVisible = false
            binding.searchHistory.isVisible = true
            binding.findProgressBar.isVisible = false
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks() {
        if(binding.inputSearch.text.isEmpty()){
            showHistory()
            return
        }
        trackAdapter.notifyDataSetChanged()
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
    @SuppressLint("NotifyDataSetChanged")
    private fun showEmptyTracks() {
        trackAdapter.notifyDataSetChanged()
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
    private fun showEmptyScreen() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = false
    }
    private fun findTrack(string: String){
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = true
        searchViewModel.findTrack(string)
    }
    private fun render(state: SearchState) {
        when (state) {
            SearchState.SEARCH_HISTORY -> showHistory()
            SearchState.SEARCH -> showEmptyTracks()
            SearchState.SEARCH_RESULTS -> showTracks()
            SearchState.NOTHING_FOUND -> showItemsNoFound()
            SearchState.SEARCH_ERROR -> showNetworkError()
            SearchState.PROGRESSBAR -> showProgressBar()
        }
    }
}
