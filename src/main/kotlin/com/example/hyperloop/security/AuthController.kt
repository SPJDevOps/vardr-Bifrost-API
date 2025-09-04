package com.example.hyperloop.security

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val userContextService: UserContextService) {

    @GetMapping("/me")
    fun getCurrentUser(): UserInfo? {
        return userContextService.getCurrentUserInfo()
    }

    @GetMapping("/status")
    fun getAuthStatus(): AuthStatus {
        return AuthStatus(
            isAuthenticated = userContextService.isAuthenticated(),
            userId = userContextService.getCurrentUserId(),
            username = userContextService.getCurrentUsername()
        )
    }
}

data class AuthStatus(
    val isAuthenticated: Boolean,
    val userId: String?,
    val username: String?
)
