package ru.practicum.ewmservice.statistics;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.practicum.servicestatsclient.ServiceStatsClient;

@Configuration
@Import(ServiceStatsClient.class)
public class StatConfig {
}
