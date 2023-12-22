package ru.kpfu.itis.galeev.android.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.databinding.SongItemBinding
import ru.kpfu.itis.galeev.android.myapplication.model.SongModel
import ru.kpfu.itis.galeev.android.myapplication.utils.TimeUtil
import kotlin.reflect.KFunction2

class SongListAdapter(
    private val songs: MutableList<SongModel>,
    private val onDelete : (SongModel, Int) -> Unit,
    private val onFavoriteClicked : (SongModel, Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return SongViewHolder(SongItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as SongViewHolder).bindItem(songs[position])
    }

    fun deleteItem(position : Int) {
        notifyItemRemoved(position)
        songs.removeAt(position)
    }

    fun changeFavoriteType(position: Int, isFavorite : Boolean) {
        songs[position].isFavorite = isFavorite
        notifyItemChanged(position)
    }

    fun getActualSize() : Int {
        return songs.size
    }


    inner class SongViewHolder(private val viewBinding : SongItemBinding) : ViewHolder(viewBinding.root) {
        fun bindItem(song : SongModel) {
            with(viewBinding) {
                tvSongTitle.text = song.title
                tvSongAuthor.text = song.author
                tvSongDuration.text = TimeUtil.convertIntToString(song.duration)
                btnAddFavorite.setImageResource(
                    if (song.isFavorite) R.drawable.ic_star_filled else R.drawable.outlined_star
                )
            }
        }

        init {
            with(viewBinding) {
                btnRemoveSong.setOnClickListener {
                    onDelete.invoke(songs[adapterPosition], adapterPosition)
                }
                btnAddFavorite.setOnClickListener {
                    onFavoriteClicked.invoke(songs[adapterPosition], adapterPosition)
                }

            }
        }
    }

}