package com.example.pomodoro

import android.os.CountDownTimer
class CountdownTimerHelper (
    private val totalTimeInMillis: Long,
    private val interval: Long,
    private val onTick: (Long) -> Unit,
    private val onFinish: () -> Unit
) : CountDownTimer(totalTimeInMillis, interval) {

    override fun onTick(millisUntilFinished: Long) {
        onTick.invoke(millisUntilFinished)
    }

    override fun onFinish() {
        onFinish.invoke()
    }
}