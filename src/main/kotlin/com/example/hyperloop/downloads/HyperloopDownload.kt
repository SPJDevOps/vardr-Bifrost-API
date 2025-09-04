package com.example.hyperloop.downloads

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
data class HyperloopDownload(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment strategy for the ID
    val id: Int = 0,

    @Column(nullable = false)
    val type: String,

    @Column(nullable = false)
    val dependency: String,

//    @Column(nullable = false)
//    val version: String,

    @Column(nullable = false)
    val status: DownloadStatus = DownloadStatus.STARTED,

    @Column(nullable = false)
    val date: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    val scheduleId: Int? = null, // Reference to DownloadSchedule if this was a scheduled download

    @Column(nullable = false)
    val isScheduled: Boolean = false, // Flag to indicate if this was triggered by a schedule

    @Column(nullable = true)
    val userId: String? = null // User who initiated the download
)
