package app.repository;

import app.entities.ContactsEntity;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<ContactsEntity, Long> {
} // interface ContactRepository
