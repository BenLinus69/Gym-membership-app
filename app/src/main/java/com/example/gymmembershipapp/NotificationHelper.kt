import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.gymmembershipapp.R

class NotificationHelper(private val context: Context) {
    private val channelId = "workout_milestones_channel"
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Workout Milestones",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showMilestoneNotification(milestone: Int) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.dumbbell)
            .setContentTitle("Felicitări! 🎉")
            .setContentText("Ai atins $milestone workout-uri!")
            .setAutoCancel(true)
            .build()

        notificationManager.notify(milestone, notification)
    }
}