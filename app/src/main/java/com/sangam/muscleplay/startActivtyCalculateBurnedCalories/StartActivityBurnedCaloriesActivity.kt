package com.sangam.muscleplay.startActivtyCalculateBurnedCalories

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sangam.muscleplay.AppUtils.AppArrays
import com.sangam.muscleplay.AppUtils.AppConvertUnitsUtil
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.Calculators.bmicalculator.BmiViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivityStartActivtyBurnedCaloriesBinding
import kotlin.math.roundToInt

class StartActivityBurnedCaloriesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityStartActivtyBurnedCaloriesBinding.inflate(layoutInflater)
    }
    private var numberPickerArrayWeight = emptyArray<String>()
    lateinit var selectedItemWeight: String
    lateinit var bmiViewModel: BmiViewModel

    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bmiViewModel = ViewModelProvider(this)[BmiViewModel::class.java]
        HideStatusBarUtil.hideStatusBar(this@StartActivityBurnedCaloriesActivity)

        intiListener()

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        observeWeightUnit()
    }


    private fun intiListener() {

        binding.calculateButton.setOnClickListener {
            val weight = AppConvertUnitsUtil.convertUnitWeight(
                selectedItemWeight, numberPickerArrayWeight, binding.numberPickerWeight
            )
        }


        binding.buttonStart.setOnClickListener {
            startStopTimer()
        }
        binding.buttonReset.setOnClickListener {
            resetTimer()
        }
        binding.tvStart.setOnClickListener {
            startStopTimer()
        }
        binding.tvReset.setOnClickListener {
            resetTimer()
        }
        binding.backButton.setOnClickListener {
            stopTimer()
            finish()
        }
        binding.historyButton.setOnClickListener {
            IntentUtil.startIntent(this, StartActivityBurnedCaloriesHistoryActivity())
        }
        spinnerWeight()
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIMER_EXTRA, 0.0)
            binding.tvTime.text = getTimeStringFromDouble(time)
        }
    }


    private fun getTimeStringFromDouble(time: Double): String {

        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        Log.d("TIMERRUNS", "hours $hours minutes $minutes seconds $seconds")

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String =
        String.format("%02d:%02d:%02d", hours, minutes, seconds)


    private fun startStopTimer() {
        if (timerStarted) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIMER_EXTRA, time)
        startService(serviceIntent)
        binding.tvStart.text = "Stop Timer"
        timerStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.tvStart.text = "Start Timer"
        timerStarted = false
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.tvTime.text = getTimeStringFromDouble(time)

    }

    private fun observeWeightUnit() {
        bmiViewModel.measureWeight.observe(this, Observer {
            binding.tvUnitsWeight.text = it
        })
    }

    private fun spinnerWeight() {
        ArrayAdapter.createFromResource(
            this, R.array.spinner_weight_measurements, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerWeight.adapter = adapter
        }
        binding.spinnerWeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItemWeight = parent?.getItemAtPosition(position).toString()
                when (selectedItemWeight) {
                    "kilograms (kg)" -> {
                        bmiViewModel.changeToKilograms()
                        binding.numberPickerWeight.minValue = 0
                        binding.numberPickerWeight.maxValue = AppArrays.numbersArrayKg.size - 1
                        binding.numberPickerWeight.displayedValues = AppArrays.numbersArrayKg
                        numberPickerArrayWeight = AppArrays.numbersArrayKg

                    }

                    "pound (lbs)" -> {
                        bmiViewModel.changeToPounds()
                        binding.numberPickerWeight.minValue = 0
                        binding.numberPickerWeight.maxValue = AppArrays.numbersArrayPounds.size - 1
                        binding.numberPickerWeight.displayedValues = AppArrays.numbersArrayPounds
                        numberPickerArrayWeight = AppArrays.numbersArrayPounds

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    this@StartActivityBurnedCaloriesActivity, "Nothing Selected", Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopTimer()
        finish()
    }
}