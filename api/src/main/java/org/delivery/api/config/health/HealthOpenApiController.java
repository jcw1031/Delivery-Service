package org.delivery.api.config.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/open-api")
@RestController
public class HealthOpenApiController {

    @GetMapping("/health")
    public void health() {
        log.info("health call");
    }
}
