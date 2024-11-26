package com.nevoroman.delivery.order.config

import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.security.oauth2.server.resource.web.HeaderBearerTokenResolver
import org.springframework.security.web.SecurityFilterChain
import java.util.*
import javax.inject.Singleton


@Configuration
@EnableWebSecurity
class SecurityConfig {

    // Keys should be stored in the identity service, received from it and cached, but well, we don't have identity service
    @Singleton
    @Bean
    fun jwtKey(): RSAKey {
        return RSAKeyGenerator(2048)
            .keyID(UUID.randomUUID().toString())
            .generate()
    }

    @Bean
    fun jwtDecoder(key: RSAKey): JwtDecoder =
        NimbusJwtDecoder
            .withPublicKey(key.toRSAPublicKey())
            .build()

    @Bean
    fun authProvider(decoder: JwtDecoder) = JwtAuthenticationProvider(decoder)

    @Bean
    @Primary
    fun filterChain(http: HttpSecurity, decoder: JwtDecoder): SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeHttpRequests { authorize("/**", authenticated) }
            oauth2ResourceServer {
                bearerTokenResolver = HeaderBearerTokenResolver("X-Authorization")
                jwt {
                    jwtDecoder = decoder
                    jwtAuthenticationConverter = jwtAuthenticationConverter()
                }
            }
        }

        return http.build()
    }

    private fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
            val role = jwt.getClaimAsString("role")
            AuthorityUtils.commaSeparatedStringToAuthorityList(role)
        }
        return converter
    }

}