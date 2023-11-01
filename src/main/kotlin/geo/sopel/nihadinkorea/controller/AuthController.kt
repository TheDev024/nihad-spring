package geo.sopel.nihadinkorea.controller

import geo.sopel.nihadinkorea.dao.RegisterDao
import geo.sopel.nihadinkorea.service.AppUserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val appUserService: AppUserService) {
    @PostMapping("/register")
    fun register(@RequestBody registerDao: RegisterDao) = appUserService.register(registerDao)
}