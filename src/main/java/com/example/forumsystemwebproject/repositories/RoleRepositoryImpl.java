package com.example.forumsystemwebproject.repositories;

import com.example.forumsystemwebproject.exceptions.EntityNotFoundException;
import com.example.forumsystemwebproject.models.Role;
import com.example.forumsystemwebproject.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@PropertySource("classpath:application.properties")
public class RoleRepositoryImpl implements RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Role> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role", Role.class);
            return query.list();
        }
    }

    @Override
    public Role getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Role result = session.get(Role.class, id);
            if (result == null) {
                throw new EntityNotFoundException("Role", id);
            }
            return result;
        }
    }

    @Override
    public Role getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role where name = :name", Role.class);
            query.setParameter("name", name);
            List<Role> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Role", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(role);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(role);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Role role = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(role);
            session.getTransaction().commit();
        }
    }

}
