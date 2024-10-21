package com.example.hyperloop.downloads

import com.example.hyperloop.exceptions.DownloadNotFoundException
import com.example.hyperloop.queues.RabbitMQSender
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class DownloadService(private val downloadRepository: DownloadRepository, private val rabbitMQSender: RabbitMQSender) {

    fun getAllDownloads(): List<HyperloopDownload> = this.downloadRepository.findAllByOrderByDateDesc()

    fun saveDownloadRequest(downloadRequest: FrontendDownloadRequest) {
        val download = this.downloadRepository.save(downloadRequest.toHyperloopDownload())
        this.rabbitMQSender.sendDownloadRequest(download)
    }

    fun updateStatusOfDownload(statusUpdate: HyperloopDownload) {
        downloadRepository.findById(statusUpdate.id).getOrElse { throw DownloadNotFoundException(statusUpdate.id) }
            .let { databaseEntry ->
                downloadRepository.save(databaseEntry.copy(status = statusUpdate.status))
            }
    }
}

fun FrontendDownloadRequest.toHyperloopDownload(): HyperloopDownload = HyperloopDownload(
    dependency = this.dependency,
    type = this.type,
)