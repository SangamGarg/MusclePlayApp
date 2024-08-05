package com.sangam.muscleplay.stepCounter

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivityStepCounterBinding
import kotlin.math.sqrt

class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    private var magnitudePrevious = 0.0
    private val binding by lazy {
        ActivityStepCounterBinding.inflate(layoutInflater)
    }
    private var sensorManager: SensorManager? = null
    private var running: Boolean = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestForPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(
                    this, "Permission Granted", Toast.LENGTH_SHORT
                ).show()
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACTIVITY_RECOGNITION)) {
                    showRationaleDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        HideStatusBarUtil.hideStatusBar(this)
        @RequiresApi(Build.VERSION_CODES.TIRAMISU) if (!checkPermission()) {
            requestForPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("steps", Context.MODE_PRIVATE)
        val savedNo: Float = sharedPreferences.getFloat("currentSteps", 0f)
        previousTotalSteps = savedNo
    }

    private fun resetSteps() {
        binding.tvSteps.setOnLongClickListener {
            previousTotalSteps = totalSteps
            binding.tvSteps.text = 0.toString()
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("steps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit().apply {
            putFloat("currentSteps", previousTotalSteps)
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    private fun showRationaleDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Notification Permission").setMessage(
            "This app requires sensor permission to keep you updated. " + "If you deny this time you have to manually go to app setting to allow permission."
        ).setPositiveButton("Ok") { _, _ ->
            requestForPermission.launch(android.Manifest.permission.ACTIVITY_RECOGNITION)
        }
        builder.create().show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission(): Boolean {
        val permission = android.Manifest.permission.ACTIVITY_RECOGNITION
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        running = true
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        val detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        when {
            countSensor != null -> {
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI)
            }
            detectorSensor != null -> {
                sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_UI)
            }
            accelerometer != null -> {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
            }
            else -> {
                ToastUtil.makeToast(this, "Your Device Is Not Compatible")
            }
        }
    }
    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            Log.d("STEPSCOUNTER", "FU****")
            return
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val xaccel: Float = event.values[0]
            val yaccel: Float = event.values[0]
            val zaccel: Float = event.values[0]
            val magnitude: Double =
                sqrt((xaccel * xaccel + yaccel * yaccel + zaccel * zaccel).toDouble())
            val magnitudeDelta: Double = magnitude - magnitudePrevious
            magnitudePrevious = magnitude
            if (magnitudeDelta > 6) {
                totalSteps++
            }
            val step: Int = totalSteps.toInt()
            binding.tvSteps.text = step.toString()
        } else {
            if (running) {
                totalSteps = event.values[0]
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                binding.tvSteps.text = currentSteps.toString()
            }
        }

    }

    override fun onAccuracyChanged(event: Sensor?, accuracy: Int) {
        Log.d("STEPSCOUNTER", "Accuracy changed to: $accuracy")    }

}