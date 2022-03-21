package com.reolle.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String host; //app.host localhost:8080 받아서 서버단에서 쓸수잇슴
}
