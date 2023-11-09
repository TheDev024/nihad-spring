package geo.sopel.nihadinkorea.service

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {
    private val logger = LoggerFactory.getLogger(JwtService::class.java)

    fun key(): Key =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode("Nihad=Turkiyededi=Magistratura=Oxuyur=Amma=Ve=Lakin=Aramizda=Qalsin"))

    fun generateToken(subject: String, expirePeriod: Long): String = Jwts.builder()
        .setSubject(subject)
        .signWith(key())
        .setIssuedAt(Date())
        .setExpiration(Date(Date().time + expirePeriod))
        .compact()

    fun getSubjectFromToken(token: String): String =
        Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).body.subject

    fun validateToken(token: String): Boolean {
        var isValid = false

        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token)
            isValid = true
        } catch (e: JwtException) {
            logger.info("Error verdi dana. Inanmirsansa al mesaji oxu:\n${e.message}")
        }

        return isValid
    }
}
