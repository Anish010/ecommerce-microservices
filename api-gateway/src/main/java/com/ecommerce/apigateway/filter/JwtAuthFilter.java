package com.ecommerce.apigateway.filter;

import com.ecommerce.apigateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Global filter that runs on every request.
 *
 * Flow:
 *  1. Public path?  → pass through immediately
 *  2. No/invalid Authorization header? → 401
 *  3. Parse + verify JWT → extract userId claim
 *  4. Mutate request with X-User-Id header → forward
 *  5. Any exception → 401
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID_HEADER = "X-User-Id";

    /** Endpoints that do NOT require a JWT. */
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/actuator/health"
    );

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    public JwtAuthFilter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    void init() {
        // Build the HMAC-SHA key once at startup; the secret must be ≥ 256 bits (32 chars)
        secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
        log.info("JwtAuthFilter initialised");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // ── 1. Skip auth for public endpoints ──────────────────────────────
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // ── 2. Extract Authorization header ───────────────────────────────
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Missing or malformed Authorization header for path: {}", path);
            return unauthorized(exchange);
        }

        // ── 3. Validate JWT & extract userId ──────────────────────────────
        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.get("userId", String.class);

            if (userId == null) {
                // Fall back to subject if userId claim is absent
                userId = claims.getSubject();
            }

            // ── 4. Forward request with X-User-Id header ──────────────────
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header(USER_ID_HEADER, userId)
                    .build();

            log.debug("JWT valid — forwarding userId={} to path={}", userId, path);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception ex) {
            // ── 5. Any JWT error → 401 ────────────────────────────────────
            log.warn("JWT validation failed for path={}: {}", path, ex.getMessage());
            return unauthorized(exchange);
        }
    }

    // Run before other filters (lower number = higher priority)
    @Override
    public int getOrder() {
        return -1;
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}