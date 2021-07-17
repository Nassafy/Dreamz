package com.matthias.dreamz.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.matthias.dreamz.repository.DreamRepository
import com.matthias.dreamz.repository.TagInfoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TagSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val tagInfoRepository: TagInfoRepository,
    private val dreamRepository: DreamRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        tagInfoRepository.reindexTagInfo()
        dreamRepository.syncTags()
        return Result.success()
    }

    companion object {
        val NAME = "TAG_SYNC_WORKER"

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
}