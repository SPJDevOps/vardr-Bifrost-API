package com.example.hyperloop.downloads

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class DownloadController(private val downloadService: DownloadService) {

    @GetMapping("downloads")
    fun getAllDownloads(pageable: Pageable): Page<HyperloopDownload> = 
        this.downloadService.getAllDownloads(pageable)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("downloads")
    fun handleIncomingDownloadRequest(@RequestBody downloadRequest: FrontendDownloadRequest) =
        this.downloadService.saveDownloadRequest(downloadRequest)
}