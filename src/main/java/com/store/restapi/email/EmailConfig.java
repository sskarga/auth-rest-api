package com.store.restapi.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.mail")
@Getter
@Setter
@NoArgsConstructor
public class EmailConfig {
    // Email
    private String from;

    // Хост вложенных ресурсов - http://localhost:8000
    private String resourceHost;
}
