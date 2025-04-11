package com.frend.planit.domain.image.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Entity
public class ImageTestEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY) // AUTO_INCREMENT
    public long id;

    public String name;

    public int age;

    public String email;

    public ImageTestEntity() {
    }

    public ImageTestEntity(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}