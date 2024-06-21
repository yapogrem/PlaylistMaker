package com.example.playlistmaker.mediathek.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding

class FragmentFavoritesTracks : Fragment() {
    private lateinit var binding: FragmentFavoritesTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout {
        binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
}
