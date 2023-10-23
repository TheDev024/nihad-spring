package geo.sopel.nihadinkorea.repository

import geo.sopel.nihadinkorea.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository

interface AppUserRepository : JpaRepository<AppUser, Long> {
    fun findAppUserByUsername(username: String): AppUser?

    fun existsAppUserByUsername(username: String): Boolean
}
