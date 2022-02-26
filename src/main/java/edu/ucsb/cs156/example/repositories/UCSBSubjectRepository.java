package edu.ucsb.cs156.example.repositories;

import edu.ucsb.cs156.example.entities.UCSBSubject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UCSBSubjectRepository extends CrudRepository<UCSBSubject, Long> {
    //Iterable<UCSBSubject> findBySubject(String subject);
}
