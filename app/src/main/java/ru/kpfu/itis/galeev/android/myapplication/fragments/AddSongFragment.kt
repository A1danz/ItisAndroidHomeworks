package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.SongEntity
import ru.kpfu.itis.galeev.android.myapplication.databinding.AddSongFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator

class AddSongFragment : BaseFragment(R.layout.add_song_fragment) {
    var _viewBinding : AddSongFragmentBinding? = null
    val viewBinding : AddSongFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = AddSongFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        val songDao = ServiceLocator.getDbInstance(requireContext()).songDao
        val alertBuilder = AlertDialog.Builder(requireContext())
        with(viewBinding) {
            btnAddSong.setOnClickListener {
                // check for validation
                val title : String = etSongTitle.text.toString()
                val author : String = etSongAuthor.text.toString()
                val duration : String = etSongDuration.text.toString()
                val text : String = etSongDuration.text.toString()
                val durationInSeconds = convertDurationToSeconds(duration)

                lifecycleScope.launch(Dispatchers.IO) {
                    var addingResultText = ""
                    val addResult = kotlin.runCatching {
                        songDao.addSong(SongEntity(
                            null,
                            title,
                            author,
                            durationInSeconds,
                            text
                            )
                        )
                    }
                    if (addResult.isSuccess) {
                        if (addResult.getOrDefault(0) > 0) {
                            addingResultText = "Песня успешно добавлена!"
                        } else {
                            addingResultText = "Песня не добавлена."
                        }
                    } else {
                        println("TEST TAG - exc: ${addResult.exceptionOrNull()}")
                        addingResultText = "Произошла непредвиденная ошибка при добавлении музыка"
                    }
                    withContext(Dispatchers.Main) {
                        alertBuilder.setMessage(addingResultText)
                        alertBuilder.show()
                    }
                }
            }
        }
    }

    private fun convertDurationToSeconds(durString : String) : Int {
        val durValues = durString.split(":")
        val minutes = durValues[0].toInt()
        val seconds = durValues[1].toInt()
        return minutes * 60 + seconds
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}