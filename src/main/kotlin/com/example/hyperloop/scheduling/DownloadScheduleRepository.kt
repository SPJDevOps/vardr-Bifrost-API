package com.example.hyperloop.scheduling

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DownloadScheduleRepository : JpaRepository<DownloadSchedule, Int> {

    fun findAllByIsActiveTrue(): List<DownloadSchedule>
    
    fun findAllByIsActiveTrueOrderByNextExecutionAsc(): List<DownloadSchedule>
    
    fun findAllByUserIdOrderByCreatedAtDesc(userId: String, pageable: Pageable): Page<DownloadSchedule>
    
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<DownloadSchedule>
    
    @Query("SELECT s FROM DownloadSchedule s WHERE s.isActive = true AND s.nextExecution <= :currentTime")
    fun findSchedulesReadyForExecution(currentTime: LocalDateTime): List<DownloadSchedule>
    
    @Query("SELECT s FROM DownloadSchedule s WHERE s.isActive = true AND s.nextExecution IS NULL")
    fun findSchedulesWithoutNextExecution(): List<DownloadSchedule>
    
    fun findByIdAndUserId(id: Int, userId: String): DownloadSchedule?
}
