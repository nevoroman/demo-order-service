package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.OrderData.userId
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpMethod
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Instant

@Tag("IntegrationTests")
@ActiveProfiles("integration")
@AutoConfigureWireMock(port = 8081)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ControllerIT: TestcontainersIT() {

    data class Endpoint(val url: String, val method: HttpMethod)

    @Autowired
    protected lateinit var testRepository: TestRepository

    @LocalServerPort
    protected val localPort: Int = 0

    @Autowired
    private lateinit var key: RSAKey

    protected fun baseRequest(): RequestSpecification {
        return RestAssured.given()
            .baseUri("http://localhost").port(localPort)
            .log().ifValidationFails()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
    }

    protected fun Endpoint.sendUnauthorized(body: Any): ValidatableResponse {
        return baseRequest()
            .body(body)
            .send(this).then()
    }

    protected fun Endpoint.send(body: Any, role: UserRole = UserRole.USER): ValidatableResponse {
        return baseRequest()
            .header("X-Authorization", createToken(role.name))
            .body(body)
            .send(this).then()
    }

    private fun RequestSpecification.send(endpoint: Endpoint): Response {
        return when (endpoint.method) {
            HttpMethod.GET -> this.get(endpoint.url)
            HttpMethod.HEAD -> this.head(endpoint.url)
            HttpMethod.POST -> this.post(endpoint.url)
            HttpMethod.PUT -> this.put(endpoint.url)
            HttpMethod.PATCH -> this.patch(endpoint.url)
            HttpMethod.DELETE -> this.delete(endpoint.url)
            HttpMethod.OPTIONS -> this.options(endpoint.url)
            else -> {
                throw NotImplementedError("Unsupported HTTP method")
            }
        }
    }

    private fun createToken(role: String): String? {
        val encoder = NimbusJwtEncoder(ImmutableJWKSet(JWKSet(key)))
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .subject(userId().toString())
            .claim("role", role)
            .build()

        return encoder.encode(
            JwtEncoderParameters.from(claims)
        ).tokenValue
    }

    fun <T: Any> T.`save to database`(): T {
        return testRepository.saveEntity(this)
    }

    @AfterEach
    fun cleanupDatabase() {
        testRepository.cleanDatabase()
    }

    companion object {
        @ServiceConnection
        fun postgresContainer(): PostgreSQLContainer<*> {
            return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
        }
    }
}