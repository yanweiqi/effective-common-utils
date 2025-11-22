package com.effective.common.flux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * WebFlux 控制器测试
 */
@WebFluxTest(FluxDemoController.class)
public class FluxDemoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetFluxDemo() {
        webTestClient.get().uri("/demo/flux")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class)
                .contains("Hello", "Reactive", "World");
    }
}
