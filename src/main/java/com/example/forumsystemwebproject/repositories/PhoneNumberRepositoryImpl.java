package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.models.PhoneNumber;
import com.example.forumsystemwebproject.models.User;
import com.example.forumsystemwebproject.repositories.contracts.PhoneNumberRepository;
import com.example.forumsystemwebproject.repositories.contracts.UserRepository;
import com.example.forumsystemwebproject.services.PhoneNumberServiceImpl;
import com.example.forumsystemwebproject.services.contracts.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneNumberRepositoryImpl implements PhoneNumberRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PhoneNumberRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<PhoneNumber> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<PhoneNumber> query = session.createQuery("from PhoneNumber", PhoneNumber.class);
            return query.list();
         }
    }

    @Override
    public PhoneNumber getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            PhoneNumber number = session.get(PhoneNumber.class, id);
            if (number == null) {
                throw new EntityNotFoundException("Number", id);
            }
            return number;
        }
    }

    @Override
    public void create(PhoneNumber number) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(number);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PhoneNumber number) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(number);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PhoneNumber number) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(number);
            session.getTransaction().commit();
        }
    }
}
