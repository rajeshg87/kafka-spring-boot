package com.rajesh.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rajesh.kafka.domain.LibraryEvent;
import com.rajesh.kafka.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.rajesh.kafka.domain.LibraryEventType.NEW;
import static com.rajesh.kafka.domain.LibraryEventType.UPDATE;

@RestController
public class LibraryEventController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEvent.setLibraryEventType(NEW);
        libraryEventProducer.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping("/v2/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEventUsingProducerRecord(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEvent.setLibraryEventType(NEW);
        libraryEventProducer.sendLibraryEventWithProducerRecord(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping("/v2/libraryevent")
    public ResponseEntity<?> updateLibraryEventUsingProducerRecord(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        if(null  == libraryEvent.getLibraryEventId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Library Event Id is missing");
        }
        libraryEvent.setLibraryEventType(UPDATE);
        libraryEventProducer.sendLibraryEventWithProducerRecord(libraryEvent);
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }
}
