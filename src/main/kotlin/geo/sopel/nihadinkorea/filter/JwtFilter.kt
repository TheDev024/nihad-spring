package geo.sopel.nihadinkorea.filter

import geo.sopel.nihadinkorea.TokenType
import geo.sopel.nihadinkorea.entity.AppUser
import geo.sopel.nihadinkorea.service.CustomUserDetailsService
import geo.sopel.nihadinkorea.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(private val jwtService: JwtService, private val customUserDetailsService: CustomUserDetailsService) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = getTokenFromRequest(request) ?: throw RuntimeException("Jwt Not Found")

            if (jwtService.validateToken(token)) {
                val subject = jwtService.getSubjectFromToken(token)
                val (tokenType, username, loginAttempt) = subject.split(";")

                if (TokenType.valueOf(tokenType) != TokenType.ACCESS) throw RuntimeException("Unauthorized")

                val user = customUserDetailsService.loadUserByUsername(username) as AppUser
                if (user.loginAttempts != loginAttempt.toLong()) throw RuntimeException("Session expired")
                val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Error cixdi dana")
        }

        doFilter(request, response, filterChain)
    }

    fun getTokenFromRequest(request: HttpServletRequest): String? {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return if (authHeader.isEmpty()) null else authHeader.removePrefix("Bearer ")
    }
}

/**
 * request ->
 */
