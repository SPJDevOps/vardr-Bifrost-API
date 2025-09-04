package com.example.hyperloop.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class UserContextService {

    fun getCurrentUserId(): String? {
        val jwt = getCurrentJwt()
        return jwt?.getClaimAsString("sub")
    }

    fun getCurrentUsername(): String? {
        val jwt = getCurrentJwt()
        return jwt?.getClaimAsString("preferred_username") 
            ?: jwt?.getClaimAsString("username")
            ?: jwt?.getClaimAsString("sub")
    }

    fun getCurrentUserEmail(): String? {
        val jwt = getCurrentJwt()
        return jwt?.getClaimAsString("email")
    }

    fun isAuthenticated(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.isAuthenticated == true && authentication.principal is Jwt
    }

    private fun getCurrentJwt(): Jwt? {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication?.principal is Jwt) {
            authentication.principal as Jwt
        } else {
            null
        }
    }

    fun getCurrentUserInfo(): UserInfo? {
        val jwt = getCurrentJwt() ?: return null
        
        return UserInfo(
            userId = jwt.getClaimAsString("sub"),
            username = getCurrentUsername() ?: "unknown",
            email = jwt.getClaimAsString("email")
        )
    }
}

data class UserInfo(
    val userId: String,
    val username: String,
    val email: String?
)
