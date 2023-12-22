package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.VotesDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.SongEntity
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.VotesEntity
import ru.kpfu.itis.galeev.android.myapplication.databinding.SongInfoFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.utils.ParamsKey
import ru.kpfu.itis.galeev.android.myapplication.utils.TimeUtil

class SongInfoFragment : BaseFragment(R.layout.song_info_fragment) {
    var _viewBinding : SongInfoFragmentBinding? = null
    val viewBinding : SongInfoFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = SongInfoFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var songId : Int? = null
        arguments?.apply {
            songId = getInt(ParamsKey.SONG_ID)
        }
        songId?.let {
            initViews(it)
        }

    }

    private fun initViews(songId : Int) {
        val songDao = ServiceLocator.getDbInstance(requireContext()).songDao
        val votesDao = ServiceLocator.getDbInstance(requireContext()).votesDao
        val userId = ServiceLocator.getUserId()
        with(viewBinding) {
            lifecycleScope.launch(Dispatchers.IO) {
                val songEntity = songDao.getSongById(songId)
                val userVote = getUserVote(userId, songId, votesDao)
                val songRating = getSongRating(songId, votesDao)

                withContext(Dispatchers.Main) {
                    if (userVote != null) {
                        tvUserRatingValue.text = userVote.toString()
                        seekbarRate.progress = userVote
                    }
                    if (songRating != null) {
                        tvSongRating.text = songRating.toString()
                    }

                    tvSongAuthor.text = songEntity.author
                    tvSongTitle.text = songEntity.title
                    tvSongDuration.text = TimeUtil.convertIntToString(songEntity.duration)
                    tvSongText.text = songEntity.text

                    seekbarRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            tvUserRatingValue.text = progress.toString()
                        }
                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })

                    btnVote.setOnClickListener {
                        val userVoteValue = seekbarRate.progress
                        lifecycleScope.launch(Dispatchers.IO) {
                            val result = kotlin.runCatching {
                                votesDao.rateSong(
                                    VotesEntity(
                                        userId,
                                        songId,
                                        userVoteValue
                                    )
                                )
                            }
                            withContext(Dispatchers.Main) {
                                if (result.isFailure) {
                                    val alertBuilder = AlertDialog.Builder(requireContext())
                                    alertBuilder.setMessage(getString(R.string.failure_vote))
                                    alertBuilder.show()
                                    println("TEST TAG exception while voting - ${result.exceptionOrNull()}")
                                } else {
                                    withContext(Dispatchers.IO) {
                                        val newRating = getSongRating(songId, votesDao)
                                        if (newRating != null) {
                                            withContext(Dispatchers.Main) {
                                                tvSongRating.text = newRating.toString()
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

                }
            }
        }
    }

    private fun getUserVote(userId : Int, songId : Int, votesDao: VotesDao) : Int? {
        val userVote : List<VotesEntity> = votesDao.getRate(userId, songId)
        if (userVote.isNotEmpty()) return userVote[0].rate
        return null
    }

    private fun getSongRating(songId : Int, votesDao: VotesDao) : Double? {
        val rates = votesDao.getRatings(songId)
        if (rates.isEmpty()) return null
        var sum = 0
        rates.forEach { vote ->
            sum += vote.rate
        }
        return sum.div(rates.size.toDouble())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}