package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ThreadServiceTest {

    @Mock
    private ThreadRepository threadRepository;

    @InjectMocks
    private ThreadService threadService;

    private Threads thread;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        thread = Threads.builder()
                        .id(1L)
                        .title("Sample Thread")
                        .content("Sample Content")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
    }

    @Test
    void testCreateThread_Success() {
        when(threadRepository.save(any(Threads.class))).thenReturn(thread);

        Threads result = threadService.createThread(new Threads());

        assertNotNull(result);
        assertEquals("Sample Thread", result.getTitle());
        verify(threadRepository).save(any(Threads.class));
    }

    @Test
    void testUpdateThread_Success() {
        Threads updated = Threads.builder()
                                 .title("Updated Title")
                                 .content("Updated Content")
                                 .build();

        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(threadRepository.save(any(Threads.class))).thenReturn(thread);

        Threads result = threadService.updateThread(1L, updated);

        assertNotNull(result);
        assertEquals(thread.getId(), result.getId());
        verify(threadRepository).save(any(Threads.class));
    }

    @Test
    void testUpdateThread_NotFound() {
        when(threadRepository.findById(99L)).thenReturn(Optional.empty());

        Threads updated = Threads.builder().title("T").content("C").build();

        assertThrows(ResourceNotFoundException.class, () -> {
            threadService.updateThread(99L, updated);
        });
    }

    @Test
    void testDeleteThread_Success() {
        when(threadRepository.existsById(1L)).thenReturn(true);
        doNothing().when(threadRepository).deleteById(1L);

        threadService.deleteThread(1L);

        verify(threadRepository).deleteById(1L);
    }

    @Test
    void testDeleteThread_NotFound() {
        when(threadRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            threadService.deleteThread(99L);
        });
    }

    @Test
    void testGetThread_Success() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));

        Threads result = threadService.getThread(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetThread_NotFound() {
        when(threadRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            threadService.getThread(999L);
        });
    }

    @Test
    void testGetAllThreads() {
        when(threadRepository.findAll()).thenReturn(List.of(thread));

        List<Threads> result = threadService.getAllThreads();

        assertEquals(1, result.size());
        verify(threadRepository).findAll();
    }
}
