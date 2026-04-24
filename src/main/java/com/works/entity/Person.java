package com.works.entity;

import com.works.util.EStatus;
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

    @ElementCollection
    private List<String> phoneList;

    @ManyToMany
    private List<Address> addressList;

    @Embedded
    private IdentityInfo identityInfo;

    @Enumerated(EnumType.STRING)
    private EStatus status = EStatus.ACTIVE;

}
