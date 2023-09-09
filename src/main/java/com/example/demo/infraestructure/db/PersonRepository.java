package com.example.demo.infraestructure.db;

import com.example.demo.domain.PersonEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends CrudRepository<PersonEntity, UUID> {

    @Query(value = """
            SELECT *
            FROM person p
            where
            p.name like %:term%
            or
            p.nickname like %:term%
            """,
            nativeQuery = true
    )
    List<PersonEntity> getByTerm(String term);
}
