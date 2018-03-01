package app.service;

import app.entities.ContactsEntity;
import app.repository.ContactRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@Service("contactService")
public class ContactServiceImpl implements ContactService {
    private ContactRepository contactRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ContactsEntity> findAll() {
        return Lists.newArrayList(contactRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ContactsEntity findById(Long id) {
        return contactRepository.findById(id).get();
    }

    @Override
    public ContactsEntity save(ContactsEntity contact) {
        return contactRepository.save(contact);
    }

    @Autowired
    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
} // class ContactServiceImpl
