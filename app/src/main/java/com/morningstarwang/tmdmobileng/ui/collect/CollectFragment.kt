package com.morningstarwang.tmdmobileng.ui.collect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.morningstarwang.tmdmobileng.App
import com.morningstarwang.tmdmobileng.R
import com.morningstarwang.tmdmobileng.REAL_MODE
import com.morningstarwang.tmdmobileng.WINDOW_SIZE
import com.morningstarwang.tmdmobileng.bean.SensorData
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
            if (REAL_MODE == -1) {
                toast(getString(R.string.alert_select_mode_first))
                btnCollect.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (App.isPredicting) {
                toast(getString(R.string.alert_stop_predict_first))
                btnCollect.isChecked = true
                return@setOnCheckedChangeListener
            }
            (currentViewModel as CollectViewModel).btnCollectOnClick(isChecked, activity)
        }
        initCharts()
        (currentViewModel as CollectViewModel).acc.observe(this, Observer<SensorData> {
            val data = chartAcc.data
            val setX = data.getDataSetByIndex(0)
            val setY = data.getDataSetByIndex(1)
            val setZ = data.getDataSetByIndex(2)
            setX.addEntry(Entry(setX.entryCount.toFloat(), it.x))
            setY.addEntry(Entry(setY.entryCount.toFloat(), it.y))
            setZ.addEntry(Entry(setZ.entryCount.toFloat(), it.z))
            data.notifyDataChanged()
            chartAcc.notifyDataSetChanged()
            chartAcc.setVisibleXRangeMaximum(WINDOW_SIZE / 2.toFloat())
            chartAcc.moveViewToX(data.entryCount.toFloat())
            chartAcc.invalidate()
        })

        (currentViewModel as CollectViewModel).lacc.observe(this, Observer<SensorData> {
            val data = chartLAcc.data
            val setX = data.getDataSetByIndex(0)
            val setY = data.getDataSetByIndex(1)
            val setZ = data.getDataSetByIndex(2)
            setX.addEntry(Entry(setX.entryCount.toFloat(), it.x))
            setY.addEntry(Entry(setY.entryCount.toFloat(), it.y))
            setZ.addEntry(Entry(setZ.entryCount.toFloat(), it.z))
            data.notifyDataChanged()
            chartLAcc.notifyDataSetChanged()
            chartLAcc.setVisibleXRangeMaximum(WINDOW_SIZE / 2.toFloat())
            chartLAcc.moveViewToX(data.entryCount.toFloat())
            chartLAcc.invalidate()
        }
        )

        (currentViewModel as CollectViewModel).gyr.observe(this, Observer<SensorData> {
            val data = chartGyr.data
            val setX = data.getDataSetByIndex(0)
            val setY = data.getDataSetByIndex(1)
            val setZ = data.getDataSetByIndex(2)
            setX.addEntry(Entry(setX.entryCount.toFloat(), it.x))
            setY.addEntry(Entry(setY.entryCount.toFloat(), it.y))
            setZ.addEntry(Entry(setZ.entryCount.toFloat(), it.z))
            data.notifyDataChanged()
            chartGyr.notifyDataSetChanged()
            chartGyr.setVisibleXRangeMaximum(WINDOW_SIZE / 2.toFloat())
            chartGyr.moveViewToX(data.entryCount.toFloat())
            chartGyr.invalidate()
        })

        (currentViewModel as CollectViewModel).mag.observe(this, Observer<SensorData> {
            val data = chartMag.data
            val setX = data.getDataSetByIndex(0)
            val setY = data.getDataSetByIndex(1)
            val setZ = data.getDataSetByIndex(2)
            setX.addEntry(Entry(setX.entryCount.toFloat(), it.x))
            setY.addEntry(Entry(setY.entryCount.toFloat(), it.y))
            setZ.addEntry(Entry(setZ.entryCount.toFloat(), it.z))
            data.notifyDataChanged()
            chartMag.notifyDataSetChanged()
            chartMag.setVisibleXRangeMaximum(WINDOW_SIZE / 2.toFloat())
            chartMag.moveViewToX(data.entryCount.toFloat())
            chartMag.invalidate()

        })

        (currentViewModel as CollectViewModel).pressure.observe(this, Observer<Float> {
            val data = chartPressure.data
            val set = data.getDataSetByIndex(0)
            set.addEntry(Entry(set.entryCount.toFloat(), it))
            data.notifyDataChanged()
            chartPressure.notifyDataSetChanged()
            chartPressure.setVisibleXRangeMaximum(WINDOW_SIZE / 2.toFloat())
            chartPressure.moveViewToX(data.entryCount.toFloat())
            chartPressure.invalidate()
        })
    }

    private fun initCharts() {
        val charts = arrayListOf(
            chartAcc,
            chartLAcc,
            chartGyr,
            chartMag,
            chartPressure
        )
        charts.forEachIndexed { index, chart ->
            chart.description.isEnabled = false
            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.setScaleEnabled(true)
            chart.setDrawGridBackground(true)
            chart.setPinchZoom(true)
            chart.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.background_light))
            val dataSets = ArrayList<ILineDataSet>()
            val colors = arrayListOf(
                ColorTemplate.VORDIPLOM_COLORS[0],
                ColorTemplate.VORDIPLOM_COLORS[1],
                ColorTemplate.VORDIPLOM_COLORS[2]
            )
            val numOfSet = if (index != 4) {
                3
            } else {
                1
            }
            for (i in 0 until numOfSet) {
                val values = ArrayList<Entry>()
                values.add(Entry(0f, 0f))
                val label = if (index != 4) {
                    when (i) {
                        0 -> "x"
                        1 -> "y"
                        2 -> "z"
                        else -> {
                            ""
                        }
                    }
                } else {
                    "value"
                }
                val lineDataSet = LineDataSet(values, label)
                lineDataSet.lineWidth = 0.5f
                lineDataSet.circleRadius = 0.5f
                lineDataSet.color = colors[i]
                lineDataSet.setDrawCircles(false)
                lineDataSet.setDrawValues(false)
                lineDataSet.cubicIntensity = 0.05f
                dataSets.add(lineDataSet)
            }
            val data = LineData(dataSets)
            chart.data = data
            chart.invalidate()
        }

    }


}
