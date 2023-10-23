package geo.sopel.nihadinkorea.service

import geo.sopel.nihadinkorea.dao.RegisterDao
import geo.sopel.nihadinkorea.entity.AppUser
import geo.sopel.nihadinkorea.repository.AppUserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AppUserService(private val appUserRepository: AppUserRepository) {
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
}