package com.example.hyperloop.downloads

import com.example.hyperloop.exceptions.DownloadNotFoundException
import com.example.hyperloop.queues.RabbitMQSender
import com.example.hyperloop.security.UserContextService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class DownloadService(
    private val downloadRepository: DownloadRepository, 
    private val rabbitMQSender: RabbitMQSender,
    private val userContextService: UserContextService
) {

    fun getAllDownloads(pageable: Pageable): Page<HyperloopDownload> = this.downloadRepository.findAllByOrderByDateDesc(pageable)

    fun saveDownloadRequest(downloadRequest: FrontendDownloadRequest) {
        val userId = userContextService.getCurrentUserId()
        val download = this.downloadRepository.save(downloadRequest.toHyperloopDownload(userId))
        this.rabbitMQSender.sendDownloadRequest(download)
    }

    fun saveScheduledDownloadRequest(downloadRequest: FrontendDownloadRequest, scheduleId: Int) {
        val download = this.downloadRepository.save(downloadRequest.toScheduledHyperloopDownload(scheduleId))
        this.rabbitMQSender.sendDownloadRequest(download)
    }

    fun updateStatusOfDownload(statusUpdate: HyperloopDownload) {
        downloadRepository.findById(statusUpdate.id).getOrElse { throw DownloadNotFoundException(statusUpdate.id) }
            .let { databaseEntry ->
                downloadRepository.save(databaseEntry.copy(status = statusUpdate.status))
            }
    }
}

fun FrontendDownloadRequest.toHyperloopDownload(userId: String?): HyperloopDownload = HyperloopDownload(
    dependency = this.dependency,
    type = this.type,
    userId = userId
)

fun FrontendDownloadRequest.toScheduledHyperloopDownload(scheduleId: Int): HyperloopDownload = HyperloopDownload(
    dependency = this.dependency,
    type = this.type,
    scheduleId = scheduleId,
    isScheduled = true
)