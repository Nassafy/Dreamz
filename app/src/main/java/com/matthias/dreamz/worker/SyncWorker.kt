package com.matthias.dreamz.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.matthias.dreamz.datastore.FlagDataStoreManager
import com.matthias.dreamz.repository.AuthRepository
import com.matthias.dreamz.repository.DreamRepository
import com.matthias.dreamz.repository.TagInfoRepository
import com.matthias.dreamz.widget.DreamzWidgetReceiver.Companion.updateWidgets
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
        @Assisted private val context: Context,
        @Assisted workerParameters: WorkerParameters,
        private val dreamRepository: DreamRepository,
        private val tagInfoRepository: TagInfoRepository,
        private val authRepository: AuthRepository,
        private val flagDataStoreManager: FlagDataStoreManager
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        try {
            val logged = authRepository.logged.first()
            if (logged) {
                dreamRepository.syncDream()
            }
            dreamRepository.syncTags()
            tagInfoRepository.reindexTagInfo()
            flagDataStoreManager.setSyncState(true)
        } catch (error: Exception) {
            Log.d("Dreamz sync", error.message ?: "Error during Sync")
            flagDataStoreManager.setSyncState(false)
            return Result.failure()
        }
        context.updateWidgets()
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