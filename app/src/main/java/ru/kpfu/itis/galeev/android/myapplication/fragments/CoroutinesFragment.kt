package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentCoroutinesBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.CoroutinesConfig

class CoroutinesFragment : AirplaneBtnsFragment(R.layout.fragment_coroutines) {
    var _viewBinding : FragmentCoroutinesBinding? = null
    val viewBinding : FragmentCoroutinesBinding get() = _viewBinding!!
    var job : Job? = null
    var deferreds : MutableList<Deferred<Unit>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentCoroutinesBinding.inflate(inflater)
        (requireActivity() as BaseActivity).changeTitleBar()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        with(viewBinding) {
            seekbarCoroutines.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvCountCoroutine.text = progress.toString()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            btnStartCoroutines.setOnClickListener {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val coroutineCount = tvCountCoroutine.text.toString().toInt()
                        if (switchAsync.isChecked) {
                            deferreds = mutableListOf()
                            deferreds?.let { deferreds ->
                                repeat(coroutineCount) {
                                    val deferred = async {
                                        CoroutinesConfig.doWork(it)
                                    }
                                    deferreds.add(deferred)
                                }
                                deferreds.awaitAll()
                            }
                        } else {
                            var cancelCoroutine = false
                            repeat(coroutineCount) {
                                job = launch {
                                    CoroutinesConfig.doWork(it)
                                }
                                job?.let { job ->
                                    if (cancelCoroutine) job.cancel()
                                    job.join()
                                    if (job.isCancelled) cancelCoroutine = true
                                }
                            }
                        }
                    }

                    if (job != null || deferreds != null) {
                        Toast.makeText(requireContext(), "End", Toast.LENGTH_SHORT).show()
                        job = null
                        deferreds = null
                    }
                }
            }
        }
    }

    override fun onStop() {
        if (viewBinding.switchStopBackground.isChecked) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    job?.cancel()
                    deferreds?.let {
                        for (deferred in it) {
                            if (deferred.isActive) deferred.cancel()
                        }
                    }
                    job = null
                    deferreds = null
                }
            }
        }
        super.onStop()
    }

    override fun changeBtnByModeState(state: Boolean) {
        _viewBinding?.let {viewBinding ->
            viewBinding.btnStartCoroutines.isEnabled = state
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job = null
        deferreds = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}