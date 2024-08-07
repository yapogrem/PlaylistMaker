package com.example.playlistmaker.mediathek.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavoritesTracks : Fragment() {
    private var binding: FragmentFavoritesTracksBinding? = null
    private val favoritesTracksViewModel: FavoritesTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout {
        binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    companion object {
        fun newInstance() = FragmentFavoritesTracks()
    }
}
