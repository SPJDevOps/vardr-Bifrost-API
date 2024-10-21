package com.example.hyperloop.downloads

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DownloadRepository : JpaRepository<HyperloopDownload, Int> {

    fun findAllByOrderByDateDesc(): List<HyperloopDownload>
}