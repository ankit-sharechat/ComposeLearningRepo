package com.example.composelearning

import kotlinx.coroutines.delay

class TimerUtil {
    private var isTimerPaused = false
    private var isTimerCancelled = false
    private var isTimerFinished = false
    var timePeriod = 0L
    var tickInterval = 100L
    private var elapsedTime = 0L
    var onUpdate: (suspend (percent: Float) -> Unit)? = null
    var onFinished: (suspend () -> Unit)? = null


    var alreadyRunning = false
    var restartScheduled = false

    private suspend fun playTimer(reset: Boolean = false) {
        if (alreadyRunning.not()) {
            //reset previous progress
            if (reset) {
                elapsedTime = 0L
            }

            //raise flag for running
            alreadyRunning = true

            //reset active timer flags
            isTimerFinished = false
            isTimerCancelled = false
            isTimerPaused = false

            //start the loop
            while (timerActive()) {
                val percent = (elapsedTime.toFloat() / timePeriod.toFloat()).coerceAtMost(1.0f)
                //update progress
                onUpdate?.let { it(percent) }

                //check for finish
                if (percent >= 1.0f && timerActive()) {
                    isTimerFinished = true
                    onFinished?.let { it() }
                    break
                }
                //add elapsed time
                elapsedTime += tickInterval
                if (timerActive()) {
                    delay(tickInterval)
                }
            }

            //take down the running flag
            alreadyRunning = false

            //check for pending calls
            if (restartScheduled) {
                restartScheduled = false
                playTimer(reset = true)
            }
        }
    }

    private fun timerActive(): Boolean {
        return isTimerPaused.not() && isTimerCancelled.not() && isTimerFinished.not()
    }

    fun pauseTimer() {
        isTimerPaused = true
    }

    suspend fun resumeTimer() {
        if (isTimerPaused) {
            playTimer(reset = false)
        }
    }

    fun stopTimer() {
        isTimerCancelled = true
    }

    suspend fun restartTimer() {
        if (alreadyRunning) {
            restartScheduled = true
        } else {
            playTimer(reset = true)
        }
    }
}