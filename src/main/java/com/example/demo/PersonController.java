package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping(("/api/persons"))
    public ResponseEntity<PersonEntity> save(@RequestBody @Valid PersonCommand personCommand) {
        var savedPerson = personRepository.save(personCommand.toEntity());

        return ResponseEntity.created(location(savedPerson.getId())).build();
    }

    @GetMapping("/api/persons/{id}")
    public ResponseEntity<Optional<PersonEntity>> search(@PathVariable("id") String personId) {
        var searchedPerson = personRepository.findById(UUID.fromString(personId));
        return ResponseEntity.ok().body(searchedPerson);
    }

    @GetMapping("/api/persons/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok().body(personRepository.count());
    }

    @GetMapping("/api/persons")
    public ResponseEntity<List<PersonEntity>> searchTerm(@RequestParam(required = true) String t) {
        return ResponseEntity.ok().body(personRepository.getByTerm(t));
    }

    private URI location(UUID id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

}
