package com.proyecto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.csrf(csrf -> csrf.disable())
    	http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth

                // ══════════════════════════════════════════
                // PÚBLICOS — van PRIMERO, más específicos
                // ══════════════════════════════════════════
                .requestMatchers(HttpMethod.POST, "/api/usuario/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuario/registro").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/producto").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/producto/**").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/categoria").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/resena/producto/**").permitAll()

                // ══════════════════════════════════════════
                // ADMIN — van después de los públicos
                // ══════════════════════════════════════════
                .requestMatchers(HttpMethod.POST,   "/api/categoria").hasRole("Admin")
                .requestMatchers(HttpMethod.PUT,    "/api/categoria/**").hasRole("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/categoria/**").hasRole("Admin")

                .requestMatchers(HttpMethod.GET,    "/api/proveedor").hasRole("Admin")
                .requestMatchers(HttpMethod.GET,    "/api/proveedor/**").hasRole("Admin")
                .requestMatchers(HttpMethod.POST,   "/api/proveedor").hasRole("Admin")
                .requestMatchers(HttpMethod.PUT,    "/api/proveedor/**").hasRole("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/proveedor/**").hasRole("Admin")

                .requestMatchers(HttpMethod.POST,   "/api/producto").hasRole("Admin")
                .requestMatchers(HttpMethod.PUT,    "/api/producto/**").hasRole("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/producto/**").hasRole("Admin")

                .requestMatchers(HttpMethod.GET,    "/api/cliente").hasRole("Admin")
                .requestMatchers(HttpMethod.GET,    "/api/cliente/**").hasRole("Admin")
                .requestMatchers(HttpMethod.POST,   "/api/cliente").hasRole("Admin")
                .requestMatchers(HttpMethod.PUT,    "/api/cliente/**").hasRole("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/cliente/**").hasRole("Admin")

                .requestMatchers(HttpMethod.GET,    "/api/usuario").hasRole("Admin")
                .requestMatchers(HttpMethod.GET,    "/api/usuario/**").hasRole("Admin")

                .requestMatchers(HttpMethod.GET,    "/api/pedido").hasRole("Admin")
                .requestMatchers(HttpMethod.PUT,    "/api/pedido/*/estado").hasRole("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/pedido/**").hasRole("Admin")
                
                .requestMatchers(HttpMethod.GET, "/api/dashboard").hasRole("Admin")

                // ══════════════════════════════════════════
                // CLIENTE — autenticado con rol Cliente
                // ══════════════════════════════════════════
                .requestMatchers(HttpMethod.POST, "/api/pedido/confirmar").hasRole("Cliente")
                .requestMatchers(HttpMethod.GET,  "/api/pedido/cliente/**").hasRole("Cliente")
                .requestMatchers(HttpMethod.POST, "/api/resena/**").hasRole("Cliente")
                .requestMatchers(HttpMethod.POST, "/api/chat").hasRole("Cliente")
                
                
                

                // ── BOLETA — Admin y Cliente pueden descargar ────────────
                .requestMatchers(HttpMethod.GET, "/api/boleta/**").authenticated()

                // Cualquier otra cosa → autenticado
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    
    
    // ← AGREGADO — CORS para Railway y cualquier frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}