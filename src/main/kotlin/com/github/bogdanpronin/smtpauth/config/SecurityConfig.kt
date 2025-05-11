package com.github.bogdanpronin.smtpauth.config


import com.github.bogdanpronin.smtpauth.service.MailboxService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val mailboxService: MailboxService
) {

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            mailboxService.findByUsername(username)?.let {
                org.springframework.security.core.userdetails.User(
                    it.username,
                    it.password,
                    emptyList()
                )
            } ?: throw UsernameNotFoundException("User not found: $username")
        }
        }
        @Bean
        fun authenticationProvider(): AuthenticationProvider {
            return object : AuthenticationProvider {
                override fun authenticate(authentication: Authentication): Authentication {
                    val username = authentication.name
                    val password = authentication.credentials.toString()
                    val user = mailboxService.findByUsername(username)
                        ?: throw UsernameNotFoundException("User not found: $username")
                    if (mailboxService.verifySSHA512(password, user.password)) {
                        return UsernamePasswordAuthenticationToken(
                            username,
                            password,
                            emptyList()
                        )
                    } else {
                        throw BadCredentialsException("Invalid password")
                    }
                }

                override fun supports(authentication: Class<*>): Boolean {
                    return authentication == UsernamePasswordAuthenticationToken::class.java
                }
            }
        }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { } // Enable CORS
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/auth/register").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic { }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): org.springframework.web.cors.CorsConfigurationSource {
        val configuration = org.springframework.web.cors.CorsConfiguration()
        configuration.allowedOrigins = listOf(
            "https://mail.messenger-mail.ru",
            "http://localhost:3001",
            "http://localhost:3000"
            )
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = org.springframework.web.cors.UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
    }