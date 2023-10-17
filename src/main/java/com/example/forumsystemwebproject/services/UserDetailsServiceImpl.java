package com.example.forumsystemwebproject.services;

import com.example.forumsystemwebproject.models.UserModels.Admin;
import com.example.forumsystemwebproject.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, SessionFactory sessionFactory) {
        this.userRepository = userRepository;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.get(username).orElseThrow(
//                () -> new UsernameNotFoundException("User not found"));
//        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), getAuthorities(user.getRoles()));
//
    return null; //tuk ne znam kakvo napravih ;D
    }

    private Collection<? extends GrantedAuthority> getAuthorities(org.springframework.security.core.userdetails.User user) {
        // Determine the user's role based on your database structure or logic
        // For example, check if the user is in the "admins" table
        if (isAdmin(user)) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            // If not an admin, assign a default role (e.g., "ROLE_USER")
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    private boolean isAdmin(org.springframework.security.core.userdetails.User user) {
        // Implement logic to check if the user is an admin based on your database structure
        // For example, check if there is a corresponding record in the "admins" table
        // and return true if the user is an admin, otherwise return false
        try (Session session = sessionFactory.openSession()) {
            Query<Admin> query = session.createQuery("FROM Admin WHERE user = :user", Admin.class);
            query.setParameter("user", user);
            List<Admin> adminList = query.getResultList();
            return !adminList.isEmpty();
        }
    }
}
