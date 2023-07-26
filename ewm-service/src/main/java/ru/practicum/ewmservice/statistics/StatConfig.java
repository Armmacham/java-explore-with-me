package ru.practicum.ewmservice.statistics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.servicestatsclient.ServiceStatsClient;

@Configuration
public class StatConfig {
    @Bean
    public ServiceStatsClient createClient() {
        return new ServiceStatsClient();
    }
}
