package geo.sopel.nihadinkorea.configuration

import geo.sopel.nihadinkorea.filter.JwtFilter
import geo.sopel.nihadinkorea.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfiguration(private val userDetailsService: CustomUserDetailsService, private val jwtFiler: JwtFilter) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()

        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(userDetailsService)

        return provider
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain = httpSecurity
        .csrf { csrf -> csrf.disable() }
        .addFilterBefore(jwtFiler, UsernamePasswordAuthenticationFilter::class.java)
        .authenticationProvider(authenticationProvider())
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests { http ->
            http.requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                .anyRequest().authenticated()
        }
        .build()
}
