package com.example.hyperloop.scheduling

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class DownloadSchedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val type: String, // Download type (e.g., "maven", "npm", etc.)

    @Column(nullable = false)
    val dependency: String, // The dependency to download

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val scheduleType: ScheduleType,

    @Column(nullable = true)
    val cronExpression: String? = null, // For CRON type schedules

    @Column(nullable = true)
    val intervalValue: Int? = null, // For CUSTOM_INTERVAL (e.g., every 7 days)

    @Column(nullable = true)
    val intervalUnit: String? = null, // "DAYS", "HOURS", "MINUTES"

    @Column(nullable = true)
    val dayOfWeek: Int? = null, // 1-7 for WEEKLY (1 = Monday)

    @Column(nullable = true)
    val dayOfMonth: Int? = null, // 1-31 for MONTHLY

    @Column(nullable = true)
    val timeOfDay: String? = null, // HH:MM format for daily/weekly/monthly

    @Column(nullable = false)
    val isActive: Boolean = true,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    val lastExecuted: LocalDateTime? = null,

    @Column(nullable = true)
    val nextExecution: LocalDateTime? = null,

    @Column(nullable = true)
    val description: String? = null, // Optional user description

    @Column(nullable = false)
    val userId: String, // User who created the schedule
)
