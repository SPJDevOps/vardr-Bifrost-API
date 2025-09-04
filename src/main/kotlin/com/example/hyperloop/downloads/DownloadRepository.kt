package com.example.hyperloop.downloads

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DownloadRepository : JpaRepository<HyperloopDownload, Int> {

    fun findAllByOrderByDateDesc(pageable: Pageable): Page<HyperloopDownload>

    fun findTop20ByOrderByDateDesc(): List<HyperloopDownload>
}