package ai.ftech.ekyc.common.widget.overlay

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

// const value
internal const val ONE_THOUSAND_MILLISECONDS = 1000L
internal const val FIVE_THOUSAND_MILLISECONDS = 5000L
internal const val CORE_POOL_SIZE_DEFAULT = 7
internal const val MAX_POOL_SIZE_DEFAULT = 10
internal const val KEEP_ALIVE_TIME_BY_SECONDS_DEFAULT = 10L
internal const val CAPACITY_DEFAULT = 15
internal const val TIME_COUNT_DOWN_SKIP_DEFAULT = 5L

class HandleViewPool(
    mediaCorePoolSize: Int = CORE_POOL_SIZE_DEFAULT,
    mediaMaxPoolSize: Int = MAX_POOL_SIZE_DEFAULT,
    mediaKeepAliveTime: Long = KEEP_ALIVE_TIME_BY_SECONDS_DEFAULT,
    mediaCapacity: Int = CAPACITY_DEFAULT
) : ThreadPoolExecutor(
    mediaCorePoolSize,
    mediaMaxPoolSize,
    mediaKeepAliveTime,
    TimeUnit.SECONDS,
    ArrayBlockingQueue(mediaCapacity)
)

class HandleViewTask(val name: String, val task: () -> Bitmap, val callback: ICallback) : Runnable {
    companion object {
        private val TAG = HandleViewTask::class.java.simpleName
    }

    private val timeBetweenCallThreadAgain = ONE_THOUSAND_MILLISECONDS / 60

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            callback.onFinish(task())
        }
    }

    override fun run() {
        try {
            handler.sendEmptyMessage(0)
        } catch (e: InterruptedException) {
            Log.e(TAG, "InterruptedException: ${e.message}")
            e.printStackTrace()
            callback.onError(e)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            e.printStackTrace()
            callback.onError(e)
        }
    }

    interface ICallback {
        fun onFinish(bitmap: Bitmap)
        fun onError(exception: Exception)
    }
}
