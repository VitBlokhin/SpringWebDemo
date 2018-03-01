package app.service;

import app.entities.ContactsEntity;

import java.util.List;

public interface ContactService {
    List<ContactsEntity> findAll();
    ContactsEntity findById(Long id);
    ContactsEntity save(ContactsEntity contact);
} // interface ContactService
