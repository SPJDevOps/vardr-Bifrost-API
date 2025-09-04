package com.example.hyperloop.scheduling

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/schedules")
class ScheduleController(private val scheduleService: ScheduleService) {

    @GetMapping
    fun getUserSchedules(pageable: Pageable): Page<DownloadSchedule> = 
        scheduleService.getUserSchedules(pageable)

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can see all schedules
    fun getAllSchedules(pageable: Pageable): Page<DownloadSchedule> = 
        scheduleService.getAllSchedules(pageable)

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can see all active schedules
    fun getActiveSchedules(): List<DownloadSchedule> = 
        scheduleService.getActiveSchedules()

    @GetMapping("/{id}")
    fun getSchedule(@PathVariable id: Int): DownloadSchedule? = 
        scheduleService.getScheduleById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSchedule(@RequestBody scheduleRequest: ScheduleRequest): DownloadSchedule =
        scheduleService.createSchedule(scheduleRequest)

    @PutMapping("/{id}/toggle")
    fun toggleScheduleStatus(@PathVariable id: Int): DownloadSchedule =
        scheduleService.toggleScheduleStatus(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSchedule(@PathVariable id: Int) =
        scheduleService.deleteSchedule(id)

    @GetMapping("/types")
    fun getScheduleTypes(): List<ScheduleTypeInfo> = 
        ScheduleType.values().map { 
            ScheduleTypeInfo(
                type = it.name,
                displayName = it.displayName,
                description = it.description
            )
        }
}

data class ScheduleTypeInfo(
    val type: String,
    val displayName: String,
    val description: String
)
