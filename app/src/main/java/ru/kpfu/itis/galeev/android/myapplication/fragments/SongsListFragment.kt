package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.adapter.FavoriteSongsAdapter
import ru.kpfu.itis.galeev.android.myapplication.adapter.SongListAdapter
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.data.db.converters.SongTypeConverter
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.FavoriteSongsDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.SongDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.FavoriteSongsEntity
import ru.kpfu.itis.galeev.android.myapplication.databinding.SongsListFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.model.SongModel

class SongsListFragment : BaseFragment(R.layout.songs_list_fragment) {
    var _viewBinding : SongsListFragmentBinding? = null
    val viewBinding : SongsListFragmentBinding get() = _viewBinding!!

    var rvSongListAdapter : SongListAdapter? = null

    var rvFavoriteSongsAdapter : FavoriteSongsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = SongsListFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val songDao : SongDao = ServiceLocator.getDbInstance(requireContext()).songDao
        val favoriteSongsDao : FavoriteSongsDao = ServiceLocator.getDbInstance(requireContext()).favoriteSongsDao
        val songTypeConverter = SongTypeConverter()
        val userId : Int = ServiceLocator.getUserId()
        with(viewBinding) {
            lifecycleScope.launch(Dispatchers.IO) {
                val songs : MutableList<SongModel> = mutableListOf()
                val favoriteSongs : MutableList<SongModel> = mutableListOf()
                val favoriteSongsFromDb = kotlin.runCatching {
                    favoriteSongsDao.getUserSongs(userId)
                }
                if (favoriteSongsFromDb.isSuccess) {
                    favoriteSongs.addAll(favoriteSongsFromDb.getOrDefault(mutableListOf()).map { songEntity ->
                        songTypeConverter.fromSongEntityToSongModel(songEntity, true)
                    })
                } else {
                    println("TEST TAG songs uploading exc - ${favoriteSongsFromDb.exceptionOrNull()}")
                }

                val songsFromDb = kotlin.runCatching {
                    songDao.getAll()
                }
                if (songsFromDb.isSuccess) {
                    val favoriteSongEntities = favoriteSongsFromDb.getOrDefault(mutableListOf())
                    songs.addAll(songsFromDb.getOrDefault(mutableListOf()).map { songEntity ->
                        songTypeConverter.fromSongEntityToSongModel(
                            songEntity,
                            favoriteSongEntities.contains(songEntity)
                        )
                    })
                } else {
                    println("TEST TAG songs uploading exc - ${songsFromDb.exceptionOrNull()}")
                }
                withContext(Dispatchers.Main) {
                    rvSongListAdapter = SongListAdapter(songs, ::onDeleteSongCallback, ::onFavoriteClickedCallback)
                    rvFavoriteSongsAdapter = FavoriteSongsAdapter(favoriteSongs)

                    rvSongs.adapter = rvSongListAdapter
                    rvFavoriteSongs.adapter = rvFavoriteSongsAdapter

                    updateItemSize()

                }

            }

        }
    }

    private fun onDeleteSongCallback(songModel: SongModel, deletedPosition : Int) {
        val songDao : SongDao = ServiceLocator.getDbInstance(requireContext()).songDao
        val songTypeConverter = SongTypeConverter()
        lifecycleScope.launch(Dispatchers.IO) {
            val deleteResult = kotlin.runCatching {
                songDao.deleteSong(songTypeConverter.fromSongModelToSongEntity(songModel))
            }
            if (deleteResult.isSuccess) {
                withContext(Dispatchers.Main) {
                    rvSongListAdapter?.deleteItem(deletedPosition)
                    rvFavoriteSongsAdapter?.updateItems(songModel, false)
                    updateItemSize()
                }
            } else {
                val alertBuilder = AlertDialog.Builder(requireContext())
                println("TEST TAG FAILURE DELETE - ${deleteResult.exceptionOrNull()}")
                alertBuilder.setMessage(getString(R.string.failure_delete_song))
            }

        }
    }

    private fun onFavoriteClickedCallback(songModel : SongModel, position : Int) {
        val favoriteSongsDao : FavoriteSongsDao = ServiceLocator.getDbInstance(requireContext()).favoriteSongsDao
        val userId = ServiceLocator.getUserId()
        val isFavorite : Boolean = !songModel.isFavorite
        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteSongsEntity = FavoriteSongsEntity(
                userId,
                songModel.id
            )
            if (isFavorite) {
                favoriteSongsDao.addSongToFavorite(favoriteSongsEntity)
            } else {
                favoriteSongsDao.deleteSongsFromFavorite(favoriteSongsEntity)
            }
            withContext(Dispatchers.Main) {
                rvSongListAdapter?.changeFavoriteType(position, isFavorite)
                rvFavoriteSongsAdapter?.updateItems(songModel, isFavorite)
                updateItemSize()
            }
        }
    }

    private fun updateItemSize() {
        with(viewBinding) {
            layoutNoFavoriteSongs.isVisible = rvFavoriteSongsAdapter?.getActualSongSize() == 0
            layoutSongsNotFound.isVisible = rvSongListAdapter?.getActualSize() == 0
            println("TEST TAG - ${layoutSongsNotFound.isVisible} - visibility")
        }
    }
}