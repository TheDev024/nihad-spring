package geo.sopel.nihadinkorea.service

import geo.sopel.nihadinkorea.TokenType
import geo.sopel.nihadinkorea.dao.LoginDao
import geo.sopel.nihadinkorea.dao.RegisterDao
import geo.sopel.nihadinkorea.entity.AppUser
import geo.sopel.nihadinkorea.repository.AppUserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AppUserService(private val appUserRepository: AppUserRepository, private val authenticationManager: AuthenticationManager, private val jwtService: JwtService) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun register(registerDao: RegisterDao): AppUser {
        if (appUserRepository.existsAppUserByUsername(registerDao.username)) throw RuntimeException("User already exists")

        return appUserRepository.save(
            AppUser(
                username = registerDao.username,
                password = passwordEncoder.encode(registerDao.password),
                role = registerDao.role
            )
        )
    }

    fun login(loginDao: LoginDao): Map<String, String> {
        val authentication = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginDao.username,
                    loginDao.password
                )
            )
        } catch (e: AuthenticationException) {
            throw RuntimeException("Giremmezsen dana")
        }

        SecurityContextHolder.getContext().authentication  = authentication

        val user = authentication.principal as AppUser

        return generateTokens(user)
    }

    fun generateTokens(user: AppUser): Map<String, String> {
        val loginAttempts = ++user.loginAttempts
        appUserRepository.save(user)

        val accessToken = jwtService.generateToken("${TokenType.ACCESS};${user.username};$loginAttempts", 86400000)
        val refreshToken = jwtService.generateToken("${TokenType.REFRESH};${user.username};$loginAttempts", 86400000 * 3)

        return mapOf(
            "accessToken" to accessToken,
            "refreshToken" to refreshToken
        )
    }
}
