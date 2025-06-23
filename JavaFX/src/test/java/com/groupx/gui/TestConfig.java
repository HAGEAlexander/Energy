package com.groupx.gui;

import com.groupx.gui.service.RestClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public RestClient restClient() {
        return Mockito.mock(RestClient.class);
    }
}
