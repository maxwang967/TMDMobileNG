package com.morningstarwang.tmdmobileng.ui.detection


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import com.morningstarwang.tmdmobileng.R
import kotlinx.android.synthetic.main.fragment_detection.*

class DetectionFragment : Fragment() {

    var confusionMatrix = Array<Array<TextView?>>(9) { arrayOfNulls(9) }
    var matrixHeaders: Array<String> = arrayOf(
        "空",
        "静",
        "行",
        "跑",
        "骑",
        "车",
        "交",
        "火",
        "地"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConfusionMatrix()
    }

    private fun initConfusionMatrix() {

        //第0行第0列为空
        val textView = TextView(this.context)
        textView.text = ""
        confusionMatrix[0][0] = textView
        //第1-8行且第1-8列为数字
        for (i in 1..8) {
            for (j in 1..8) {
                val textView = TextView(this.context)
                textView.text = "0"
                textView.setTextColor(resources.getColor(android.R.color.black))
                confusionMatrix[i][j] = textView
            }
        }
        //第0行且1-8列为文字，第0列且1-8行为文字
        for (i in 1..8){
            //第0行且1-8列
            val textViewRow = TextView(this.context)
            textViewRow.text = matrixHeaders[i]
            textViewRow.setTextColor(resources.getColor(R.color.colorAccent))
            confusionMatrix[0][i] = textViewRow

            //第0列且1-8行
            val textViewColumn = TextView(this.context)
            textViewColumn.text = matrixHeaders[i]
            textViewColumn.setTextColor(resources.getColor(R.color.colorAccent))
            confusionMatrix[i][0] = textViewColumn

        }
        //添加到GridLayout
        for (i in 0..8) {
            for (j in 0..8) {
                val mLayoutParams = GridLayout.LayoutParams()
                mLayoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1.0f)
                mLayoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1.0f)
                detectionConfusionMatrix.addView(confusionMatrix[i][j], mLayoutParams)
            }
        }
    }
}



