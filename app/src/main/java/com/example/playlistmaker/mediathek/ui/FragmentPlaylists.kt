package com.example.playlistmaker.mediathek.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentPlaylists : Fragment() {
    private var binding: FragmentPlaylistsBinding? = null
    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = FragmentPlaylists()
        }
    }

