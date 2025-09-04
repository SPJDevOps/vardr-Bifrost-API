package com.example.hyperloop.scheduling

import com.example.hyperloop.downloads.DownloadService
import com.example.hyperloop.downloads.FrontendDownloadRequest
import com.example.hyperloop.security.UserContextService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@Service
class ScheduleService(
    private val downloadScheduleRepository: DownloadScheduleRepository,
    private val downloadService: DownloadService,
    private val userContextService: UserContextService
) {

    fun createSchedule(scheduleRequest: ScheduleRequest): DownloadSchedule {
        val userId = userContextService.getCurrentUserId() 
            ?: throw IllegalStateException("User must be authenticated to create schedules")
        
        val schedule = scheduleRequest.toDownloadSchedule(userId)
        val scheduleWithNextExecution = schedule.copy(
            nextExecution = calculateNextExecution(schedule)
        )
        return downloadScheduleRepository.save(scheduleWithNextExecution)
    }

    fun getUserSchedules(pageable: Pageable): Page<DownloadSchedule> {
        val userId = userContextService.getCurrentUserId() 
            ?: throw IllegalStateException("User must be authenticated")
        return downloadScheduleRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
    }

    fun getAllSchedules(pageable: Pageable): Page<DownloadSchedule> =
        downloadScheduleRepository.findAllByOrderByCreatedAtDesc(pageable)

    fun getActiveSchedules(): List<DownloadSchedule> =
        downloadScheduleRepository.findAllByIsActiveTrueOrderByNextExecutionAsc()

    fun toggleScheduleStatus(scheduleId: Int): DownloadSchedule {
        val userId = userContextService.getCurrentUserId() 
            ?: throw IllegalStateException("User must be authenticated")
        
        val schedule = downloadScheduleRepository.findByIdAndUserId(scheduleId, userId)
            ?: throw IllegalArgumentException("Schedule not found or access denied: $scheduleId")
        
        val updatedSchedule = schedule.copy(
            isActive = !schedule.isActive,
            nextExecution = if (!schedule.isActive) calculateNextExecution(schedule) else null
        )
        return downloadScheduleRepository.save(updatedSchedule)
    }

    fun deleteSchedule(scheduleId: Int) {
        val userId = userContextService.getCurrentUserId() 
            ?: throw IllegalStateException("User must be authenticated")
        
        val schedule = downloadScheduleRepository.findByIdAndUserId(scheduleId, userId)
            ?: throw IllegalArgumentException("Schedule not found or access denied: $scheduleId")
        
        downloadScheduleRepository.delete(schedule)
    }

    @Scheduled(fixedRate = 60000) // Check every minute
    fun processScheduledDownloads() {
        val currentTime = LocalDateTime.now()
        val readySchedules = downloadScheduleRepository.findSchedulesReadyForExecution(currentTime)
        
        readySchedules.forEach { schedule ->
            try {
                // Create and execute the download
                val downloadRequest = FrontendDownloadRequest(
                    type = schedule.type,
                    dependency = schedule.dependency
                )
                
                // Execute the download (this will create a HyperloopDownload with scheduleId)
                downloadService.saveScheduledDownloadRequest(downloadRequest, schedule.id)
                
                // Update the schedule with last execution and next execution
                val updatedSchedule = schedule.copy(
                    lastExecuted = currentTime,
                    nextExecution = calculateNextExecution(schedule)
                )
                downloadScheduleRepository.save(updatedSchedule)
                
                println("Executed scheduled download: ${schedule.dependency} (${schedule.type}) for user ${schedule.userId}")
                
            } catch (e: Exception) {
                println("Failed to execute scheduled download: ${schedule.id} - ${e.message}")
            }
        }
    }

    fun calculateNextExecution(schedule: DownloadSchedule): LocalDateTime? {
        val now = LocalDateTime.now()
        
        return when (schedule.scheduleType) {
            ScheduleType.ONCE -> null // One-time execution, no next execution
            
            ScheduleType.DAILY -> {
                val time = parseTimeOfDay(schedule.timeOfDay) ?: LocalTime.of(0, 0)
                val nextDate = if (now.toLocalTime().isAfter(time)) {
                    now.toLocalDate().plusDays(1)
                } else {
                    now.toLocalDate()
                }
                LocalDateTime.of(nextDate, time)
            }
            
            ScheduleType.WEEKLY -> {
                val time = parseTimeOfDay(schedule.timeOfDay) ?: LocalTime.of(0, 0)
                val targetDayOfWeek = DayOfWeek.of(schedule.dayOfWeek ?: 1)
                var nextDate = now.toLocalDate()
                
                // Find next occurrence of the target day
                while (nextDate.dayOfWeek != targetDayOfWeek || 
                       (nextDate == now.toLocalDate() && now.toLocalTime().isAfter(time))) {
                    nextDate = nextDate.plusDays(1)
                }
                LocalDateTime.of(nextDate, time)
            }
            
            ScheduleType.MONTHLY -> {
                val time = parseTimeOfDay(schedule.timeOfDay) ?: LocalTime.of(0, 0)
                val targetDay = schedule.dayOfMonth ?: 1
                var nextMonth = YearMonth.from(now)
                
                // Check if we can schedule for this month
                val thisMonthDate = try {
                    nextMonth.atDay(targetDay)
                } catch (e: Exception) {
                    // Day doesn't exist in this month, move to next month
                    nextMonth = nextMonth.plusMonths(1)
                    nextMonth.atDay(minOf(targetDay, nextMonth.lengthOfMonth()))
                }
                
                val nextDateTime = LocalDateTime.of(thisMonthDate, time)
                if (nextDateTime.isAfter(now)) {
                    nextDateTime
                } else {
                    // Move to next month
                    nextMonth = nextMonth.plusMonths(1)
                    val nextMonthDay = minOf(targetDay, nextMonth.lengthOfMonth())
                    LocalDateTime.of(nextMonth.atDay(nextMonthDay), time)
                }
            }
            
            ScheduleType.MONTHLY_FIRST -> {
                val time = parseTimeOfDay(schedule.timeOfDay) ?: LocalTime.of(0, 0)
                var nextMonth = YearMonth.from(now)
                val firstOfMonth = LocalDateTime.of(nextMonth.atDay(1), time)
                
                if (firstOfMonth.isAfter(now)) {
                    firstOfMonth
                } else {
                    nextMonth = nextMonth.plusMonths(1)
                    LocalDateTime.of(nextMonth.atDay(1), time)
                }
            }
            
            ScheduleType.MONTHLY_LAST -> {
                val time = parseTimeOfDay(schedule.timeOfDay) ?: LocalTime.of(0, 0)
                var nextMonth = YearMonth.from(now)
                val lastOfMonth = LocalDateTime.of(nextMonth.atEndOfMonth(), time)
                
                if (lastOfMonth.isAfter(now)) {
                    lastOfMonth
                } else {
                    nextMonth = nextMonth.plusMonths(1)
                    LocalDateTime.of(nextMonth.atEndOfMonth(), time)
                }
            }
            
            ScheduleType.CUSTOM_INTERVAL -> {
                val intervalValue = schedule.intervalValue ?: 1
                val intervalUnit = schedule.intervalUnit ?: "DAYS"
                
                when (intervalUnit.uppercase()) {
                    "MINUTES" -> now.plusMinutes(intervalValue.toLong())
                    "HOURS" -> now.plusHours(intervalValue.toLong())
                    "DAYS" -> now.plusDays(intervalValue.toLong())
                    else -> now.plusDays(intervalValue.toLong())
                }
            }
            
            ScheduleType.CRON -> {
                // For cron expressions, you'd need a cron parser library
                // For now, return null and handle manually or add spring-boot-starter-quartz
                null
            }
        }
    }

    private fun parseTimeOfDay(timeString: String?): LocalTime? {
        return try {
            timeString?.let { LocalTime.parse(it) }
        } catch (e: Exception) {
            null
        }
    }

    fun getScheduleById(id: Int): DownloadSchedule? {
        val userId = userContextService.getCurrentUserId() ?: return null
        return downloadScheduleRepository.findByIdAndUserId(id, userId)
    }
}
