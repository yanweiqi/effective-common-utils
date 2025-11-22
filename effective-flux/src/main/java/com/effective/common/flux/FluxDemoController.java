package com.effective.common.flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * WebFlux 响应式 REST API 演示
 */
@RestController
public class FluxDemoController {

    @GetMapping("/demo/flux")
    public Flux<String> getFluxDemo() {
        return Flux.just("Hello", "Reactive", "World");
    }
}
