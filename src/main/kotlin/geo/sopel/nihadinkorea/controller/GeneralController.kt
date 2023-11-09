package geo.sopel.nihadinkorea.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GeneralController {
    @GetMapping("/nihadin-kefi")
    fun nihadinKefi(): String = "Eladi!"

    @GetMapping("/qaranliq")
    fun qaranliq(): String = "Get o yanda oyna"
}
