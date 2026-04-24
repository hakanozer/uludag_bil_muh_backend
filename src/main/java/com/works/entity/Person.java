package com.works.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String phone;

    @ManyToMany
    private List<Address> addressList;

    @Embedded
    private IdentityInfo identityInfo;

}
