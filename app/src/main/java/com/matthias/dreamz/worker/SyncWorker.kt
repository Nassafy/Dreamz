package com.matthias.dreamz.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.matthias.dreamz.repository.DreamRepository
import com.matthias.dreamz.repository.TagInfoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dreamRepository: DreamRepository,
    private val tagInfoRepository: TagInfoRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        try {
            dreamRepository.syncDream()
            dreamRepository.syncTags()
            tagInfoRepository.reindexTagInfo()
        } catch (error: Exception) {
            return Result.failure()
        }
        return Result.success()
    }

    companion object {
        const val NAME_PERIODIC = "SYNC_WORKER_PERIODIC"
        const val NAME_UNIQUE = "SYNC_WORKER_UNIQUE"
        const val TAG = "SYNC_TAG"

        private fun workRequest(): PeriodicWorkRequest {
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            return PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS).setConstraints(
                constraints
            )
                .build()
        }

        fun launch(workManager: WorkManager): Operation {
            return workManager.enqueueUniquePeriodicWork(
                NAME_PERIODIC,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest()
            )
        }
    }
}