package com.example.forumsystemwebproject.models;

import com.example.forumsystemwebproject.models.UserModels.RegisteredUser;
import jakarta.persistence.*;

public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private RegisteredUser user;

    @Column(name = "number")
    private String number;

    public PhoneNumber() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RegisteredUser getUser() {
        return user;
    }

    public void setUser(RegisteredUser user) {
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
