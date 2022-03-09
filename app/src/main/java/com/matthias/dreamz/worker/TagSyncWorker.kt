package com.matthias.dreamz.worker

import android.app.Notification
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.matthias.dreamz.R
import com.matthias.dreamz.repository.DreamRepository
import com.matthias.dreamz.repository.TagInfoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TagSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val tagInfoRepository: TagInfoRepository,
    private val dreamRepository: DreamRepository
) : CoroutineWorker(context, workerParameters) {
    private val CHANNEL_ID = "dreamz__tag_sync_notif"

    override suspend fun doWork(): Result {
        tagInfoRepository.reindexTagInfo()
        dreamRepository.syncTags()
        return Result.success()
    }

    companion object {
        private const val NAME = "TAG_SYNC_WORKER"

        private fun workRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<TagSyncWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        }

        fun launch(workManager: WorkManager) {
            workManager.enqueueUniqueWork(
                NAME,
                ExistingWorkPolicy.KEEP,
                workRequest()
            )
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notificationId = 0
        val notification =
            Notification.Builder(context, CHANNEL_ID).setContentText(context.getText(R.string.sync))
                .setSmallIcon(R.drawable.ic_launcher_foreground).build()
        return ForegroundInfo(notificationId, notification)
    }
}