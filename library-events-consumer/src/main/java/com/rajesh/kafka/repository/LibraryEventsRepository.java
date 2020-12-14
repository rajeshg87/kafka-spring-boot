package com.rajesh.kafka.repository;

import com.rajesh.kafka.entity.LibraryEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface LibraryEventsRepository extends CrudRepository<LibraryEvent, Integer> {
}
