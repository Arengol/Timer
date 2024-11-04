package ru.vstu.vladimir.lr1

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ru.vstu.vladimir.lr1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var timer: CountDownTimer
    private var hour = 0L
    private var minute = 0L
    private var second = 0L
    private var isPauseState = false
    private var isTimerState = false
        set(value) { binding.apply {
                hourNP.isVisible = value.not()
                minuteNP.isVisible = value.not()
                secondNP.isVisible = value.not()
                hourTV.isVisible = value.not()
                minuteTV.isVisible = value.not()
                secondTV.isVisible = value.not()
                startBT.isVisible = value.not()
                currentTimePB.isVisible = value
                currentTimeTV.isVisible = value
                cancelBT.isVisible = value
                pauseBT.isVisible = value
            }
            field = value
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            hourNP.minValue = 0
            hourNP.maxValue = 99
            minuteNP.minValue = 0
            minuteNP.maxValue = 59
            secondNP.minValue = 0
            secondNP.maxValue = 59

            startBT.setOnClickListener {
                hour = hourNP.value.toLong()
                minute = minuteNP.value.toLong()
                second = secondNP.value.toLong()
                isTimerState = true
                binding.currentTimePB.max = (hour * 3600 + minute * 60 + second).toInt()
                timer = createTimer()
                timer.start()
            }

            pauseBT.setOnClickListener {
                if (isPauseState) {
                    timer = createTimer()
                    timer.start()
                    binding.pauseBT.text = getString(R.string.timer_pause)
                }
                else {
                    timer.cancel()
                    binding.pauseBT.text = getString(R.string.timer_continue)
                }
                isPauseState = isPauseState.not()
            }

            cancelBT.setOnClickListener {
                isTimerState = false
                isPauseState = false
                timer.cancel()
            }
        }
    }

    private fun createTimer () = object : CountDownTimer((hour * 3600 + minute * 60 + second) * 1000L, 1000L) {
        override fun onFinish() {
            Toast.makeText(applicationContext, getString(R.string.timer_expired), Toast.LENGTH_LONG).show()
        }

        override fun onTick(millisUntilFinished: Long) {
            val normalizedTime = millisUntilFinished / 1000
            hour = normalizedTime / 3600
            minute = (normalizedTime % 3600) / 60
            second = (normalizedTime % 3600) % 60
            Log.d("TIMER", "${hour}:${minute}:${second}")
            binding.apply {
                currentTimeTV.text = "${hour}:${minute}:${second}"
                currentTimePB.progress = (hour * 3600 + minute * 60 + second).toInt()
            }
        }
    }
}