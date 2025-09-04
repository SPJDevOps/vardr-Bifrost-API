package com.example.hyperloop.scheduling

data class ScheduleRequest(
    val type: String, // Download type (e.g., "maven", "npm", etc.)
    val dependency: String, // The dependency to download
    val scheduleType: ScheduleType,
    val cronExpression: String? = null, // For CRON type schedules
    val intervalValue: Int? = null, // For CUSTOM_INTERVAL (e.g., every 7 days)
    val intervalUnit: String? = null, // "DAYS", "HOURS", "MINUTES"
    val dayOfWeek: Int? = null, // 1-7 for WEEKLY (1 = Monday)
    val dayOfMonth: Int? = null, // 1-31 for MONTHLY
    val timeOfDay: String? = null, // HH:MM format for daily/weekly/monthly
    val description: String? = null // Optional user description
)

fun ScheduleRequest.toDownloadSchedule(userId: String): DownloadSchedule = DownloadSchedule(
    type = this.type,
    dependency = this.dependency,
    scheduleType = this.scheduleType,
    cronExpression = this.cronExpression,
    intervalValue = this.intervalValue,
    intervalUnit = this.intervalUnit,
    dayOfWeek = this.dayOfWeek,
    dayOfMonth = this.dayOfMonth,
    timeOfDay = this.timeOfDay,
    description = this.description,
    userId = userId
)
