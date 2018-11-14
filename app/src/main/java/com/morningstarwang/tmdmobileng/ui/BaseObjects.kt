package com.morningstarwang.tmdmobileng.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

open class BaseFragment : Fragment() {
    lateinit var currentViewModel: ViewModel
}

open class BaseViewModel : ViewModel(), LifecycleObserver