package com.rajesh.kafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajesh.kafka.domain.Book;
import com.rajesh.kafka.domain.LibraryEvent;
import com.rajesh.kafka.domain.LibraryEventType;
import com.rajesh.kafka.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(LibraryEventController.class)
@AutoConfigureMockMvc
public class LibraryEventsProducerControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LibraryEventProducer libraryEventProducer;

    ObjectMapper mapper=new ObjectMapper();

    @Test
    public void testPostLibraryEvent() throws Exception{
        Book book = Book.builder()
                .bookId(100)
                .bookName("Java 11")
                .bookAuthor("Rajesh")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .libraryEventType(LibraryEventType.NEW)
                .book(book)
                .build();

        String json = mapper.writeValueAsString(libraryEvent);
        doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));
        mockMvc.perform(post("/v1/libraryevent")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
