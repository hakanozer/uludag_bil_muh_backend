package com.works.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Data
@Embeddable
public class IdentityInfo {

    private String nationalId;
    private LocalDate birthDate;
    private String birthPlace;
    private String motherName;
    private String fatherName;

}
