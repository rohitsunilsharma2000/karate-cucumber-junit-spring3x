package com.example.supportservice;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TuringLLMTuningSystem {

    @Test
    void contextLoads () {
        // This test will pass if the Spring application context loads successfully
        assertThat(true).isTrue(); // Optional, just indicates success explicitly
    }
}

