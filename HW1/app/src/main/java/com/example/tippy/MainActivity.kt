package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SeekBar
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        etBase.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription: String
        when(tipPercent){
            in 0..9 -> tipDescription = "😢"
            in 10..20 -> tipDescription = "😐"
            else -> tipDescription = "🤩"
        }
        tvTipRating.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        ) as Int
        tvTipRating.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        if( etBase.text.toString().isEmpty()){
            tvTipAmt.text = ""
            tvTotalAmt.text = ""
            return

        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress.toDouble()
        val tipAmount = baseAmount * (tipPercent/100)
        val totalAmount = baseAmount + tipAmount
        tvTipAmt.text = "%.2f".format(tipAmount).toString()
        tvTotalAmt.text = "%.2f".format(totalAmount).toString()

    }

}