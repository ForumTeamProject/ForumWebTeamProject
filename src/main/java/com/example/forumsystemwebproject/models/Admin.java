package com.example.forumsystemwebproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User{
//    admin_id     int auto_increment primary key,
//    user_id      int not null references users (user_id),
//    phone_number varchar(15) unique

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private int adminId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "phone_number")
    private String phoneNumber;

    public Admin(){

    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
