package com.morningstarwang.tmdmobileng.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.morningstarwang.tmdmobileng.R
import com.morningstarwang.tmdmobileng.REAL_MODE
import com.morningstarwang.tmdmobileng.TIMESTAMP
import com.morningstarwang.tmdmobileng.databinding.FragmentMainBinding
import com.morningstarwang.tmdmobileng.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.support.v4.toast

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

        btnModeSelect.setOnCheckedChangeListener { _, isChecked ->
            if(!checkIfAtLeastOneRadioChecked()){
                toast(getString(R.string.alert_at_least_one_mode))
                btnModeSelect.isChecked  = false
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
                    rbBus.isChecked -> REAL_MODE =  5
                    rbTrain.isChecked -> REAL_MODE = 6
                    rbSubway.isChecked -> REAL_MODE = 7
                }
                disableRadios()
            }else{
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

    private fun setRadios(realMode: Int){
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
