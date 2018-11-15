package com.morningstarwang.tmdmobileng.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProviders
import com.morningstarwang.tmdmobileng.*
import com.morningstarwang.tmdmobileng.databinding.FragmentMainBinding
import com.morningstarwang.tmdmobileng.ui.BaseFragment
import com.morningstarwang.tmdmobileng.utils.FileUtils
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import java.net.URL

class MainFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater, R.layout.fragment_main, container, false
        ).apply {

            viewModel = currentViewModel as MainViewModel
            setLifecycleOwner(this@MainFragment)
            lifecycle.addObserver(viewModel as LifecycleObserver)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (REAL_MODE != -1) {
            setRadios(REAL_MODE)
            disableRadios()
            btnModeSelect.isChecked = true
        }
        doAsync {
            val url = URL(ANNOUCEMENT_URL)
            val content = url.readText()
            uiThread {
                markdownView.loadMarkdown(content)
            }
        }
        btnReset.setOnClickListener {
            if (btnModeSelect.isChecked || App.isCollecting || App.isPredicting) {
                toast(getString(R.string.alert_reset_before))
                return@setOnClickListener
            }
            AlertDialog.Builder(context).apply {
                setTitle(getString(R.string.tip))
                setMessage(getString(R.string.alert_confirm_reset))
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    FileUtils.deleteFile("tmd_ng_cache.bin")
                    App.currentCorrectCountVote = arrayListOf(0, 0, 0, 0, 0)
                    App.currentAllCountVote = arrayListOf(0, 0, 0, 0, 0)
                    for (i in 0..4) {
                        for (j in 0..8) {
                            for (k in 0..8) {
                                App.currentConfusionValues[i][j][k] = 0
                            }
                        }
                    }
                    for (i in 0..4) {
                        for (j in 0..8) {
                            for (k in 0..8) {
                                App.confusionValues[i][j][k] = 0
                            }
                        }
                    }
                    App.predictResult = arrayListOf("N/A", "N/A", "N/A", "N/A", "N/A")
                    App.voteResult = arrayListOf("N/A", "N/A", "N/A", "N/A", "N/A")

                    App.totalCorrectCountVote = arrayListOf(0, 0, 0, 0, 0)
                    App.totalAllCountVote = arrayListOf(0, 0, 0, 0, 0)

                    App.currentCorrectCountVote = arrayListOf(0, 0, 0, 0, 0)
                    App.currentAllCountVote = arrayListOf(0, 0, 0, 0, 0)
                    toast(getString(R.string.alert_reset_ok))
                }
                setNegativeButton(getString(R.string.no)) { _, _ ->
                }
            }.create().show()
        }

        btnModeSelect.setOnCheckedChangeListener { _, isChecked ->
            if (!checkIfAtLeastOneRadioChecked()) {
                toast(getString(R.string.alert_at_least_one_mode))
                btnModeSelect.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (App.isCollecting ||
                App.isPredicting
            ) {
                toast("请先停止数据采集和模式识别操作。")
                btnModeSelect.isChecked = true
                return@setOnCheckedChangeListener
            }
            if (isChecked) {
                TIMESTAMP = System.currentTimeMillis()
                when {
                    rbStill.isChecked -> REAL_MODE = 0
                    rbWalk.isChecked -> REAL_MODE = 1
                    rbRun.isChecked -> REAL_MODE = 2
                    rbBike.isChecked -> REAL_MODE = 3
                    rbCar.isChecked -> REAL_MODE = 4
                    rbBus.isChecked -> REAL_MODE = 5
                    rbTrain.isChecked -> REAL_MODE = 6
                    rbSubway.isChecked -> REAL_MODE = 7
                }
                disableRadios()
            } else {
                rbStill.isChecked = false
                rbWalk.isChecked = false
                rbRun.isChecked = false
                rbBike.isChecked = false
                rbCar.isChecked = false
                rbBus.isChecked = false
                rbTrain.isChecked = false
                rbSubway.isChecked = false
                REAL_MODE = -1
                enableRadios()
            }
        }
    }

    private fun checkIfAtLeastOneRadioChecked(): Boolean {
        return rbStill.isChecked ||
                rbWalk.isChecked ||
                rbRun.isChecked ||
                rbBike.isChecked ||
                rbCar.isChecked ||
                rbBus.isChecked ||
                rbTrain.isChecked ||
                rbSubway.isChecked
    }

    private fun disableRadios() {
        rbStill.isEnabled = false
        rbWalk.isEnabled = false
        rbRun.isEnabled = false
        rbBike.isEnabled = false
        rbCar.isEnabled = false
        rbBus.isEnabled = false
        rbTrain.isEnabled = false
        rbSubway.isEnabled = false
    }

    private fun enableRadios() {
        rbStill.isEnabled = true
        rbWalk.isEnabled = true
        rbRun.isEnabled = true
        rbBike.isEnabled = true
        rbCar.isEnabled = true
        rbBus.isEnabled = true
        rbTrain.isEnabled = true
        rbSubway.isEnabled = true
    }

    private fun setRadios(realMode: Int) {
        when (realMode) {
            0 -> rbStill.isChecked = true
            1 -> rbWalk.isChecked = true
            2 -> rbRun.isChecked = true
            3 -> rbBike.isChecked = true
            4 -> rbCar.isChecked = true
            5 -> rbBus.isChecked = true
            6 -> rbTrain.isChecked = true
            7 -> rbSubway.isChecked = true
        }
    }


}
