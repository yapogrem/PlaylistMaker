package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFindBinding
import com.example.playlistmaker.search.ui.model.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentFindBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private var searchField: String = ""
    private lateinit var screenState: ScreenState

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFindBinding.inflate(inflater, container, false)
        searchViewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
        screenState = ScreenState(binding)
        val debounce = Debounce(searchViewModel)
        trackAdapter = TrackAdapter(searchViewModel)
        searchHistoryAdapter = SearchHistoryAdapter(searchViewModel)
        val inputEditText = binding.inputSearch
        binding.recyclerViewTrack.adapter = trackAdapter
        binding.recyclerSearchHistory.adapter = searchHistoryAdapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack(binding.inputSearch.text.toString())
            }
            false
        }
        binding.refreshButton.setOnClickListener {
            findTrack(binding.inputSearch.text.toString())
        }
        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearSearchHistory()
            screenState.showEmptyScreen()
        }
        binding.clearSearch.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            debounce.canselSearchDebounce()
            showHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                showEmptyTracks()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() != true) {
                    screenState.showProgressBar()
                    debounce.searchDebounce(binding.inputSearch.text.toString())
                }
                searchField = s.toString()
                binding.clearSearch.isVisible = clearButtonVisibility(s)
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    showHistory()
                } else
                    screenState.showEmptyScreen()
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

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchField", searchField)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            searchField = savedInstanceState.getString("searchField") ?: ""
        }
    }


    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return s?.isNotBlank() == true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        binding.recyclerViewTrack.recycledViewPool.clear()
        searchHistoryAdapter.notifyDataSetChanged()
        if (searchViewModel.getCountHistory() == 0) screenState.showEmptyScreen() else {
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
        if (binding.inputSearch.text.isEmpty()) {
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

    @SuppressLint("NotifyDataSetChanged")
    private fun showEmptyTracks() {
        trackAdapter.notifyDataSetChanged()
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = true
        binding.findProgressBar.isVisible = false
    }

    private fun findTrack(string: String) {
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
            SearchState.NOTHING_FOUND -> screenState.showItemsNoFound()
            SearchState.SEARCH_ERROR -> screenState.showNetworkError()
            SearchState.PROGRESSBAR -> screenState.showProgressBar()
        }
    }
}