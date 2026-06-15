package com.proyecto.config;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
 
@Configuration
@EnableAsync
public class AsyncConfig {
    // Solo necesita esta anotación para habilitar @Async
}