package com.example.videohosting.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 **StreamingControllerMockTest**
 *
 * This test class verifies the functionality of the StreamingController endpoint by mocking
 * the construction of UrlResource using Mockito. It uses:
 *   - @SpringBootTest to load the full application context.
 *   - @AutoConfigureMockMvc to set up the web environment.
 *   - @Transactional to auto-rollback database changes after each test.
 *   - @WithMockUser to simulate a secured user context.
 *   - @TestMethodOrder with @Order to control test execution order.
 *
 * File: src/test/java/com/example/videohosting/controller/StreamingControllerMockTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@WithMockUser
@TestMethodOrder(OrderAnnotation.class)
public class StreamingControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test case for successful video streaming.
     *
     * This test mocks the UrlResource constructor to simulate a valid video file.
     * It expects a 200 OK response, with the proper Content-Disposition header and Content-Type.
     */
    @Test
    @Order(1)
    public void testStreamVideoSuccessMocked() throws Exception {
        try (MockedConstruction<UrlResource> mocked = Mockito.mockConstruction(UrlResource.class,
                                                                               (mock, context) -> {
                                                                                   // Simulate that the resource exists and returns a filename.
                                                                                   Mockito.when(mock.exists()).thenReturn(true);
                                                                                   Mockito.when(mock.getFilename()).thenReturn("test-video.mp4");
                                                                               })) {

            mockMvc.perform(get("/api/stream/test-video.mp4"))
                   .andExpect(status().isOk())
                   .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                                              containsString("inline; filename=\"test-video.mp4\"")))
                   .andExpect(content().contentType(MediaType.parseMediaType("video/mp4")));
        }
    }




}
