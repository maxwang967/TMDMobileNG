package com.morningstarwang.tmdmobileng.ui.detection


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.morningstarwang.tmdmobileng.App
import com.morningstarwang.tmdmobileng.DETECTION_MODEL
import com.morningstarwang.tmdmobileng.R
import com.morningstarwang.tmdmobileng.REAL_MODE
import com.morningstarwang.tmdmobileng.databinding.FragmentDetectionBinding
import com.morningstarwang.tmdmobileng.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_detection.*
import org.jetbrains.anko.support.v4.toast
import java.lang.ref.WeakReference

class DetectionFragment : BaseFragment() {

    val handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){

            }
            super.handleMessage(msg)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentViewModel = ViewModelProviders.of(this).get(DetectionViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentDetectionBinding>(
            inflater, R.layout.fragment_detection, container, false
        ).apply {
            viewModel = currentViewModel as DetectionViewModel
            setLifecycleOwner(this@DetectionFragment)
            lifecycle.addObserver(viewModel as LifecycleObserver)
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConfusionMatrix()
        refreshDetectionModeUI()
        if(App.isPredicting){
            btnDetection.isChecked = true
        }
        btnDetection.setOnCheckedChangeListener{_, isChecked ->
            if (REAL_MODE == -1){
                toast(getString(R.string.alert_select_mode_first))
                btnDetection.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (!App.isCollecting){
                toast(getString(R.string.alert_collect_first))
                btnDetection.isChecked = false
                return@setOnCheckedChangeListener
            }
            (currentViewModel as DetectionViewModel).btnDetectionOnClick(isChecked, activity)

        }
    }

    private fun refreshDetectionModeUI() {
        if (DETECTION_MODEL != -1){
            detectionTabLayout.getTabAt(DETECTION_MODEL)?.select()
            (currentViewModel as DetectionViewModel).loadDataToConfusionMatrix()
        }else{
            DETECTION_MODEL = detectionTabLayout.selectedTabPosition
            (currentViewModel as DetectionViewModel).loadDataToConfusionMatrix()
        }
        detectionTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                DETECTION_MODEL = p0?.position!!
                (currentViewModel as DetectionViewModel).loadDataToConfusionMatrix()
            }

        })
    }


    private fun initConfusionMatrix() {

        val viewModel = currentViewModel as DetectionViewModel
        //第0行第0列为空
        val textView = TextView(this.context)
        textView.text = ""
        viewModel.confusionMatrix[0][0] = textView
        //第1-8行且第1-8列为数字
        for (i in 1..8) {
            for (j in 1..8) {
                val textView = TextView(this.context)
                textView.text = "0"
                textView.setTextColor(resources.getColor(android.R.color.black))
                viewModel.confusionMatrix[i][j] = textView
            }
        }
        //第0行且1-8列为文字，第0列且1-8行为文字
        for (i in 1..8){
            //第0行且1-8列
            val textViewRow = TextView(this.context)
            textViewRow.text = viewModel.matrixHeaders[i]
            textViewRow.setTextColor(resources.getColor(R.color.colorAccent))
            viewModel.confusionMatrix[0][i] = textViewRow

            //第0列且1-8行
            val textViewColumn = TextView(this.context)
            textViewColumn.text = viewModel.matrixHeaders[i]
            textViewColumn.setTextColor(resources.getColor(R.color.colorAccent))
            viewModel.confusionMatrix[i][0] = textViewColumn

        }
        //添加到GridLayout
        for (i in 0..8) {
            for (j in 0..8) {
                val mLayoutParams = GridLayout.LayoutParams()
                mLayoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1.0f)
                mLayoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1.0f)
                detectionConfusionMatrix.addView(viewModel.confusionMatrix[i][j], mLayoutParams)
            }
        }
    }
}



