package ru.kpfu.itis.galeev.android.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.SongEntity
import ru.kpfu.itis.galeev.android.myapplication.databinding.FavoriteSongItemBinding
import ru.kpfu.itis.galeev.android.myapplication.model.SongModel

class FavoriteSongsAdapter(private val songs : MutableList<SongModel> ) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return FavoriteSongHolder(FavoriteSongItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as FavoriteSongHolder).bindItem(songs[position])
    }

    fun updateItems(songModel : SongModel, isFavorite : Boolean) {
        songModel.isFavorite = isFavorite
        if (isFavorite) {
            songs.add(songModel)
            notifyItemInserted(songs.size - 1)
        } else {
            for ((index, song) in songs.withIndex()) {
                if (song.id == songModel.id) {
                    songs.removeAt(index)
                    notifyItemRemoved(index)
                    return
                }
            }
        }
    }

    fun getActualSongSize() : Int {
        return songs.size
    }

    inner class FavoriteSongHolder(private val viewBinding: FavoriteSongItemBinding) : ViewHolder(viewBinding.root) {
        fun bindItem(song : SongModel) {
            with(viewBinding) {
                tvSongAuthor.text = song.author
                tvSongTitle.text = song.title
            }
        }
    }
}