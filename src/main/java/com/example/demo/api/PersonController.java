package com.example.demo.api;

import com.example.demo.domain.PersonEntity;
import com.example.demo.infraestructure.db.PersonRepository;
import com.example.demo.infraestructure.db.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RedisTemplate<String, Object> template;

    @Autowired
    private RedisService redisService;

    Logger logger = Logger.getLogger(PersonController.class.getName());


    @PostMapping(("/api/persons"))
    public ResponseEntity<PersonEntity> save(@RequestBody PersonCommand personCommand) {

        if (personCommand.isNotValid()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        if (personCommand.isNotSyntacticallyInvalid()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var savedPerson = personRepository.save(personCommand.toEntity());
            redisService.saveObject(savedPerson.getId().toString(), savedPerson);

           return ResponseEntity.created(location(savedPerson.getId())).build();
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/api/persons/{id}")
    public ResponseEntity<PersonEntity> search(@PathVariable("id") String personId) {
        var personUUID = UUID.fromString(personId);
        var cachedPerson = template.opsForValue().get(personId);

        if (cachedPerson != null) {
            logger.info("Retrieving from cache: " + ((PersonEntity) cachedPerson).getId() );
            return ResponseEntity.ok().body((PersonEntity) cachedPerson);
        } else {
            var dbPerson = personRepository.findById(personUUID);

            if (dbPerson.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            logger.info("Retrieving from db: " + dbPerson.get().getId());
            redisService.saveObject(dbPerson.get().getId().toString(), dbPerson.get());

            return ResponseEntity.ok().body(dbPerson.get());
        }
    }

    @GetMapping("/api/persons/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok().body(personRepository.count());
    }

   @GetMapping("/api/persons")
    public ResponseEntity<List<PersonEntity>> searchTerm(@RequestParam(required = true, name = "t") String t) {
       var cachedPerson = redisService.getObjectList(t);

       if (!cachedPerson.isEmpty()) {
           logger.info("Retrieving from cache: " + t);

           return ResponseEntity.ok().body(cachedPerson);
       }

       var personsByTerm = personRepository.getByTerm(t);

       if (!personsByTerm.isEmpty()) {
           redisService.saveObjectList(t, personsByTerm);
       }

       return ResponseEntity.ok().body(personsByTerm);
    }

    private URI location(UUID id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

}
