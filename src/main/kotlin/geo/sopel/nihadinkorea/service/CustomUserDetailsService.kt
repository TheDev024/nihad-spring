package geo.sopel.nihadinkorea.service

import geo.sopel.nihadinkorea.repository.AppUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.nio.file.attribute.UserPrincipalNotFoundException

@Service
class CustomUserDetailsService(private val appUserRepository: AppUserRepository) : UserDetailsService {
    @Throws(UserPrincipalNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails = appUserRepository.findAppUserByUsername(username)
        ?: throw UserPrincipalNotFoundException("User not found with username: $username")
}
