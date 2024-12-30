package com.medco.Travel.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String file;
    private String fileName;
    private Long fileSize;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_uuid", referencedColumnName = "userUuid", nullable = false)
    private User user;


}
