package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    private ConcurrentHashMap cache = new ConcurrentHashMap();


    @PostMapping(("/api/persons"))
    public ResponseEntity<PersonEntity> save(@RequestBody PersonCommand personCommand) {

        try {
            var savedPerson = personRepository.save(personCommand.toEntity());
            cache.put(savedPerson.getId(), savedPerson);
            ResponseEntity.created(location(savedPerson.getId())).build();
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            ResponseEntity.unprocessableEntity().build();
        }

        if (personCommand.isNotValid()) {
            ResponseEntity.unprocessableEntity().build();
        }

        if (personCommand.isNotSyntacticallyInvalid()) {
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/api/persons/{id}")
    public ResponseEntity<PersonEntity> search(@PathVariable("id") String personId) {
        var personUUID = UUID.fromString(personId);

        if (cache.containsKey(personUUID)) {
            return ResponseEntity.ok().body((PersonEntity) cache.get(personUUID));
        } else {
            var searchedPerson = personRepository.findById(personUUID);

            if (searchedPerson.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            cache.put(searchedPerson.get().getId(), searchedPerson);

            return ResponseEntity.ok().body(searchedPerson.get());
        }
    }

    @GetMapping("/api/persons/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok().body(personRepository.count());
    }

   @GetMapping("/api/persons")
    public ResponseEntity<List<PersonEntity>> searchTerm(@RequestParam(required = true, name = "t") String t) {

       if (cache.containsKey(t)) {
           return ResponseEntity.ok().body((List<PersonEntity>) cache.get(t));
       }

       var searchedTerm = personRepository.getByTerm(t);
       cache.put(t, searchedTerm);

        return ResponseEntity.ok().body(searchedTerm);
    }

    private URI location(UUID id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

}
