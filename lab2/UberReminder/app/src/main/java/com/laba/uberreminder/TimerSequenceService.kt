package com.laba.uberreminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val CHANNEL_ID = "FOREGROUND_SERVICE_CHANNEL"
const val ACTION_KEY = "action"
const val STAGE_ENDED_CHANNEL_ID = "STAGE_ENDED"
const val NOTIFICATION_ID = 13

class TimerSequenceService : Service() {

    private var sequence: TimerSequence? = null
    private var currentPosition = -1
    private var timers: List<ActiveTimer>? = null
    private var currentCountDownTimer:CountDownTimer? = null

    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground service channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = ""
        }
        val stageEndedNotificationChannel = NotificationChannel(
            STAGE_ENDED_CHANNEL_ID,
            "Timer popups",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = ""
        }
        val notificationManager: NotificationManager =
            baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.createNotificationChannel(stageEndedNotificationChannel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(baseContext.getString(R.string.app_name))
            .setContentText(baseContext.getString(R.string.timer_is_working))
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .build()
        startForeground(1, notification)
        when (intent?.getSerializableExtra(ACTION_KEY) as StartServiceAction) {
            StartServiceAction.START -> {
                sequence = intent.getSerializableExtra(SEQUENCE_KEY) as TimerSequence?
                timers = generateTimersArray()
                currentPosition = -1
                updateCurrentPosition()
            }
            StartServiceAction.PREVIOUS -> {
                currentCountDownTimer?.cancel()
                currentPosition -= 2
                if(currentPosition < 0) currentPosition = -1
                updateCurrentPosition(true)
            }
            StartServiceAction.NEXT -> {
                currentCountDownTimer?.cancel()
                updateCurrentPosition(true)
            }
            StartServiceAction.RESUME -> {
                currentCountDownTimer?.start()
            }
            StartServiceAction.PAUSE -> {
                currentCountDownTimer?.cancel()
            }
        }
        return START_NOT_STICKY
    }

    private fun updateCurrentPosition(nextOrPrevious:Boolean = false) {
        currentPosition++
        if (currentPosition < timers?.size ?: 0)
        {
            currentCountDownTimer = object : CountDownTimer(timers?.get(currentPosition)?.lengthInMillis ?: 0, 1)
            {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    if (currentPosition >= 0 && currentPosition < timers?.size ?: 0 && !nextOrPrevious) {
                        val notification = NotificationCompat.Builder(baseContext, STAGE_ENDED_CHANNEL_ID)
                            .setContentTitle("Timer")
                            .setContentText(timers?.get(currentPosition)?.name + "stage has ended")
                            .setSmallIcon(R.drawable.ic_baseline_timer_24)
                            .build()
                        NotificationManagerCompat.from(baseContext).notify(NOTIFICATION_ID, notification)
                    }
                    updateCurrentPosition()
                }
            }.start()
        }
    }

    private fun generateTimersArray(): List<ActiveTimer> {
        val timers = mutableListOf<ActiveTimer>()
        if (sequence != null) {
            val sequenceTimers = sequence!!.timers
            for (timer in sequenceTimers) {
                for (r in 0..timer.repeatTimes) {
                    timers.add(ActiveTimer(timer.name, timer.length.toMillis()))
                }
            }
        }
        return timers
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}
