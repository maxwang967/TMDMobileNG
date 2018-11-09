package com.morningstarwang.tmdmobileng.ui.collect

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.morningstarwang.tmdmobileng.App
import com.morningstarwang.tmdmobileng.R
import com.morningstarwang.tmdmobileng.REAL_MODE
import com.morningstarwang.tmdmobileng.databinding.FragmentCollectBinding
import com.morningstarwang.tmdmobileng.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_collect.*
import org.jetbrains.anko.support.v4.toast

class CollectFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentViewModel = ViewModelProviders.of(this).get(CollectViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentCollectBinding>(
            inflater, R.layout.fragment_collect, container, false
        ).apply {
            viewModel = currentViewModel as CollectViewModel
            setLifecycleOwner(this@CollectFragment)
            lifecycle.addObserver(viewModel as LifecycleObserver)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (App.isCollecting) {
            btnCollect.isChecked = true
        }
        btnCollect.setOnCheckedChangeListener { _, isChecked ->
            if (REAL_MODE == -1){
                toast(getString(R.string.alert_select_mode_first))
                btnCollect.isChecked = false
                return@setOnCheckedChangeListener
            }
            (currentViewModel as CollectViewModel)?.btnCollectOnClick(isChecked, activity)
        }
    }






}
